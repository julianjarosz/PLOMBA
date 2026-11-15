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
        NEXUS_URL = 'http://nexus:8081'
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
                    sh 'mvn -B -s "$WORKSPACE/maven-settings.xml" clean verify'
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'nexus-creds',
                                                  usernameVariable: 'NEXUS_USERNAME',
                                                  passwordVariable: 'NEXUS_PASSWORD')]) {
                    dir("${PROJECT_DIR}") {
                        sh 'mvn -B -s "$WORKSPACE/maven-settings.xml" deploy -DskipTests'
                    }
                }
            }
        }
    }

    post {
        always {
            // "Tests" in Jenkins
            junit allowEmptyResults: true, testResults: "${PROJECT_DIR}/target/surefire-reports/*.xml"

            // coverage report in Jenkins (JaCoCo)
            recordCoverage(
                tools: [[parser: 'JACOCO', pattern: "${PROJECT_DIR}/target/site/jacoco/jacoco.xml"]],
                sourceDirectories: [[path: "${PROJECT_DIR}/src/main/java"]]
            )

            // report from JaCoCo (HTML to download)
            archiveArtifacts artifacts: "${PROJECT_DIR}/target/site/jacoco/**", allowEmptyArchive: true
        }
        success {
            // Built app (.jar) to download
            archiveArtifacts artifacts: "${PROJECT_DIR}/target/*.jar", allowEmptyArchive: true, fingerprint: true
        }
    }
}