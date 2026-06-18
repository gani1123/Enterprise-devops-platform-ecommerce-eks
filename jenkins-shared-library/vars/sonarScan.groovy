def call() {

    dir('app-monolith') {

        withSonarQubeEnv('sonarqube') {

            sh '''
                mvn sonar:sonar \
                -Dsonar.projectKey=ecommerce-api \
                -Dsonar.projectName="E-commerce API" \
                -Dsonar.host.url=http://32.193.251.152:9000 \
                -Dsonar.java.binaries=target/classes
            '''

        }

    }

    timeout(time: 10, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: true
    }

}