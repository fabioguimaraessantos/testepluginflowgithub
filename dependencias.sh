#!/bin/bash

# Obtenha o valor de QA_ENV
environment=${BITBUCKET_DEPLOYMENT_ENVIRONMENT:-dev}
echo ambiente: "$environment"

cd maven_dependencies
cp -r repository/ ~/.m2/
mvn install:install-file -Dfile=./ojdbc8-12.2.0.1.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=10.2.0.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./richfaces-api-3.3.1.GA.jar -DgroupId=org.richfaces.framework -DartifactId=richfaces-api -Dversion=3.3.1.GA -Dpackaging=jar
mvn install:install-file -Dfile=./richfaces-impl-3.3.1.GA.jar -DgroupId=org.richfaces.framework -DartifactId=richfaces-impl -Dversion=3.3.1.GA -Dpackaging=jar
mvn install:install-file -Dfile=./richfaces-ui-3.3.1.GA.jar -DgroupId=org.richfaces.ui -DartifactId=richfaces-ui -Dversion=3.3.1.GA -Dpackaging=jar
mvn install:install-file -Dfile=./facelets-taglib-0.1.jar -DgroupId=org.springframework.security -DartifactId=facelets-taglib -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=./xmldsig-1.0.jar -DgroupId=javax.xml.crypto -DartifactId=xmldsig -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./xmlsec-2.0.jar -DgroupId=com.sun.org.apache.xml.security -DartifactId=xmlsec -Dversion=2.0 -Dpackaging=jar

mvn install:install-file -Dfile=./maven-war-plugin-2.0.2.jar -DgroupId=org.apache.maven.plugins -DartifactId=maven -Dversion=2.0.2 -Dpackaging=jar

#mvn install:install-file -Dfile=./micrometer-core-1.7.12.jar -DgroupId=io.micrometer -DartifactId=micrometer-core -Dversion=1.7.12 -Dpackaging=jar

mvn install:install-file -Dfile=./maven-install-plugin-2.4.jar -DgroupId=org.apache.maven.plugins -DartifactId=maven -Dversion=2.4 -Dpackaging=jar
cd ..

echo "Starting to configure domain.xml and config.properties"

if [ "${environment}" == "prd" ]; then
    sed -i 's@${LDAP}@ldap://ldapcloudaws.ciandt.global:389/OU=CIANDT.GLOBAL,DC=ciandt,DC=global@g' src/main/webapp/WEB-INF/conf/spring-security.xml
else
    sed -i 's@${LDAP}@ldap://172.16.22.110:389/OU=CIANDT.GLOBAL,DC=ciandt,DC=global@g' src/main/webapp/WEB-INF/conf/spring-security.xml
fi

cp docker/config/template_domain.xml docker/config/template_domain_bkp.xml
cp docker/config/template_domain.xml docker/config/domain.xml

rm src/main/config/pms/config.properties

echo "Copying src/main/resources/env/"${environment}".config.properties to default"
cp src/main/resources/env/"${environment}".config.properties src/main/config/pms/config.properties

set +e
mkdir -p docker/config/pms
set -e

cp src/main/config/pms/* docker/config/pms/

source docker/config/environment/env.${environment}
