# ingress for local k8s
    install ingress-nginx
    https://kubernetes.github.io/ingress-nginx/deploy/#quick-start

# deployment = "deployment stpes-deploy"
# add local storage class: 

[//]: # ( K3s when installed, also downloads a storage class which makes it as default)
[//]: # ( K8s cluster on the other hand, does not download also a default storage class)
    kubectl apply -f local-path-storage.yaml
    kubectl patch storageclass local-path -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
# add name space     
    kubectl apply -f namespace.yaml
# restaurant development
    kubectl apply -f postgres-restaurant-configmap.yaml
    kubectl apply -f postgres-retaurant-credentials.yaml
    kubectl apply -f postgres-restaurant-deployment.yaml
    kubectl apply -f restaurant-deployment.yaml
    kubectl apply -f ingress.yaml

[//]: # (    kubectl apply -f local-path-storage2.yaml)
[//]: # (    kubectl patch storageclass local-path2 -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}')
# message development
    kubectl apply -f service-message-deployment.yaml
    kubectl apply -f sms-ingress.yaml

# order development
    kubectl apply -f postgres-orders-configmap.yaml
    kubectl apply -f postgres-orders-credentials.yaml
    kubectl apply -f postgres-orders-deployment.yaml
    kubectl apply -f service-orders-deployment.yaml
    kubectl apply -f or-ingress.yaml
# customer development
    kubectl apply -f postgres-customer-configmap.yaml
    kubectl apply -f postgres-customer-credentials.yaml
    kubectl apply -f postgres-customer-deployment.yaml
    kubectl apply -f service-customers-deployment.yaml
    kubectl apply -f cust-ingress.yaml
# delivery development
    kubectl apply -f postgres-delivery-configmap.yaml
    kubectl apply -f postgres-delivery-credentials.yaml
    kubectl apply -f postgres-delivery-deployment.yaml
    kubectl apply -f delivery-deployment.yaml
    kubectl apply -f delv-ingress.yaml
# help
    kubectl get pod --namespace=itep-g3
    kubectl get namespace itep-g3
    kubectl describe pod restaurant-db-0 --namespace=itep-g3

[//]: # (create ingress by terminal)
[//]: # (    kubectl expose deployment restaurant-service-k8s --namespace=itep-g3)
[//]: # (    kubectl create ingress restaurant-ingress --namespace=itep-g3 --class=nginx --rule="restaurant.localdev.me/*=restaurant-service-k8s:8082")

# port forwarding example postgres
    kubectl port-forward svc/restaurant-service-k8s 22622:8086
    kubectl port-forward svc/order-service-k8s 33633:8080
    kubectl port-forward svc/customer-service-k8s 33933:8069
    kubectl port-forward svc/order-service-k8s 22922:9090