// println 'all files:'
new File('/usr/share/jenkins/dsl').eachFile { file ->
    // println file
    if (file.name.endsWith('.jenkinsfile')) {
        String pipelineName = file.name.split('\\.')[0]
        pipelineText = java.nio.file.Files.readString(java.nio.file.Path.of(file.path))
        generatePipelineJob(pipelineName, pipelineText)
    }
}

def generatePipelineJob(String pipelineName, String pipelineScript) {
    pipelineJob(pipelineName) {
        definition {
            cps {
                script(pipelineScript)
                sandbox()
            }
        }
    }
}
