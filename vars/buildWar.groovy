def call() {

    def appDir = "app-monolith"   // ✅ define here instead of passing

    echo "Building WAR from ${appDir}..."

    dir(appDir) {
        sh 'mvn clean package -DskipTests=false -Dmaven.test.failure.ignore=false'
    }

    junit "${appDir}/target/surefire-reports/*.xml"

    archiveArtifacts artifacts: "${appDir}/target/*.war", fingerprint: true
}
