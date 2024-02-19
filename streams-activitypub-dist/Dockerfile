FROM tomcat:jre17-temurin
MAINTAINER dev@streams.apache.org
LABEL Description="apache-streams-activitypub-dist"
WORKDIR /
RUN mkdir -p /etc/fuseki
ADD target/lib/* /usr/local/tomcat/lib/
ADD target/webapps/streams-activitypub.war /usr/local/tomcat/webapps/ROOT.war
ADD target/webapps/jena-fuseki.war /usr/local/tomcat/webapps/fuseki.war
