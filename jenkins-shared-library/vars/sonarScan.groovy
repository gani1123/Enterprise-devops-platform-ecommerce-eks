def call(Map config = [:]) {

    dir(config.appDir) {

        withSonarQubeEnv('sonarqube') {

            sh """
                mvn sonar:sonar \
                    -Dsonar.projectKey=${config.projectKey} \
                    -Dsonar.projectName='${config.projectName}' \
                    -Dsonar.host.url=${config.sonarUrl} \
                    -Dsonar.java.binaries=target/classes
            """

        }
    }

    timeout(time: 10, unit: 'MINUTES') {
        waitForQualityGate abortPipeline: true
    }
}