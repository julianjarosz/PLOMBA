pipeline {
    agent any

    tools {
        maven 'maven-3'
        jdk 'jdk-25'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build i testy') {
            steps {
                sh 'mvn -B clean verify'
            }
        }
    }
}