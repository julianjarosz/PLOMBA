pipeline {
    agent any

    options {
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    tools {
        maven 'maven-3'
        jdk 'jdk-25'
    }

    environment {
        PROJECT_DIR = 'PLOMBA/plomba'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build and test') {
            steps {
                dir("${PROJECT_DIR}") {
                    sh 'mvn -B clean verify'
                }
            }
        }
    }

    post {
        always {
            // "Tests" in Jenkins
            junit allowEmptyResults: true, testResults: "${PROJECT_DIR}/target/surefire-reports/*.xml"

            // Report from JaCoCo
            archiveArtifacts artifacts: "${PROJECT_DIR}/target/site/jacoco/**", allowEmptyArchive: true
        }
        success {
            // build app (.jar) to download
                archiveArtifacts artifacts: "${PROJECT_DIR}/target/*.jar", allowEmptyArchive: true, fingerprint: true
        }
    }
}