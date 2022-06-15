pipeline {
    agent any

    stages {
        stage('git') {
            steps {
                git credentialsId: 'github-ssh-key', url: 'git@github.com:Robby300/gs-producing-web-service-main.git'
            }
        }
        /*stage('build gradle') {
            steps {
                sh './gradlew build'
            }
        }*/
        stage('build docker') {
            steps {
                sh 'docker build -t employee/jenkins-images:0.3 .'
            }
        }
    }
}