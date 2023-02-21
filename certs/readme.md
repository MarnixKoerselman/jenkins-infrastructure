# Root CA and derived website certificates, for local/private SSL

https://www.linkedin.com/pulse/how-create-your-own-self-signed-root-certificate-shankar-gomare/

## Certificate Authority

(if you want a non password protected key remove -des3 option)
openssl genrsa -des3 -out myCA.key 2048
passphrase: 1234567890

openssl req -x509 -new -nodes -key myCA.key -sha256 -days 20000 -out myCA.pem

## Website

openssl genrsa -out jenkins.moria.local.key 2048

openssl req -new -key jenkins.moria.local.key -out jenkins.moria.local.csr
A challenge password []:1234567890
An optional company name []:

openssl x509 -req -in jenkins.moria.local.csr -CA myCA.pem -CAkey myCA.key -CAcreateserial -out jenkins.moria.local.crt -days 20000 -sha256 -extfile jenkins.moria.local.ext
