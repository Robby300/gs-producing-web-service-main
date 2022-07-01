pipeline {
    agent any

    stages {
        stage('git') {
            steps {
                git credentialsId: 'github-ssh-key', url: 'git@github.com:Robby300/gs-producing-web-service-main.git'
            }
        }
        stage ('build containers and push to DockerHub') {
            steps {
                withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
//                 sh './balance/gradlew jibDockerBuild --image robby300/balance'
                    sh './gradlew jib'
//                 sh './eureka/gradlew jibDockerBuild --image robby300/eureka'
                }
            }
        }

//         stage('stop and remove container') {
//             steps {
//                 sshagent(['server']) {
//                      sh 'ssh robert@192.168.233.131 docker rm -f employee'
//                 }
//             }
//         }
        stage('deploy to server') {
            steps {
                sshagent(['server']) {
                     sh 'ssh robert@192.168.233.131 bash deploy-compose''
                 }
            }
        }
    }
}