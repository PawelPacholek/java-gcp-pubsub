../gradlew :main-owner-service:run:jib
gcloud run deploy main-owner-service --image europe-central2-docker.pkg.dev/local-axle-425708-t0/project-repository/main-owner-service --no-allow-unauthenticated