node {
stage('Preparation') {
    sh("rm -rf ${env.WORKSPACE}/*")
}
stage('Download ansible configs') { 

 git branch: 'build', url: 'https://github.com/ImBeeMaster/demo.git'

}

 stage('Docker build and push to registry') {
 docker.withRegistry('http://registry.local:5000') {
 def DockerImage = docker.build("java-dev")
        DockerImage.push()
  }
 }

   stage('Deploy') {

                ansiblePlaybook(

                    playbook: 'playbook_dev.yml')

//                   inventory: 'hosts.txt',)

   }
}