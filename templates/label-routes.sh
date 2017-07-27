oc project adv-dev-hw-test
oc label route nationalparks type=parksmap-backend
oc label route mlbparks type=parksmap-backend
oc project adv-dev-hw-prod
oc label route nationalparks-blue-green type=parksmap-backend
oc label route mlbparks-blue-green type=parksmap-backend
oc label route nationalparks-blue type=parksmap-backend
oc label route mlbparks-blue type=parksmap-backend
oc label route nationalparks-green type=parksmap-backend
oc label route mlbparks-green type=parksmap-backend
