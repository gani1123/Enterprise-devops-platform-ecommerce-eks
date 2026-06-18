def call(Map config = [:]) {


git branch: config.branch,
    credentialsId: config.credentialsId,
    url: config.repoUrl

sh 'echo "Commit: $(git rev-parse --short HEAD)"'


}
