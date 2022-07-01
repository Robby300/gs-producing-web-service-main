pipeline {
    agent any

    stages {
        stage('git') {
            steps {
                git credentialsId: 'github-ssh-key', url: 'git@github.com:Robby300/gs-producing-web-service-main.git'
            }
        }
        stage ('build containers') {
            steps {
                sh './gradlew jib -t robby300/jenkins-images:0.3 .'
            }
        }
//         }        stage ('build docker') {
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
        stage('stop and remove container') {
            steps {
                sshagent(['server']) {
                     sh 'ssh robert@192.168.233.131 docker rm -f employee'
                }
            }
        }
        stage('deploy to server') {
            steps {
                sshagenrmt(['server']) {
                     sh '''ssh robert@192.168.233.131 \
                     docker run --name employee --network cloud -p 8080:8080 \
                     -e PORT=8080 \
                     -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/robdb \
                     -e SPRING_DATASOURCE_USERNAME=rob  \
                     -e SPRING_DATASOURCE_PASSWORD=isa_3812 \
                     -e SPRING_FLYWAY_URL=jdbc:postgresql://postgres:5432/robdb \
                     -e SPRING_FLYWAY_USER=rob \
                     -e SPRING_FLYWAY_PASSWORD=isa_3812 \
                     -e SPRING_KAFKA_CONSUMER_BOOTSTRAP_SERVERS=kafka:9092 \
                     -e SPRING_KAFKA_PRODUCER_BOOTSTRAP_SERVERS=kafka:9092 \
                     -d robby300/jenkins-images:0.3'''
                 }
            }
        }
    }
}