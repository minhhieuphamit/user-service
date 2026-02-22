pipeline {
    agent any

    environment {
        SERVICE_NAME = 'user-service'
        DOCKER_IMAGE = "ecm/${SERVICE_NAME}"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                dir('user-service') {
                    bat 'gradlew.bat api:bootJar -x test --no-daemon'
                }
            }
        }

        stage('Test') {
            steps {
                dir('user-service') {
                    bat 'gradlew.bat test --no-daemon'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true,
                         testResults: 'user-service/**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Code Coverage') {
            steps {
                dir('user-service') {
                    bat 'gradlew.bat jacocoTestReport --no-daemon'
                }
            }
            post {
                always {
                    jacoco(
                        execPattern: 'user-service/**/build/jacoco/*.exec',
                        classPattern: 'user-service/**/build/classes',
                        sourcePattern: 'user-service/**/src/main/java'
                    )
                }
            }
        }

        stage('Docker Build') {
            steps {
                dir('user-service') {
                    bat "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:latest ."
                }
            }
        }

        stage('Deploy to Staging') {
            when {
                branch 'develop'
            }
            steps {
                script {
                    bat """
                        docker stop ${SERVICE_NAME}-staging || exit 0
                        docker rm ${SERVICE_NAME}-staging || exit 0
                    """
                    withCredentials([
                        string(credentialsId: 'DB_URL', variable: 'DB_URL'),
                        string(credentialsId: 'DB_USERNAME', variable: 'DB_USERNAME'),
                        string(credentialsId: 'DB_PASSWORD', variable: 'DB_PASSWORD')
                    ]) {
                        bat """
                            docker run -d ^
                                --name ${SERVICE_NAME}-staging ^
                                -p 8081:8081 ^
                                -e DB_URL=%DB_URL% ^
                                -e DB_USERNAME=%DB_USERNAME% ^
                                -e DB_PASSWORD=%DB_PASSWORD% ^
                                -e SERVER_PORT=8081 ^
                                ${DOCKER_IMAGE}:${DOCKER_TAG}
                        """
                    }
                }
            }
        }

        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                input message: 'Deploy to Production?', ok: 'Deploy'
                script {
                    bat """
                        docker stop ${SERVICE_NAME}-prod || exit 0
                        docker rm ${SERVICE_NAME}-prod || exit 0
                    """
                    withCredentials([
                        string(credentialsId: 'DB_URL_PROD', variable: 'DB_URL'),
                        string(credentialsId: 'DB_USERNAME_PROD', variable: 'DB_USERNAME'),
                        string(credentialsId: 'DB_PASSWORD_PROD', variable: 'DB_PASSWORD')
                    ]) {
                        bat """
                            docker run -d ^
                                --name ${SERVICE_NAME}-prod ^
                                -p 8081:8081 ^
                                -e DB_URL=%DB_URL% ^
                                -e DB_USERNAME=%DB_USERNAME% ^
                                -e DB_PASSWORD=%DB_PASSWORD% ^
                                -e SERVER_PORT=8081 ^
                                ${DOCKER_IMAGE}:${DOCKER_TAG}
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully for ${SERVICE_NAME}!"
        }
        failure {
            echo "Pipeline FAILED for ${SERVICE_NAME}!"
        }
        always {
            cleanWs()
        }
    }
}
