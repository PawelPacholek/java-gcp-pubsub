SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

$SCRIPT_DIR/../gradlew :main-owner-service:run:jib
docker pull europe-central2-docker.pkg.dev/local-axle-425708-t0/project-repository/main-owner-service

$SCRIPT_DIR/../gradlew :label-owner-service:run:jib
docker pull europe-central2-docker.pkg.dev/local-axle-425708-t0/project-repository/label-owner-service
