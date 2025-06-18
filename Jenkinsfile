pipeline {
    agent any

    environment {
        BACKEND_IMAGE = "matiasjara1901244/proyectogps-backend:latest"
        FRONTEND_IMAGE = "matiasjara1901244/proyectogps-frontend:latest"
    }

    stages {
        stage('Pull imágenes Docker') {
            steps {
                echo 'Descargando imágenes actualizadas de Docker Hub...'
                sh "docker pull $BACKEND_IMAGE"
                sh "docker pull $FRONTEND_IMAGE"
            }
        }

        stage('Actualizar contenedores') {
            steps {
                echo 'Recreando contenedores con docker-compose...'

                // Backend
                dir('/home/matiasjara1901/Proyecto-GPS') {
                    sh 'docker compose down'
                    sh 'docker compose up -d'
                }

                // Frontend
                dir('/home/matiasjara1901/Proyecto-GPS-Front') {
                    sh 'docker compose down'
                    sh 'docker compose up -d'
                }
            }
        }
    }
}