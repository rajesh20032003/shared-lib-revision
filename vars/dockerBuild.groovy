def call(Map config) {
    def service = config.service
    def contextPath = config.contextPath ?: "services/${service}"
    def registry = config.registry ?: 'rajesh00007'
    def platform = config.platform ?: 'linux/amd64'
    def push = config.containsKey('push') ? config.push : true
    def tag = config.tag ?: env.BUILD_NUMBER
    def builderName = "${service}-builder"
    def imageRef = "${registry}/${service}:${tag}"
    def cacheRef = "${registry}/${service}:buildcache"

    withCredentials([usernamePassword(
        credentialsId: 'dockerhub-creds',
        usernameVariable: 'USER',
        passwordVariable: 'PASS')]) {

        withEnv([
            "SERVICE=${service}",
            "IMAGE_REF=${imageRef}",
            "CACHE_REF=${cacheRef}",
            "BUILDER=${builderName}",
            "PLATFORM=${platform}",
            "CONTEXT=${contextPath}"
        ]) {
            try {
                sh '''
                    echo ${PASS} | docker login -u "${USER}" --password-stdin

                    docker buildx create \
                      --name ${BUILDER} \
                      --driver docker-container \
                      --use || docker buildx use ${BUILDER}

                    docker buildx inspect --bootstrap

                    docker buildx build \
                      --builder ${BUILDER} \
                      --platform ${PLATFORM} \
                      --tag ${IMAGE_REF} \
                      --cache-from type=registry,ref=${CACHE_REF} \
                      --cache-to type=registry,ref=${CACHE_REF},mode=max \
                      --push \
                      ${CONTEXT}
                '''
            } finally {
                sh 'docker logout'
            }
        }
    }
}