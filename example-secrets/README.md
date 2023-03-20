# Secrets

Generate secrets on the deployment host, and store them only there. Regenerate if deployed on a different host. Or backup the secrets, and protect the backup... arguably more work, and less secure.

The provided username/password combos are for demonstration purposes only (duh) and should not be used in a real deployment.

## ssh-agent

SSH keypair that is used to authenticate Jenkins Controller to the Jenkins Agent, when the Controller attempt to connect to the Agent to add it as a Jenkins node.

NB: I've had some problems with ssh key generation, to come up with an acceptable key... maybe something to do with keylength?

```ssh-keygen -N '' -t rsa -b 1024 -f ssh_agent_id_rsa```
