pipeline {
    agent {
        docker {
            image 'maven:3.9.4-eclipse-temurin-17-alpine'
            args '-v /var/run/docker.sock:/var/run/docker.sock -v $HOME/.m2:/root/.m2'
            reuseNode true
        }
    }

    environment {
        REGISTRY = 'docker.io'
        DOCKER_HUB_USER = 'mapxyz007'
        IMAGE_NAME = "${REGISTRY}/${DOCKER_HUB_USER}/todo-app"
    }

    stages {
        stage('Build and Test') {
            steps {
                sh 'mvn clean install -DskipTests=false'
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

                    docker.withRegistry("https://${env.REGISTRY}", 'DockerHubcred') {
                        def dockerImage = docker.build(IMAGE_NAME_FULL)
                        dockerImage.push()
                        dockerImage.push('latest')
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