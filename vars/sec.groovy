def call(Map config) {
  sh '''
  gitleaks detect --source=. --report=gitleaks-report.json --report-format=json

  trivy fs --scanners vuln,secret,misconfig .
  '''
}