# Parametry
VM_NAME="label-owner-service-20251010-081236"
ZONE="europe-central2-c"
IMAGE="europe-central2-docker.pkg.dev/local-axle-425708-t0/project-repository/label-owner-service:latest"
CONTAINER_NAME="label-owner-service-container"

# Zdalny deploy przez SSH do maszyny
gcloud compute ssh "$VM_NAME" --zone="$ZONE" --command="\
  echo '===> Stopping old container (if any)...' && \
  docker stop $CONTAINER_NAME || true && \
  docker rm $CONTAINER_NAME || true && \
  echo '===> Starting new container...' && \
  docker run -d --name $CONTAINER_NAME -p 8080:8080 $IMAGE \
"
