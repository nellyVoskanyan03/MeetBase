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
minikube image load --overwrite=true auth-ms.tar
minikube image load --overwrite=true meet-ms.tar
minikube image load --overwrite=true notification-ms.tar
minikube image load --overwrite=true gateway-ms.tar

echo "🧹 Cleaning up local TAR files..."
rm *-ms.tar

echo "🔐 Applying ConfigMaps and Secrets..."
kubectl apply -f config.yaml

echo "🗄️ Applying Databases..."
kubectl apply -f postgres.yaml

echo "☕ Applying Kafka..."
kubectl apply -f kafka/00-namespace.yaml
kubectl apply -f kafka/01-configmap.yaml
kubectl apply -f kafka/02-headless-service.yaml
kubectl apply -f kafka/03-service.yaml
kubectl apply -f kafka/04-statefulset.yaml

echo "🖥️ Applying Kafka UI..."
kubectl apply -f kafka-ui.yaml

echo "⏳ Waiting for databases to be ready..."
kubectl wait --for=condition=ready pod -l app=postgres --timeout=120s
kubectl wait --for=condition=ready pod -l app=auth-postgres --timeout=120s

echo "⏳ Waiting for Kafka to be ready..."
kubectl wait --for=condition=ready pod -l app=kafka -n kafka --timeout=180s

echo "📋 Pre-creating Kafka internal and application topics..."
kubectl exec -n kafka kafka-0 -- \
  /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server kafka.kafka.svc.cluster.local:9092 \
  --create --topic __consumer_offsets \
  --partitions 50 --replication-factor 1 \
  --config cleanup.policy=compact \
  --if-not-exists

kubectl exec -n kafka kafka-0 -- \
  /opt/kafka/bin/kafka-topics.sh \
  --bootstrap-server kafka.kafka.svc.cluster.local:9092 \
  --create --topic meet-notifications-topic \
  --partitions 1 --replication-factor 1 \
  --if-not-exists

echo "🚀 Applying Spring Boot Microservices..."
kubectl apply -f auth-ms.yaml
kubectl apply -f meet-ms.yaml
kubectl apply -f notification-ms.yaml

echo "⏳ Waiting for microservices to be ready..."
kubectl wait --for=condition=ready pod -l app=auth-ms --timeout=180s
kubectl wait --for=condition=ready pod -l app=meet-ms --timeout=180s
kubectl wait --for=condition=ready pod -l app=notification-ms --timeout=180s

echo "🚪 Applying API Gateway..."
kubectl apply -f gateway-ms.yaml

echo "⏳ Waiting for Gateway to be ready..."
kubectl wait --for=condition=ready pod -l app=gateway-ms --timeout=120s

echo "✅ Deployment Complete!"
kubectl get pods,svc
kubectl get pods,svc -n kafka

echo ""
echo "🌐 Gateway:  $(minikube service gateway-ms-service --url)"
echo "📊 Kafka UI: $(minikube service kafka-ui --url)"