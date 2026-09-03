def call(Map config) {
    def service = config.service
    def sourcePath = "services/${service}"
    def projectKey = "monorepo-${service}"
    def workDir = ".scannerwork-${service}"
    def scannerHome = tool 'SonarScanner'

    withSonarQubeEnv('SonarQube') {
        withEnv([
            "SERVICE=${service}",
            "SOURCE_PATH=${sourcePath}",
            "PROJECT_KEY=${projectKey}",
            "WORK_DIR=${workDir}",
            "PATH+SONAR=${scannerHome}/bin"
        ]) {
            sh '''
                sonar-scanner \
                  -Dsonar.projectKey=${PROJECT_KEY} \
                  -Dsonar.sources=${SOURCE_PATH} \
                  -Dsonar.projectName=${SERVICE} \
                  -Dsonar.working.directory=${WORK_DIR} \
                  -Dsonar.exclusions=**/node_modules/**,**/vendor/**,**/test/**,**/tests/**,**/.git/**,**/*.spec.js,**/*.test.js,**/*.spec.rb,**/*.test.rb
            '''
        }
    }
}