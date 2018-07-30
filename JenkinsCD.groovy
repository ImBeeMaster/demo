node {
    def server = Artifactory.server('Local')
    def downloadSpec = """{
     "files": [
       {
          "pattern": "libs-release-local/demo-${Commit_hash}-${Env_name}-SNAPSHOT.jar",
          "target": "./"
        }
     ]
    }""" 
properties([
    parameters([

    string(name: 'Commit_hash', defaultValue: '40e0c087', description: '8 numbers of commits hash', ),
    choice(choices: 'Dev\nQA', description: 'What environment choose to deploy', name: 'Env_name')
    
])])



stage('Preparation') {
    sh("rm -rf ${env.WORKSPACE}/*.jar")
    server.download(downloadSpec)
    sh("ls -la ${env.WORKSPACE}")
}

stage('Download ansible configs') { 

 git branch: 'deploy', url: 'https://github.com/ImBeeMaster/demo.git'

}

 stage('Docker build and push to registry') {
 sh('ls -la .')
//  docker.withRegistry('http://registry.local:5000') {
 docker.withRegistry('http://localhost:5000') {
 def DockerImage = docker.build("java-custom")
        DockerImage.push()
  }
 }

   stage('Deploy') {
                sh("whoami")
                sh("cat /etc/ansible/ansible.cfg")
                ansiblePlaybook(
                    playbook: 'deploy.yml',
                    extras: "-e Env='${Env_name}' -vv",
                    inventory: "./hosts" )
   }
}