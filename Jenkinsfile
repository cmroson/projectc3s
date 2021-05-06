pipeline{
    agent any
    environment {
        NAME = 'helloworld'
        kubeConfigPath = "/home/config"
        registry = "codejacksparrow/helloworld"
        registryCredential = 'docker'
        dockerImage = ''
        kubectlPath = '/usr/local/bin/kubectl'
    }
    tools {
        maven 'maven'
        jdk 'jdk8'
    }
    stages {
        stage('build app'){
            steps {
                sh'mvn clean install'
            }
        }
        stage('docker build'){
            steps {
                script {
                    dockerImage = docker.build registry + ":$BUILD_NUMBER"
                }
            }
        }
	    stage('dockerhub deploy'){
            steps {
                script {
                    docker.withRegistry('', registryCredential) {
                    dockerImage.push('latest')
                    }
                }
            }
        }
        stage('deploy to Kubernetes'){
			steps {
				withAWS(credentials: 'AWS_Credentials', region: 'us-east-1') {
					sh "${kubectlPath} --kubeconfig=${kubeConfigPath} rollout restart deployment/helloworld-deployment"
					sh "${kubectlPath} --kubeconfig=${kubeConfigPath} apply -f deployment.yaml"
				}
			}
		}
        stage('remove unused images') {
            steps {
                sh "docker rmi $registry:$BUILD_NUMBER"
                sh "docker images -q |xargs docker rmi"
            }
        }    
    }
}
