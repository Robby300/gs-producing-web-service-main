pipeline {
    agent any

    stages {
//         stage('git') {
//             steps {
//                 git credentialsId: 'github-ssh-key', url: 'git@github.com:Robby300/gs-producing-web-service-main.git'
//             }
//         }
// //         stage('build gradle') {
// //             steps {
// //                 sh './gradlew build'
// //             }
// //         }
//         stage ('build docker') {
//             steps {
//                 sh 'docker build -t robby300/jenkins-images:0.3 .'
//             }
//         }
         stage('push to DockerHub') {
            steps{
               withDockerRegistry(credentialsId: 'dockerhub', url: 'https://index.docker.io/v1/') {
                  sh 'docker push robby300/jenkins-images:0.3'
               }
            }
         }
        stage('Deploy to staging') {
            steps {
                sshagent(['server']) {
                     sh 'ssh -o StrictHostKeyChecking=no robert@192.168.233.128 docker run --network cloud -p 8080:8080 -e PORT=8080 -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres/robdb -e SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS=kafka:29092 -e SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=kafka:29092 -v ${PWD}/logs:/logs -d robby300/jenkins-images:0.3'
                 }
            }
        }
    }
}