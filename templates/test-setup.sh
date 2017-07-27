## OSE Setups
oc project adv-dev-hw-test
oc create configmap parksmap-config --from-file=./parksmap.properties 
oc volume dc/nationalparks --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc volume dc/mlbparks --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc policy add-role-to-user view system:serviceaccount:adv-dev-hw-test:default
