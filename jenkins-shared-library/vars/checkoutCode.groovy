def call() {

    git branch: 'main',
        credentialsId: 'github-cred',
        url: 'https://github.com/gani1123/Enterprise-devops-platform-ecommerce-eks.git'

    sh 'echo "Commit: $(git rev-parse --short HEAD)"'
}