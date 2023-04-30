################################
# update cfg_nio.preperties    #
################################
nio.server.ssl.KeyStore=keystore.p12
nio.server.ssl.KeyStorePwd=DEC(admin123)
nio.server.ssl.KeyAlias=demo1.com
nio.server.ssl.KeyPwd=DEC(demo1pwd)


############################
# 1. generate cert and key #
############################
https://www.ssleye.com/ssltool/self_sign.html
generate certificate and private key for demo1 and demo2:
	save demo1 certificate to demo1_cert.pem
	save demo1 private key to demo1_key.pem
	save demo2 certificate to demo2_cert.pem
	save demo2 private key to demo2_key.pem
	save demo3 certificate to demo3_cert.pem
	save demo3 private key to demo3_key.pem

############################
# 2. build cert and key    #
############################
openssl pkcs12 -export -out demo1.p12 -inkey demo1_key.pem -in demo1_cert.pem
>pwd=demo1pwd

openssl pkcs12 -export -out demo2.p12 -inkey demo2_key.pem -in demo2_cert.pem
>pwd=demo2pwd

openssl pkcs12 -export -out demo3.p12 -inkey demo3_key.pem -in demo3_cert.pem
>pwd=demo3pwd


############################
# 3. build key store       #
############################
merge demo1.p12， demo2.p12 and demo3.p12 into keystore.p12 (pwd=admin123)
    keystore.p12 (pwd=admin123)
    |
    |- alias=demo1.com, pwd=demo1pwd
    |- alias=demo2.com, pwd=demo2pwd
    |- alias=demo3.com, pwd=demo3pwd

############################
# 4. build trust store     #
############################
add demo1.com.cer and demo3.com.cer into trustsotre_13.p12
    trustsotre.p12 (pwd=changeit)
    |
    |- alias=demo1.com (summerboot.org)
    |- alias=demo3.com (summerboot.org)
	

add demo2.com.cer and demo3.com.cer into trustsotre_23.p12
    trustsotre.p12 (pwd=changeit)
    |
    |- alias=demo2.com (summerboot.org)
    |- alias=demo3.com (summerboot.org)

