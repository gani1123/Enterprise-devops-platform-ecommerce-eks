def call() {

    def appDir = "app-monolith"
    def nexusUrl = "http://your-nexus-url"
    def appVersion = "1.0.0"
    def projectKey = "ecommerce-api"

    echo "Uploading WAR to Nexus..."

    dir(appDir) {
        sh """
            curl -v -u admin:admin123 \
            --upload-file target/${projectKey}.war \
            ${nexusUrl}/repository/maven-releases/${projectKey}/${appVersion}/${projectKey}-${appVersion}.war
        """
    }}
