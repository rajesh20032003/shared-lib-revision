def call(Map config) {
    def service = config.service
    def tag = config.tag  // pass this in from the caller, since you're already computing it there
    def gitopsRepo = config.gitopsRepo ?: 'https://github.com/rajesh20032003/revision.git'
    def gitopsDir = "helm-"

    withCredentials([usernamePassword(
        credentialsId: 'github-mb',
        usernameVariable: 'GIT_USER',
        passwordVariable: 'GIT_TOKEN')]) {

        withEnv([
            "SERVICE=${service}",
            "TAG=${tag}",
            "GITOPS_DIR=${gitopsDir}",
            "GITOPS_REPO=${gitopsRepo}"
        ]) {
            sh '''
                rm -rf ${GITOPS_DIR}
                git clone https://${GIT_USER}:${GIT_TOKEN}@$(echo ${GITOPS_REPO} | sed 's#https://##') ${GITOPS_DIR}
                cd ${GITOPS_DIR}

                yq -i "(.images[] | select(.name == \"${SERVICE}\") | .tag) = \"${TAG}\"" values.yaml

                git config user.email "jenkins-ci@yourdomain.com"
                git config user.name "jenkins-ci"
                git add values.yaml
                git commit -m "chore: update ${SERVICE} to ${TAG}" || echo "No changes to commit"
                git push origin main
            '''
        }
    }
}