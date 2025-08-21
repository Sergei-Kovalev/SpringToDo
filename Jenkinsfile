pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile'
            dir '.'
            args '-v /var/run/docker.sock:/var/run/docker.sock -u root'
        }
    }

    environment {
        REGISTRY = 'docker.io'
        DOCKER_HUB_USER = 'mapxyz007'
        IMAGE_NAME = "${REGISTRY}/${DOCKER_HUB_USER}/todo-app"
        IMAGE_TAG = env.BRANCH_NAME == 'master' ? "latest-${env.BUILD_NUMBER}" : "pr-${env.CHANGE_ID}"
    }

    stages {
        stage('Build and Test') {
            when {
                not { branch 'master' }
            }
            steps {
                sh 'mvn clean install -DskipTests=false'
            }
        }

        stage('Build & Push Docker Image') {
            when {
                branch 'master'
            }
            steps {
                script {
                    docker.withRegistry("https://${REGISTRY}", 'Docker Hub cred') {
                        def dockerImage = docker.build("${IMAGE_NAME}:${IMAGE_TAG}")
                        dockerImage.push()
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