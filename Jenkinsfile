pipeline {

    parameters { choice(name: 'BUILD_SCOPE', choices: ['develop', 'release'], description: '') }

    agent { label 'openjdk11 && maven && linux' }

    stages {

        stage('Compile & Test') {
            steps {
                sh('mvn -B jacoco:prepare-agent clean test')
            }
        }

        stage('Sonar analysis') {
            when {
                branch 'master'
            }
            steps {
                sh('mvn -B jacoco:report sonar:sonar -DskipTests -Dsonar.host.url=$SONAR_URL')
            }
        }

        stage('Tag and update version') {
            when {
                branch "master"
                environment name: 'BUILD_SCOPE', value: 'release'
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'jenkins-dpn-git', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    setToken()
                    setVersionForRelease()
                    createTag()
                    increaseVersion()
                }
            }
        }

        stage('Build & Deploy docker image') {
            when {
                branch "master"
                environment name: 'BUILD_SCOPE', value: 'release'
            }
            steps {
                echo 'Build & Deploy docker image - not done'
                //TODO
            }
        }

        stage('Run container') {
            when {
                branch "master"
                environment name: 'BUILD_SCOPE', value: 'release'
            }
            steps {
                echo 'Run container - not done'
                //TODO
            }
        }

        stage('Set build name') {
            when {
                branch 'master'
                environment name: 'BUILD_SCOPE', value: 'develop'
            }
            steps {
                script {
                    VERSION = sh(returnStdout: true, script: 'mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout')
                    currentBuild.displayName = VERSION
                }
            }
        }
    }
}

private void setToken() {
    GIT_URL_NO_SCHEMA = GIT_URL.replace("https://", "")
    sh("git remote set-url origin https://${USERNAME}:${PASSWORD}@${GIT_URL_NO_SCHEMA}")
}

private void setVersionForRelease() {
    sh('mvn -B versions:set -DremoveSnapshot')
    VERSION = sh(returnStdout: true, script: 'mvn org.apache.maven.plugins:maven-help-plugin:3.1.0:evaluate -Dexpression=project.version -q -DforceStdout')
    currentBuild.displayName = VERSION
}

private void createTag() {
    sh("git tag -a v${VERSION} -m 'TAG ${VERSION}' && git push origin v${VERSION}")
}

private void increaseVersion() {
    sh "git checkout master"
    sh "mvn -B --batch-mode build-helper:parse-version release:update-versions -DdevelopmentVersion='\${parsedVersion.majorVersion}.\${parsedVersion.nextMinorVersion}'"
    sh "git commit pom.xml -m 'Version updated'"
    sh "git push origin master"
}
