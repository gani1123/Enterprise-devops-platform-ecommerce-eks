def call() {

    def awsRegion = "us-east-1"
    def accountId = env.AWS_ACCOUNT_ID
    def imageName = "ecommerce-api"
    def imageTag = "1.0.0-${env.BUILD_NUMBER}"

    def registry = "${accountId}.dkr.ecr.${awsRegion}.amazonaws.com"

    echo "✅ Using IAM Role authentication..."

    sh """
        aws sts get-caller-identity

        echo "🔐 Logging into ECR..."
        aws ecr get-login-password --region ${awsRegion} | \
        docker login --username AWS --password-stdin ${registry}

        echo "🚀 Pushing images..."
        docker push ${registry}/${imageName}:${imageTag}

        docker tag ${registry}/${imageName}:${imageTag} ${registry}/${imageName}:latest

        docker push ${registry}/${imageName}:latest
    """
}

