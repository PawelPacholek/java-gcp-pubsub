SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

$SCRIPT_DIR/../main-owner-service/deploy.sh
$SCRIPT_DIR/../label-owner-service/deploy.sh
