#dtrack upload
def config(Map config) {
  def service = config.service 
  def registry = 'rajesh00007'
  def tag = env.BUILD_NUMBER

 sh '''
 dependencyTrackPublisher(
   artifact: "${service}:${tag}.json",
   projectName: "${service}",
   projectVersion: "${tag}",
   synchronous: true
   )
 '''
}