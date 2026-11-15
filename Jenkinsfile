pipeline {
    agent any

    tools {
        jdk 'jdk-25'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Format check, build and test') {
            steps {
                dir('PLOMBA/plomba') {
                    // verify includes the Spotless check configured in pom.xml.
                    sh 'sh mvnw -B clean verify'
                }
            }
        }
    }
}
