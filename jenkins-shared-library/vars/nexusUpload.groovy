def call() {

    dir('app-monolith') {

        sh '''
            mvn deploy:deploy-file \
            -DgroupId=com.enterprise \
            -DartifactId=ecommerce-api \
            -Dversion=1.0.0 \
            -Dpackaging=war \
            -Dfile=target/ecommerce-api.war \
            -DrepositoryId=nexus-releases \
            -Durl=http://32.193.251.152:8081/repository/maven-releases
        '''

    }

}