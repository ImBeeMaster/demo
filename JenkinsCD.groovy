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
              //  sh("cat /etc/ansible/ansible.cfg")
                sh("echo /id_rsa > ~/.ssh/id_rsa")
                //sh("chown jenkins:jenkins ~/.ssh/id_rsa")
                sh("chmod 700 ~/.ssh/id_rsa")
                ansiblePlaybook(
                    playbook: 'deploy.yml',
                    extras: "-e Env='${Env_name}' -vvvv",
                    inventory: "./hosts" )
   }
}