# Parametry
VM_NAME="main-owner-service-20251024-102351"
ZONE="europe-central2-c"
IMAGE="europe-central2-docker.pkg.dev/local-axle-425708-t0/project-repository/main-owner-service:latest"
CONTAINER_NAME="main-owner-service-container"
USER_NAME="pawel_pacholek"

# Zdalny deploy przez SSH do maszyny
gcloud compute ssh "$USER_NAME@$VM_NAME" --zone="$ZONE" --command="\
  echo '===> Stopping old container (if any)...' && \
  docker stop $CONTAINER_NAME || true && \
  docker rm $CONTAINER_NAME || true && \
  echo '===> Fetching new image...' && \
  docker pull $IMAGE && \
  echo '===> Starting new container...' && \
  docker run -d --name $CONTAINER_NAME -e REDIS_HOST=127.0.0.1 -e REDIS_PORT=6379 -p 8080:8080 $IMAGE \
"
