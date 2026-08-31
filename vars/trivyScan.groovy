def call(Map config) {
    def service = config.service
    def registry = 'rajesh00007'
    def tag = env.BUILD_NUMBER
    def cacheDir = "/tmp/trivy-cache-${service}-${env.BUILD_NUMBER}"

    withCredentials([usernamePassword(
        credentialsId: 'dockerhub-creds',
        usernameVariable: 'USER',
        passwordVariable: 'PASS')]) {

        withEnv(["SERVICE=${service}", "REGISTRY=${registry}", "TAG=${tag}", "CACHE_DIR=${cacheDir}"]) {
            try {
                sh '''
                    echo ${PASS} | docker login -u "${USER}" --password-stdin

                    mkdir -p ${CACHE_DIR}
                    cp -r /tmp/trivy-shared-db/db ${CACHE_DIR}/db

                    trivy image --exit-code 1 \
                      --severity CRITICAL \
                      --ignore-unfixed \
                      --skip-db-update \
                      --cache-dir ${CACHE_DIR} \
                      ${REGISTRY}/${SERVICE}:${TAG}
                '''
            } finally {
                sh '''
                    docker logout
                    rm -rf ${CACHE_DIR}
                '''
            }
        }
    }
}