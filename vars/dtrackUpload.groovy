def call(Map config) {
    def service = config.service
    def tag = env.BUILD_NUMBER
    def sbomFile = "artifacts/${service}/sbom.json"

    dependencyTrackPublisher(
        artifact: sbomFile,
        projectName: service,
        projectVersion: tag,
        synchronous: true,
        projectProperties: [
          parentId: 'ae877a2c-578a-450b-a46a-39383eae8c22'
        ]
    )
}