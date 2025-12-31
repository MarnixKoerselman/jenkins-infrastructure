String githubRepoOwner = 'MarnixKoerselman' // take care that the variable name doesn't clash with a JobDSL reserved name
organizationFolder(githubRepoOwner) {
    configure { node ->
        // Set child scan triggers to 60 minutes (`Jenkins-Imposed API Limiter: Current quota for Github API usage has 11 remaining (4 over budget). Next quota of 60 in 49 min. Sleeping until reset.`)
        node / 'properties' << 'jenkins.branch.OrganizationChildTriggersProperty' {
            templates {
                'com.cloudbees.hudson.plugins.folder.computed.PeriodicFolderTrigger' {
                    spec('H * * * *')
                    interval('900000')
                }
            }
        }
    }
    description("This organisation folder contains all public repositories in the ${githubRepoOwner} organisation on github :wink:\n\nFor access to private repositories, provide credentials \'github-credentials\', e.g. a user name and [Personal Access Token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).\n\nGitHub invalidates a PAT as soon as I publish it in this repo, so I cannot showcase the mechanism in its totality.")
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
