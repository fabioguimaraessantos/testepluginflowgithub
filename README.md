# PMS - Performance Management System
Copyright (C) 2014 CI&T Software S/A.

## PMS Project.

Requires [Apache Maven](http://maven.apache.org) 3.0 or greater, and JDK 5+ in order to run.

To build, run

    mvn package

Building will run the tests, but to explicitly run tests you can use the test target

    mvn test

## Technical Reference Documentation

JSF 1
[Home](http://docs.oracle.com/cd/E17802_01/j2ee/javaee/javaserverfaces/1.1_01/docs/tlddocs/)

RichFaces 3
[Project](http://showcase-rf3.richfaces.org/)

Spring Framework
[Project](http://projects.spring.io/spring-framework/)

Velocity - Templating Engine
[Project](http://velocity.apache.org/)

## Profiles

To select a specific profile run:

    mvn -Pdev compile

A complete Intellij command line example:
 
    -Pdev compile jetty:stop jetty:run

# Renovar o certificado ssl para a integração com o Pipedrive.
1. Pegar o certificado do servidor do Pipedrive
    1.1 Colocar a url abaixo no Firefox:  http://api.pipedrive.com/v1/organizations?start=0&api_token=1994bfa46fa199b82ffe5762162622a97a04c258
    1.2 Clicar no cadeado verde > Depois em uma flexa indicando mais detalhes > More Information > View Certificate
    1.3 Na aba "General", é possível verificar a validade do certificado. Já crie um evento no calendário com a data de expiração.
    1.4 Na aba "Details", clique em "Export" e salve o arquivo .crt como "pipedrivecom.crt"

2. Mover o arquivo para dentro do container do sistema
    2.1 A build já espera um arquivo com o nome "pipedrivecom.crt" na pasta Docker/security do projeto. Ela coloca o arquivo na diretório "/usr/lib/jvm/jdk1.6.0_31/jre/lib/security".

3. Importar o certificado
    3.1 A build também já faz isso. No caso do PMS é necessário ir até "/opt/glassfish/domains/domain1/config" e executar o comando abaixo. Ele importa o "pipedrivecom.crt" para o arquivo "cacerts.jks".
`/usr/lib/jvm/jdk1.6.0_31/bin/keytool -import -storepass changeit -noprompt -trustcacerts -alias myalias -file /usr/lib/jvm/jdk1.6.0_31/jre/lib/security/pipedrivecom.crt -keystore cacerts.jks`

4. Verificar se importou o certificado com sucesso
    4.1 O comando abaixo tenta achar a string "pipedrive"
`echo 'changeit' | /usr/lib/jvm/jdk1.6.0_31/bin/keytool -list -v -keystore /opt/glassfish/domains/domain1/config/cacerts.jks | grep 'pipedrive'`