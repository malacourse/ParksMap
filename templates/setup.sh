## OSE Setups
#oc new-project adv-dev-hw-test
#oc new-project adv-dev-hw-prod
oc policy add-role-to-user system:image-puller system:serviceaccount:adv-dev-hw-test:default -n adv-dev-hw
oc policy add-role-to-user system:image-puller system:serviceaccount:adv-dev-hw-prod:default -n adv-dev-hw
oc policy add-role-to-user edit system:serviceaccount:jenkins:jenkins -n adv-dev-hw
oc policy add-role-to-user edit system:serviceaccount:jenkins:jenkins -n adv-dev-hw-test
oc policy add-role-to-user edit system:serviceaccount:jenkins:jenkins -n adv-dev-hw-prod
oc project adv-dev-hw
oc create configmap parksmap-config --from-file=./parksmap.properties
oc volume dc/nationalparks --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
oc volume dc/mlbparks --overwrite --add -t configmap --name=db-config --configmap-name=parksmap-config -m /etc/config
##oc label ns adv-dev-hw-prod type=parksmap-backend
