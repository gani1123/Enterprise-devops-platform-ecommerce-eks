def call() {

    dir('app-monolith') {

        sh "docker build -t ${ECR_REPO}:${IMAGE_TAG} -t ${ECR_REPO}:latest ."

    }

}