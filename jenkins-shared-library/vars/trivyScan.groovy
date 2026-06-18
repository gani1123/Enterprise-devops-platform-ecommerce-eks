def call(Map config = [:]) {

    sh """
        trivy image \
            --exit-code 0 \
            --severity CRITICAL,HIGH \
            --no-progress \
            --format table \
            ${config.ecrRepo}:${config.imageTag}
    """

}