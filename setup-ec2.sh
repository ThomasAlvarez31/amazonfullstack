#!/bin/bash
set -e

ECR="635498947387.dkr.ecr.us-east-1.amazonaws.com"
APP1_HOST="10.0.2.176"
APP2_HOST="10.0.4.96"
DB_HOST="10.0.3.59"
WEB_IP="44.203.20.153"
WEB_ID="i-02c46952889c1a397"
APP1_ID="i-0f990ee3b52621b24"
APP2_ID="i-051adb1dbea199c8e"
DATA_ID="i-07defa07c955394c6"
MYSQL_PASS="rootpass"
REGION="us-east-1"
AZ="us-east-1a"
BRANCH="feature/devops-evaluacion-2"

# Genera key temporal y la mantiene durante el script
ssh-keygen -t rsa -f /tmp/eickey -N '' -q 2>/dev/null || true

connect() {
  local INSTANCE_ID=$1
  local HOST=$2
  local CMD=$3
  aws ec2-instance-connect send-ssh-public-key \
    --instance-id $INSTANCE_ID \
    --availability-zone $AZ \
    --instance-os-user ec2-user \
    --ssh-public-key file:///tmp/eickey.pub > /dev/null
  ssh -i /tmp/eickey -o StrictHostKeyChecking=no ec2-user@$HOST "$CMD"
}

connect_jump() {
  local INSTANCE_ID=$1
  local PRIV_IP=$2
  local CMD=$3
  aws ec2-instance-connect send-ssh-public-key \
    --instance-id $WEB_ID \
    --availability-zone $AZ \
    --instance-os-user ec2-user \
    --ssh-public-key file:///tmp/eickey.pub > /dev/null
  aws ec2-instance-connect send-ssh-public-key \
    --instance-id $INSTANCE_ID \
    --availability-zone $AZ \
    --instance-os-user ec2-user \
    --ssh-public-key file:///tmp/eickey.pub > /dev/null
  ssh -i /tmp/eickey -o StrictHostKeyChecking=no \
    -J ec2-user@$WEB_IP ec2-user@$PRIV_IP "$CMD"
}

SETUP_CMD="
set -e
cd ~
[ ! -d amazonfullstack ] && git clone https://github.com/ThomasAlvarez31/amazonfullstack.git
cd amazonfullstack
git fetch origin && git checkout $BRANCH && git pull origin $BRANCH
"

echo "=== Setup ec2-data ==="
connect_jump $DATA_ID $DB_HOST "$SETUP_CMD; echo MYSQL_ROOT_PASSWORD=$MYSQL_PASS > .env; docker compose -f docker-compose.data.yml --env-file .env up -d; echo DATA OK"

echo "=== Setup ec2-app-1 ==="
connect_jump $APP1_ID $APP1_HOST "$SETUP_CMD; printf 'ECR=$ECR\nMYSQL_ROOT_PASSWORD=$MYSQL_PASS\nDB_HOST=$DB_HOST\n' > .env; aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR; echo APP1 OK"

echo "=== Setup ec2-app-2 ==="
connect_jump $APP2_ID $APP2_HOST "$SETUP_CMD; printf 'ECR=$ECR\nMYSQL_ROOT_PASSWORD=$MYSQL_PASS\nDB_HOST=$DB_HOST\n' > .env; aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR; echo APP2 OK"

echo "=== Setup ec2-web ==="
connect $WEB_ID $WEB_IP "$SETUP_CMD; printf 'ECR=$ECR\nAPP1_HOST=$APP1_HOST\nAPP2_HOST=$APP2_HOST\nMYSQL_ROOT_PASSWORD=$MYSQL_PASS\n' > .env; aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR; echo WEB OK"

echo "=== Todas las EC2 configuradas ==="
