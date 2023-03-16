# Root CA and derived website certificates, for local/private SSL

## Certificate Authority

(if you want a non password protected key remove -des3 option)

```openssl genrsa -des3 -out myCA.key 2048```

passphrase: 1234567890

```openssl req -x509 -new -nodes -key myCA.key -sha256 -days 20000 -out myCA.cer```

## Website

```openssl genrsa -out wildcard.test.local.key 2048```

```openssl req -new -key wildcard.test.local.key -out wildcard.test.local.csr```

- A challenge password []:1234567890
- An optional company name []:

```openssl x509 -req -in wildcard.test.local.csr -CA myCA.cer -CAkey myCA.key -CAcreateserial -out wildcard.test.local.crt -days 20000 -sha256 -extfile wildcard.test.local.ext```

## References

- <https://deliciousbrains.com/ssl-certificate-authority-for-local-https-development/>
- <https://www.linkedin.com/pulse/how-create-your-own-self-signed-root-certificate-shankar-gomare/>
