def call(Map config = [:]) {

    sh """
        docker rmi ${config.ecrRepo}:${config.imageTag} || true
    """

    cleanWs()
}