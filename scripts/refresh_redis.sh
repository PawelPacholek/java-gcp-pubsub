gcloud redis instances create my-redis --size=1 --region=europe-central2 --zone=europe-central2 --redis-version=redis_7_0 --tier=basic
gcloud compute networks vpc-access connectors create my-connector --region=europe-central2 --network=default --range=10.8.0.0/28
gcloud redis instances describe my-redis --region=europe-central2 --format='get(host)'