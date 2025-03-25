FROM ubuntu:14.04
MAINTAINER "Marcos Vidolin <mvidolin@ciandt.com>"

RUN echo "America/Sao_Paulo" > /etc/timezone && dpkg-reconfigure -f noninteractive tzdata

# References
# https://docs.docker.com/reference/builder/
# https://glassfish.java.net/downloads/v2.1.1-final.html

LABEL description="This image is used to start the CI&T PMS application."
LABEL stack="Java 1.6b31 and Glassfish 2.1.1"
LABEL environment="Development"
LABEL since="Aug 25, 2015"
LABEL version="1.0"

WORKDIR /opt/glassfish

ADD bin/jdk-8u202-linux-x64.tar.gz /opt/jdk
ADD bin/glassfish-installer-v2.1.1-b31g-linux.jar /opt/glassfish.jar

# 1- Java instalation & configuration
RUN cd /opt && \
    mkdir -p /usr/lib/jvm && \
    mv jdk/jdk1.8.0_202/ /usr/lib/jvm/ && \
    update-alternatives --install "/usr/bin/java" "java" "/usr/lib/jvm/jdk1.8.0_202/bin/java" 1 && \
    update-alternatives --install "/usr/bin/javac" "javac" "/usr/lib/jvm/jdk1.8.0_202/bin/javac" 1

# 2- Glassfish instalation & configuration
RUN cd /opt && \
  echo A|java -jar glassfish.jar && \
  rm glassfish.jar && \
  cd /opt/glassfish && \
  chmod -R +x lib/ant/bin && \
  sed -i 's/1.6/1.8/g' setup.xml && \
  lib/ant/bin/ant -f setup.xml && \
  chmod a+x bin/asadmin

# 3- Database Driver
ADD libs/ext/ojdbc8-12.2.0.1.jar /opt/glassfish/domains/domain1/lib/ext/ojdbc8-12.2.0.1.jar
ADD libs/ext/hibernate-jpa-2.0-api-1.0.0.Final.jar /opt/glassfish/domains/domain1/lib/ext/hibernate-jpa-2.0-api-1.0.0.Final.jar
ADD libs/ext/orai18n-12.2.0.1.jar /opt/glassfish/domains/domain1/lib/ext/orai18n-12.2.0.1.jar

# 4- Pool & DataSource
ADD config/domain.xml /opt/glassfish/domains/domain1/config/domain.xml

# 5- Configuracoes extras
ADD config/default-web.xml /opt/glassfish/domains/domain1/config/default-web.xml

ADD security/pipedrivecom.crt /usr/lib/jvm/jdk1.8.0_202/jre/lib/security

RUN \
    cd /opt/glassfish/domains/domain1/config && \
    /usr/lib/jvm/jdk1.8.0_202/bin/keytool -import -storepass changeit -noprompt -trustcacerts -alias myalias -file /usr/lib/jvm/jdk1.8.0_202/jre/lib/security/pipedrivecom.crt -keystore cacerts.jks

# 6- Application configurations files
ADD config/pms /opt/glassfish/domains/domain1/lib/applibs/pms

# 7- Extras JARs
ADD libs/lib/commons-modeler-2.0.1.jar /opt/glassfish/lib/commons-modeler-2.0.1.jar
ADD libs/lib/tomcat-ajp.jar /opt/glassfish/lib/tomcat-ajp.jar
ADD libs/lib/appserv-rt_l10n.jar /opt/glassfish/lib/appserv-rt_l10n.jar
ADD libs/lib/webservices-rt_l10n.jar /opt/glassfish/lib/webservices-rt_l10n.jar
ADD libs/lib/webservices-tools_l10n.jar /opt/glassfish/lib/webservices-tools_l10n.jar

# 8- Autodeploy (caution wildcard to skip version and build number)
ADD pms.war /opt/glassfish/domains/domain1/autodeploy/pms.war

# 9- Default ports
# EXPOSE 24848 28009 28080 28181

# 10- Start the container
CMD ["/opt/glassfish/bin/asadmin", "start-domain", "--verbose=true"]

