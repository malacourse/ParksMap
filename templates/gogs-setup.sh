#oc new-app wkulhanek/gogs:11.4
#oc expose svc/gogs
#oc volume dc/gogs --add -m /data22 --claim-size=1G --name=gogs-data-vol --overwrite
oc create configmap gogs-config2 --from-file=./app.ini
oc volume dc/gogs --overwrite --add -t configmap --name=gogs-config2 --configmap-name=gogs-config2 -m /opt/gogs/custom/conf

