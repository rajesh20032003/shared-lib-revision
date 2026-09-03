// vars/sonarQualityGate.groovy
def call() {
    timeout(time: 5, unit: 'MINUTES') {
        def qg = waitForQualityGate()
        if (qg.status != 'OK') {
            error "SonarQube Quality Gate failed: ${qg.status}"
        }
    }
}