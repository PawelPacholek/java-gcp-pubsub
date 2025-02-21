./gradlew :main-owner-service:run:jib
docker pull gcr.io/local-axle-425708-t0/main-owner-service

./gradlew :label-owner-service:run:jib
docker pull gcr.io/local-axle-425708-t0/label-owner-service
