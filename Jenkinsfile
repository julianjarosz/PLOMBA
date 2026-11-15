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
        stage('Build and test') {
            steps {
                dir('PLOMBA/plomba') {
                    sh 'mvn -B clean verify'
                }
            }
        }
    }
}