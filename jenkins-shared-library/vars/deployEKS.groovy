deployEKS(
    awsRegion: AWS_REGION,
    awsCredentialsId: 'AWS-cred',
    eksCluster: EKS_CLUSTER,
    namespace: K8S_NAMESPACE,
    helmChart: HELM_CHART,
    appName: APP_NAME,
    appVersion: APP_VERSION,
    ecrRepo: ECR_REPO,
    imageTag: IMAGE_TAG
)