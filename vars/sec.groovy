def call(Map config) {
    def service = config.service
    def outputDir = "artifacts/${service}"
    def gitleaksReport = "${outputDir}/gitleaks-report.json"
    def trivyFsReport = "${outputDir}/trivy-fs-report.json"

    withEnv([
        "SERVICE_PATH=services/${service}",
        "OUT_DIR=${outputDir}",
        "GITLEAKS_OUT=${gitleaksReport}",
        "TRIVY_FS_OUT=${trivyFsReport}"
    ]) {
        sh '''
            mkdir -p ${OUT_DIR}

            gitleaks detect \
              --source=${SERVICE_PATH} \
              --report-path=${GITLEAKS_OUT} \
              --report-format=json \
              --no-git \
              --exit-code=1

            trivy fs \
              --scanners vuln,secret,misconfig \
              --format json \
              --output ${TRIVY_FS_OUT} \
              ${SERVICE_PATH}
        '''
    }

    archiveArtifacts artifacts: "${gitleaksReport},${trivyFsReport}", allowEmptyArchive: true
}