oc project jenkins
oc new-app wkulhanek/sonarqube -e SONARQUBE_JDBC_PASSWORD=password -e SONARQUBE_JDBC_USERNAME=supportdb -e SONARQUBE_JDBC_URL=jdbc:postgresql://supportdb.jenkins.svc:5432/supportdb
oc expose service sonarqube
