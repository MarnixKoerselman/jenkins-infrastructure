// jobDslRunner.groovy

pipelineJob('docker test') {
    definition {
        cps {
            sandbox(true)
            script("""
pipeline {
    agent {
        node 'built-in'
    }
    stages {
        stage('docker info') {
            steps {
                sh 'docker info'
            }
        }
    }
    post {
        cleanup {
            cleanWs()
        }
    }
}
""")
        }
    }
}
