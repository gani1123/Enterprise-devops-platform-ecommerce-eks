def call() {

    sh "docker rmi ${ECR_REPO}:${IMAGE_TAG} || true"

    cleanWs()

}