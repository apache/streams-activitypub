# Use a base image with JRE 17 and Tomcat runtime
FROM tomcat:10.1.18-jre17-temurin

# Copy the war file to the webapps directory in the container
COPY maven/streams-webapp*.war /usr/local/tomcat/webapps/ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Start Tomcat when the container is launched
CMD ["catalina.sh", "run"]
