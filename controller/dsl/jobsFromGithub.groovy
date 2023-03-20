String githubRepoOwner = 'MarnixKoerselman' // take care that the variable name doesn't clash with a DSL reserved name
organizationFolder(githubRepoOwner) {
    configure { node ->
        // Set child scan triggers to 15 minutes
        node / 'properties' << 'jenkins.branch.OrganizationChildTriggersProperty' {
            templates {
                'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
                    spec('H/4 * * * *')
                    interval('900000')
                }
            }
        }
    }
    description("This organisation folder contains all repositories in the ${githubRepoOwner} organisation on github :wink:\n\nDepending on the access provided by the credentials \'github-credentials\', you\'ll see only the public repositories - or all.")
    organizations {
        github {
            apiUri('https://api.github.com')
            repoOwner(githubRepoOwner)
            credentialsId('github-credentials')
            traits {
                gitHubBranchDiscovery {
                    strategyId(3)
                }
                gitHubPullRequestDiscovery {
                    strategyId(3)
                }
                gitHubExcludeArchivedRepositories()
                gitHubTagDiscovery()
                pruneStaleBranch()
                pruneStaleTag()
                submoduleOption {
                    extension {
                        // Retrieve all submodules recursively (uses '--recursive' option which requires git>=1.6.5)
                        recursiveSubmodules(true)
                    }
                }
                cleanBeforeCheckout {
                    extension { }
                }
                cleanAfterCheckout {
                    extension {
                        // Deletes untracked submodules and any other subdirectories which contain .git directories.
                        deleteUntrackedNestedRepositories(true)
                    }
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
