pipeline {
    agent any

    environment {
        REGISTRY = 'docker.io'
        DOCKER_HUB_USER = 'mapxyz007'
        IMAGE_NAME = "${REGISTRY}/${DOCKER_HUB_USER}/todo-app"
    }

    stages {
        stage('Build and Test') {
            steps {
                bat 'mvn clean install -DskipTests=false'
            }
            when {
                anyOf {
                    branch 'master'
                    branch 'dev'
                    changeRequest target: 'master'
                    changeRequest target: 'dev'
                }
            }
        }

        stage('Build & Push Docker Image') {
            when {
                branch 'master'
            }
            steps {
                script {
                    def IMAGE_TAG = "build-${env.BUILD_NUMBER}"
                    def IMAGE_NAME_FULL = "${env.IMAGE_NAME}:${IMAGE_TAG}"

                    bat "docker build -t ${IMAGE_NAME_FULL} ."

                    withCredentials([usernamePassword(
                        credentialsId: 'DockerHubcred',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        bat """
                            echo %DOCKER_PASS% | docker login -u %DOCKER_USER% --password-stdin %REGISTRY%
                            docker push ${IMAGE_NAME_FULL}
                            docker tag ${IMAGE_NAME_FULL} ${env.IMAGE_NAME}:latest
                            docker push ${env.IMAGE_NAME}:latest
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Сборка прошла успешно!"
        }
        failure {
            echo "Сборка провалена!"
        }
    }
}