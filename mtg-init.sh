#!/bin/zsh
############
# NOTE: source this script! To set the env vars
############

export CLOUDSDK_CONFIG=~/.config/gcloud-personal
# setting explicitly for Terraform
export GOOGLE_APPLICATION_CREDENTIALS="$CLOUDSDK_CONFIG/application_default_credentials.json"
echo "*** Overriding CLOUDSDK_CONFIG to $CLOUDSDK_CONFIG"
gcloud config configurations activate mtg
