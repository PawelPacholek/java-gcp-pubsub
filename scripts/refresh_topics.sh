gcloud pubsub topics create initialOwner
gcloud pubsub topics create labeledOwner

gcloud pubsub subscriptions create main-owner-service-to-labeledOwner-subscription --topic labeledOwner --ack-deadline=600
gcloud pubsub subscriptions create label-owner-service-to-initialOwner-subscription --topic initialOwner --ack-deadline=600