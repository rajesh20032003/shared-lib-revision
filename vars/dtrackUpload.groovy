def call(Map config) {
    def service = config.service
    def tag = env.BUILD_NUMBER
    def sbomFile = "artifacts/${service}/sbom.json"

    dependencyTrackPublisher(
        artifact: sbomFile,
        projectName: service,
        projectVersion: tag,
        synchronous: true
    )
}