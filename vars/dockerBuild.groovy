def call(Map config = [:]) {

    dir(config.appDir) {

        sh """
            docker build \
                -t ${config.ecrRepo}:${config.imageTag} \
                -t ${config.ecrRepo}:latest .
        """

    }
}