pipeline {

    agent any

    tools {
        maven 'Maven_3.9.16'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Maven Project') {
            steps {
                bat 'mvn clean install'
            }
        }
    }

    post {

        success {
            echo 'Maven build completed successfully.'
        }

        failure {
            echo 'Maven build failed.'
        }
    }
}