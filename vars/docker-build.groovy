def call(Map config){
  //required 
  def service = config.service 
  def contextPath = config.contextPath ?: "services/${service}"

  //optional 

  def registry = config.registry ?: 'rajesh00007'
  def platform = config.platform ?: 'linux/amd64'
  def tag = config.tag ?: env.BUILD_NUMBER
    def builderName = "${service}-builder"
    def imageRef = "${registry}/${service}:${tag}"
    def cacheRef = "${registry}/${service}:buildcache"

  sh """
  docker buildx create \\
          --name ${builderName} \\
          --driver docker-container \\
          --use || docker buildx use ${builderName}

        docker buildx inspect --bootstrap

     docker buildx build \\
          --builder ${builderName} \\
          --platform ${platform} \\
          --tag ${imageRef} \\
          --cache-from type=registry,ref=${cacheRef} \\
          --cache-to type=registry,ref=${cacheRef},mode=max \\
          --push \\
          ${contextPath}   
  """
}