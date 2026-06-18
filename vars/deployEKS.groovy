def call() {

    def awsRegion = env.AWS_REGION ?: "us-east-1"
    def cluster = "enterprise-eks-us-east-1"
    def namespace = "ecommerce"

    echo "🚀 Deploying to EKS..."

    sh """
        aws eks update-kubeconfig --region ${awsRegion} --name ${cluster}

        kubectl get nodes

        kubectl apply -f k8s/deployment.yaml -n ${namespace}
        kubectl apply -f k8s/service.yaml -n ${namespace}
    """
}

