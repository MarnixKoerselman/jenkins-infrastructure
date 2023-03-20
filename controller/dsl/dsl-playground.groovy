freeStyleJob('dsl-playground') {
    displayName('Job DSL playground')
    description('Environment for experimentation/development of JobDSL job definitions.\n\nTypically, these definitions should be copied into groovy files, to be executed on Jenkins startup through the \'jobs\' section in jenkins.yaml.')
    steps {
        jobDsl {
            sandbox(true)
            useScriptText(true)
            scriptText('job(\'test\') {\n\tsteps {\n\t\t\n\t}\n}\n')
        }
    }
}
