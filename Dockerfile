FROM tomcat:8.5-jre8
RUN rm -fr /usr/local/tomcat/webapps/ROOT
COPY target/mpi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
