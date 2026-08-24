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

        stage('Build Docker Image') {

                    when {
                        branch 'main'
                    }

                    steps {
                        bat 'docker build -t ecommerce-order-service:latest ./order-service'
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