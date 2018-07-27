node {
    def server = Artifactory.server('Local')
    def downloadSpec = """{
     "files": [
       {
          "pattern": "libs-release-local/demo-${Commit_hash}-${Env_name}-SNAPSHOT.jar",
          "target": "."
        }
     ]
    }""" 
properties([
    parameters([

    string(name: 'Commit_hash', defaultValue: 'NULL', description: '8 numbers of commits hash', ),
    choice(choices: 'Dev\nQA', description: 'What environment choose to deploy', name: 'Env_name')
    
])])



stage('Preparation') {
    sh("rm -rf ${env.WORKSPACE}/*.jar")
    server.download(downloadSpec)
}

stage('Download ansible configs') { 

 git branch: 'deploy', url: 'https://github.com/ImBeeMaster/demo.git'

}

 stage('Docker build and push to registry') {
 sh('ls -la .')
 docker.withRegistry('http://registry.local:5000') {
 def DockerImage = docker.build("java-custom")
        DockerImage.push()
  }
 }

   stage('Deploy') {
                ansiblePlaybook(
                    playbook: 'deploy.yml',
                    extras: "-e Env='${Env_name}'")
   }
}