def call(Map config) {
    def appName      = config.appName      ?: error('appName is required')
    def nexusUrl     = config.nexusUrl     ?: error('nexusUrl is required')
    def ecrRepo      = config.ecrRepo      ?: error('ecrRepo is required')
    def awsRegion    = config.awsRegion    ?: 'us-east-1'
    def eksCluster   = config.eksCluster   ?: 'enterprise-eks-us-east-1'
    def namespace    = config.namespace    ?: 'ecommerce'
    def helmChart    = config.helmChart    ?: "helm-${appName}"
    def appVersion   = config.appVersion   ?: '1.0.0'
    def sonarUrl     = config.sonarUrl     ?: 'http://sonarqube:9000'
    def appDir       = config.appDir       ?: "app-${appName}"
    def imageTag     = "${appVersion}-${env.BUILD_NUMBER}"
    def ecrRegistry  = ecrRepo.split('/')[0]

    pipeline {
        agent any

        tools {
            maven 'Maven-3.9'
            jdk   'JDK-17'
        }

        options {
            buildDiscarder(logRotator(numToKeepStr: '10'))
            timeout(time: 60, unit: 'MINUTES')
            timestamps()
        }

        stages {

            stage('Build WAR') {
                steps {
                    dir(appDir) {
                        sh 'mvn clean package -DskipTests=false -Dmaven.test.failure.ignore=false'
                    }
                }
                post {
                    always {
                        junit "${appDir}/target/surefire-reports/*.xml"
                        archiveArtifacts artifacts: "${appDir}/target/*.war", fingerprint: true
                    }
                }
            }

            stage('Nexus Publish') {
                steps {
                    dir(appDir) {
                        withCredentials([usernamePassword(credentialsId: 'nexus-credentials',
                                                          usernameVariable: 'NEXUS_USER',
                                                          passwordVariable: 'NEXUS_PASS')]) {
                            sh """
                                mvn deploy:deploy-file \
                                    -DgroupId=com.enterprise \
                                    -DartifactId=${appName} \
                                    -Dversion=${appVersion} \
                                    -Dpackaging=war \
                                    -Dfile=target/ecommerce-api.war \
                                    -DrepositoryId=nexus-releases \
                                    -Durl=${nexusUrl} \
                                    -Dusername=\${NEXUS_USER} \
                                    -Dpassword=\${NEXUS_PASS}
                            """
                        }
                    }
                }
            }

            stage('SonarQube Scan') {
                steps {
                    dir(appDir) {
                        withSonarQubeEnv('SonarQube') {
                            sh """
                                mvn sonar:sonar \
                                    -Dsonar.projectKey=${appName} \
                                    -Dsonar.projectName='${appName}' \
                                    -Dsonar.host.url=${sonarUrl} \
                                    -Dsonar.java.binaries=target/classes
                            """
                        }
                        timeout(time: 10, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }
            }

            stage('Docker Build + Trivy + ECR Push') {
                steps {
                    dir(appDir) {
                        sh "docker build -t ${ecrRepo}:${imageTag} -t ${ecrRepo}:latest ."
                    }
                    sh """
                        trivy image \
                            --exit-code 1 \
                            --severity CRITICAL,HIGH \
                            --no-progress \
                            --format table \
                            ${ecrRepo}:${imageTag}
                    """
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                       credentialsId: 'aws-credentials',
                                       accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                       secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh """
                            aws ecr get-login-password --region ${awsRegion} \
                                | docker login --username AWS --password-stdin ${ecrRegistry}
                            docker push ${ecrRepo}:${imageTag}
                            docker push ${ecrRepo}:latest
                        """
                    }
                }
            }

            stage('Helm Deploy to EKS') {
                steps {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                                       credentialsId: 'aws-credentials',
                                       accessKeyVariable: 'AWS_ACCESS_KEY_ID',
                                       secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                        sh """
                            aws eks update-kubeconfig --region ${awsRegion} --name ${eksCluster}

                            helm upgrade --install ${appName} ./${helmChart} \
                                --namespace ${namespace} \
                                --create-namespace \
                                --set image.repository=${ecrRepo} \
                                --set image.tag=${imageTag} \
                                --set app.version=${appVersion} \
                                --atomic \
                                --timeout 5m \
                                --wait
                        """
                    }
                }
            }
        }

        post {
            success {
                echo "Shared library pipeline: ${appName}:${imageTag} deployed successfully."
            }
            failure {
                echo "Shared library pipeline failed for ${appName}. Review logs."
            }
            always {
                sh "docker rmi ${ecrRepo}:${imageTag} || true"
                cleanWs()
            }
        }
    }
}
