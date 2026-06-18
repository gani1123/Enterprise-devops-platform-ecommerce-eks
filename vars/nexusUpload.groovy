def call(Map config = [:]) {

    dir(config.appDir) {

        sh """
            mvn deploy:deploy-file \
                -DgroupId=com.enterprise \
                -DartifactId=${config.projectKey} \
                -Dversion=${config.appVersion} \
                -Dpackaging=war \
                -Dfile=target/${config.projectKey}.war \
                -DrepositoryId=nexus-releases \
                -Durl=${config.nexusUrl}
        """
    }
}