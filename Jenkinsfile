pipeline {
  agent any

  environment {
    IMAGE_NAME     = "matiasjara1901244/proyectogps-backend"
    DOCKERHUB_CRED = 'docker-hub-creds'
    SSH_CRED       = 'ssh-prod'
    REMOTE_USER    = 'matiasjara1901'
    REMOTE_HOST    = '190.13.177.173'
    REMOTE_PATH    = '/home/matiasjara1901'    // donde está tu docker-compose.yml
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          sh "docker build -t ${IMAGE_NAME}:latest ."
        }
      }
    }

    stage('Push to Docker Hub') {
      steps {
        script {
          docker.withRegistry('https://registry.hub.docker.com', DOCKERHUB_CRED) {
            sh "docker push ${IMAGE_NAME}:latest"
          }
        }
      }
    }

    stage('Deploy to Production') {
      steps {
        sshagent(credentials: [SSH_CRED]) {
          sh """
            ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} \\
              'docker pull ${IMAGE_NAME}:latest && \\
               cd ${REMOTE_PATH} && \\
               docker-compose up -d'
          """
        }
      }
    }
  }

  post {
    success {
      echo 'Despliegue completado correctamente'
    }
    failure {
      echo 'Hubo un error en el pipeline'
    }
  }
}