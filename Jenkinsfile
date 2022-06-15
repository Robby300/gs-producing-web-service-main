pipeline {
    agent any

    stages {
        stage ('Build & Push'){
            steps ('git') {
                git credentialsId: 'github-ssh-key', url: 'git@github.com:Robby300/gs-producing-web-service-main.git'
            }

            steps ('build docker') {
                sh 'docker build -t robby300/jenkins-images:0.3 .'
            }
            steps ('push to DockerHub') {
               withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                  sh '''
                    docker push robby300/jenkins-images:0.3
                  '''
               }
            }
        }

        stage ('Pull & Run'){
            steps ('build') {
                sh 'echo HELLO'
            }
        }

    }
}