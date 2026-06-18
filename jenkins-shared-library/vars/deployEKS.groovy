def call() {

    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
        credentialsId: 'AWS-cred',
        accessKeyVariable: 'AWS_ACCESS_KEY_ID',
        secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {

        sh """
            aws eks update-kubeconfig --region ${AWS_REGION} --name ${EKS_CLUSTER}

            helm upgrade --install ${APP_NAME} ./${HELM_CHART} \
                --namespace ${K8S_NAMESPACE} \
                --create-namespace \
                --set image.repository=${ECR_REPO} \
                --set image.tag=${IMAGE_TAG} \
                --set app.version=${APP_VERSION} \
                --atomic \
                --timeout 5m \
                --wait
        """

    }

}