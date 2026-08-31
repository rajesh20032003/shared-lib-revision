def call (map config) {
  def service = config.service 
  def registry = 'rajesh00007'
  def tag = env.BUILD_NUMBER 

  withCredentials([usernamePassword(
    credentialsId: 'dockerhub-creds', 
    usernameVariable: 'USER', 
    passwordVariable: 'PASS')]) {

          sh """
           echo ${PASS} | docker login -u "${USER}" --password-stdin 

            trivy image --exit-code 1 \
             --severity CRITICAL \
             --ignore-unfixed \
              ${registry}/${service}:${tag}
          """
          }
}
