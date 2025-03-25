mvn install:install-file -Dfile=./ojdbc8-12.2.0.1.jar -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=10.2.0.1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./richfaces-api-3.3.1.GA.jar -DgroupId=org.richfaces.framework -DartifactId=richfaces-api -Dversion=3.3.1.GA -Dpackaging=jar
mvn install:install-file -Dfile=./richfaces-impl-3.3.1.GA.jar -DgroupId=org.richfaces.framework -DartifactId=richfaces-impl -Dversion=3.3.1.GA -Dpackaging=jar
mvn install:install-file -Dfile=./richfaces-ui-3.3.1.GA.jar -DgroupId=org.richfaces.ui -DartifactId=richfaces-ui -Dversion=3.3.1.GA -Dpackaging=jar
mvn install:install-file -Dfile=./facelets-taglib-0.1.jar -DgroupId=org.springframework.security -DartifactId=facelets-taglib -Dversion=0.1 -Dpackaging=jar
mvn install:install-file -Dfile=./xmldsig-1.0.jar -DgroupId=javax.xml.crypto -DartifactId=xmldsig -Dversion=1.0 -Dpackaging=jar
mvn install:install-file -Dfile=./xmlsec-2.0.jar -DgroupId=com.sun.org.apache.xml.security -DartifactId=xmlsec -Dversion=2.0 -Dpackaging=jar
