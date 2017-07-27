oc project jenkins
oc new-app sonatype/nexus3 --name nexus3
oc expose svc/nexus3
oc volume dc/nexus3 --add -m /nexus-data --claim-size=4G --name=nexus-vol --overwrite
