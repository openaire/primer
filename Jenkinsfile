def dockerImage = "maven:3-jdk-8"
def dockerArgs(env) { return "-e JAVA_TOOL_OPTIONS=-Duser.home=${env.WORKSPACE}"; }

pipeline {
    agent any

    triggers {
	pollSCM('H * * * 1-5')
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
        timeout time: 60, unit: 'MINUTES'
    }

    environment {
	SONAR_TOKEN=credentials('sonar-token')
	ARTIFACTORY_DEPLOY=credentials('artifactory-deploy')
    }
    stages {
        stage('Build') {
            agent {
                docker {
		    image dockerImage
		    reuseNode true
		    args dockerArgs(env)
                }
            }
            steps {
		sh 'mvn -B clean package -Dmaven.test.failure.ignore'
            }
	    post {
		always {
		    recordIssues(tools: [mavenConsole(), java()])
		    junit "**/target/surefire-reports/*.xml"
		    jacoco()
		}
	    }
        }
        stage('Sonar & Deploy') {
            agent {
                docker {
                    image dockerImage
		    reuseNode true
		    args dockerArgs(env)
                }
            }
	    //NOTE: sonar scan is only done for master branch because current Sonar instance does not support branching
	    when { branch 'master' }
            steps {
		sh 'mvn -B sonar:sonar deploy -s deploy-settings.xml -DskipTests -DskipITs -Dsonar.host.url=http://sonar.ceon.pl -Dsonar.login=${SONAR_TOKEN}'
            }
        }
    }

    post {
        unstable {
            emailext (
                subject: "Build unstable in Jenkins: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Unstable job '${env.JOB_NAME}' #${env.BUILD_NUMBER}: check console output at ${env.BUILD_URL}.",
                recipientProviders:  [developers()]
            )
        }

        failure {
            emailext (
                subject: "Build failed in Jenkins: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "Failed job '${env.JOB_NAME}' #${env.BUILD_NUMBER}: check console output at ${env.BUILD_URL}.",
                recipientProviders:  [developers()]
            )
        }
    }
}
