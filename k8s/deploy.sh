#!/bin/bash
set -e

echo "🚀 Starting Minikube with Podman..."
minikube start --driver=podman

echo "🏗️ Building Microservice Images (from parent directory)..."
podman build -t auth-ms:latest ../auth_ms
podman build -t meet-ms:latest ../meet_ms
podman build -t notification-ms:latest ../notification_ms
podman build -t gateway-ms:latest ../gateway_ms

echo "📦 Saving Images to TAR (Minikube Podman workaround)..."
podman save -o auth-ms.tar auth-ms:latest
podman save -o meet-ms.tar meet-ms:latest
podman save -o notification-ms.tar notification-ms:latest
podman save -o gateway-ms.tar gateway-ms:latest

echo "📥 Loading TARs into Minikube..."
# Fixed: added --overwrite=true to avoid silent failures on re-deploy
minikube image load --overwrite=true auth-ms.tar
minikube image load --overwrite=true meet-ms.tar
minikube image load --overwrite=true notification-ms.tar
minikube image load --overwrite=true gateway-ms.tar

echo "🧹 Cleaning up local TAR files..."
rm *-ms.tar

echo "🔐 Applying ConfigMaps and Secrets..."
kubectl apply -f config.yaml

echo "🗄️ Applying Databases and Kafka Broker..."
kubectl apply -f postgres.yaml
kubectl apply -f kafka.yaml

echo "⏳ Waiting for databases to be ready before starting Spring Boot apps..."
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s
kubectl wait --for=condition=ready pod -l app=auth-postgres --timeout=120s
kubectl wait --for=condition=ready pod -l app=kafka --timeout=120s

echo "🚀 Applying Spring Boot Microservices..."
kubectl apply -f auth-ms.yaml
kubectl apply -f meet-ms.yaml
kubectl apply -f notification-ms.yaml

# Fixed: wait for microservices to be ready before starting the gateway
echo "⏳ Waiting for microservices to be ready before starting gateway..."
kubectl wait --for=condition=ready pod -l app=auth-ms --timeout=180s
kubectl wait --for=condition=ready pod -l app=meet-ms --timeout=180s
kubectl wait --for=condition=ready pod -l app=notification-ms --timeout=180s

echo "🚪 Applying API Gateway..."
kubectl apply -f gateway-ms.yaml

echo "⏳ Waiting for Gateway to be ready..."
kubectl wait --for=condition=ready pod -l app=gateway-ms --timeout=120s

echo "✅ Deployment Complete! Here is your cluster status:"
kubectl get pods,svc

echo "🌐 Run 'minikube service gateway-ms-service' to open the app in your browser!"