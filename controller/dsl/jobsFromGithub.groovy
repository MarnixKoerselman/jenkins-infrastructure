String githubRepoOwner = 'MarnixKoerselman' // take care that the variable name doesn't clash with a DSL reserved name
organizationFolder(githubRepoOwner) {
    description("This organisation folder contains all repositories in the ${githubRepoOwner} organisation on github :wink:\n\nDepending on the access provided by the credentials \'github-credentials\', you\'ll see only the public repositories - or all.")
    organizations {
        github {
            apiUri('https://api.github.com')
            repoOwner(githubRepoOwner)
            credentialsId('github-credentials')
            traits {
                gitHubBranchDiscovery {
                    strategyId(1)
                }
                gitHubPullRequestDiscovery {
                    strategyId(1)
                }
            }
        }
        orphanedItemStrategy {
            discardOldItems {
            }
        }
    }
    triggers {
        periodicFolderTrigger {
            interval('2h')
        }
    }
}
