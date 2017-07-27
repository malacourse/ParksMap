## OSE Setups
oc project adv-dev-hw-prod
oc create configmap parksmap-config --from-file=./parksmap.properties
oc volume dc/nationalparks-blue --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc volume dc/mlbparks-blue --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc volume dc/mlbparks-green --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc volume dc/nationalparks-green --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc policy add-role-to-user view system:serviceaccount:adv-dev-hw-prod:default
