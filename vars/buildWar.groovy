def call() {

    dir('app-monolith') {

        sh 'mvn clean package -DskipTests=false -Dmaven.test.failure.ignore=false'

    }

    junit 'app-monolith/target/surefire-reports/*.xml'

    archiveArtifacts artifacts: 'app-monolith/target/*.war', fingerprint: true

}