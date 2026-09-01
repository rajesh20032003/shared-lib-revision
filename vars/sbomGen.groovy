def call(Map config) {
  def service = config.service 
  def registry = 'rajesh00007'
  def tag = env.BUILD_NUMBER
  def outputFile = "${service}-${tag}.json"
  withCredentials([usernamePassword(
        credentialsId: 'dockerhub-creds',
        usernameVariable: 'DOCKER_USER',
        passwordVariable: 'DOCKER_PASS')]) {

      withEnv([
        "SERVICE=${service}", 
        "REGISTRY=${registry}", 
        "TAG=${tag}", 
        "OUT=${outputFile}"]) {
            try {
                sh '''
                    printf '%s' "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                    syft ${REGISTRY}/${SERVICE}:${TAG} -o cyclonedx-json=${OUT}
                '''
            } finally {
                sh 'docker logout'
            }
        }
    }

    archiveArtifacts artifacts: outputFile, fingerprint: true

        }
