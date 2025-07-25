set -e
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
$SCRIPT_DIR/../gradlew :main-owner-service:run:jib
$SCRIPT_DIR/deploy.sh