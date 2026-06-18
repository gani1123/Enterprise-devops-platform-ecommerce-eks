def call(Map config = [:]) {

    def awsRegion = config.awsRegion ?: "us-east-1"
    def cluster = config.eksCluster ?: "enterprise-eks-us-east-1"
    def namespace = config.namespace ?: "ecommerce"

    echo "🚀 Deploying to EKS..."

    sh """
        aws eks update-kubeconfig --region ${awsRegion} --name ${cluster}

        kubectl get nodes
        kubectl apply -f k8s/deployment.yaml -n ${namespace}
        kubectl apply -f k8s/service.yaml -n ${namespace}
    """
}

