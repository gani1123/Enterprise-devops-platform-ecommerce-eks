def call() {

    sh """
        trivy image \
        --exit-code 0 \
        --severity CRITICAL,HIGH \
        --no-progress \
        --format table \
        ${ECR_REPO}:${IMAGE_TAG}
    """

}