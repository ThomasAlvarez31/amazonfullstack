#!/bin/bash
set -e

ECR="635498947387.dkr.ecr.us-east-1.amazonaws.com"
APP1_HOST="10.0.2.176"
APP2_HOST="10.0.4.96"
DB_HOST="10.0.3.59"
WEB_HOST="44.203.81.30"
MYSQL_PASS="rootpass"
REGION="us-east-1"
BRANCH="feature/devops-evaluacion-2"

echo "=== Guardando clave PEM ==="
mkdir -p ~/.ssh
cat > ~/.ssh/devops-key.pem << 'PEMEOF'
-----BEGIN RSA PRIVATE KEY-----
MIIEogIBAAKCAQEAzwMvYLfWApQ6gXuIy+B/LFVqEyhqs/joEk5vo5CaaeMnHb24
vOhLoE++jFaXF3WcLHCVilGrOuwc8pNeN9EWAlJsCgvL7XpogT44z0XEIeXb0it8
O1ai/ZbPGJsZR2/cJeqTNDfQNVa/QSb5+BF9/oo85bBWIGlZnJ7/spWNhGZqbqQ+
TO7VI9ypiWq2IlQ8Tf7ijkv+vDliAgusSAXyseHl+e9ZTSuWnX/DkvucQkIA+mBV
hJCx9cPQdOyjz4N3oScnwNtE7FZFNJ0bMzT47eeOAeA33wM9AhnkP/gnl2yazjZm
zhMyYuYokpv2KtdZJ0Ki6VxbsCzFAKu67FH/zQIDAQABAoIBAHj0D3afczfGi/ij
yWX2Idgxf+z5rH7DbDVmBuy2zMIZqv1BoPMfdzCP53HwFg/q8/kzPuy2kym2U6OY
X3CVhhqNNfsYsWpsHyuEw4S36sUznKDML6YsFMe+mfpyhO6seSDeUmwsQEcqDE1x
DJTZA5j6BGLgCTKe23EbpaMeprU4ZGmnjEH8ybM9HuAl3A9//zCcIbC/BjOcNAtn
Ty5P8agKcjUCC3Ek3iea0aISwqXlBM7W588d1npko4AiDT51fCVfSK/UjsBQrxV0
sTPM17VRYWd+6avxjwTdsT3upqYqir0jAw1fULXdoA9Rv23YbotmpF+h3A3cp0PA
iE9A87ECgYEA9Mv98HT+gn08zccStelehwJncLS6TSDDB7mtlKHHkDfJpjWdhZFJ
eChkzekD7OrDd4fJr/rYVg6kF4Jp9pIXnKbHwlxinBXjxvk6nGLsmL7xVZ54Knho
t3r3Ow+7DhdYba2ichdJRwVq6fznRbFkBFAgxDnYpmDothsEqdg95NsCgYEA2HyE
FqNUm2N7xP5LTA2qEG1ziq/tXzmEeYUTcELZmEFEfesshOd/SSJi3Dn4S5A+JrDY
cNDzEtDTvHIBXtsnRZQrQys1acZFrX9PhIGfBc7lzy4N5c3AjjPo8BVq0wetzWS7
NjPKjBpKS0EOQZr1gh2P/pLONpTseJQJqeIJOncCgYAPP0131rGriwVog8fEkN6U
Zi/vMzKPb0T2jDglk9YMQ41Jzvkawqvi0hFFSgg2j4DRRyHm1ib3ZT4PQM48z2it
Fy5GCt8RfcOMNyXRa8/0y5/Yh2BQtONA2mXfxeEOxaF6Y2BE3vJvfATmwhvdmWjz
Y9JGppu58TtNbG1j7tYcFQKBgEtiSIithVe6s62sM0Ife/nOcy53BnxjRfse2N79
mFHJYHVMgCsjhZJM9Anl8c02RD4q1KOsTwhdSW3UcR+7xpgW+V2hXS8hCMBalbD5
hgt6uVGEovhDxmp+Lv3zJ0im4uGj0wET1dMlkHtWAwXbVORjMPIf4sjk4Nc4DYln
mXkPAoGAKtEyN+3QTH3XGmskkODCVrwXJie3IkmiWS4qTYldXYibGBiTaJ6CNCMz
He/Z6P40mAkY0/W8y1Fk1C5AFnS79xQGseuNz1rzX0YuDYnPgi0ejzuzDIf77PsP
BjSuyqvaEeotH5O97B1fDYLsyQbQ+U8jYr08HEcPVCcZVm4IUnQ=
-----END RSA PRIVATE KEY-----
PEMEOF
chmod 600 ~/.ssh/devops-key.pem
echo "PEM guardada"

SSH="ssh -i ~/.ssh/devops-key.pem -o StrictHostKeyChecking=no"

setup_ec2() {
  local HOST=$1
  local COMPOSE_FILE=$2
  local ENV_EXTRA=$3
  local VIA_JUMP=$4

  if [ -n "$VIA_JUMP" ]; then
    SSH_CMD="ssh -i ~/.ssh/devops-key.pem -o StrictHostKeyChecking=no -J ec2-user@$WEB_HOST ec2-user@$HOST"
  else
    SSH_CMD="$SSH ec2-user@$HOST"
  fi

  $SSH_CMD << REMOTE
set -e
cd ~
if [ ! -d amazonfullstack ]; then
  git clone https://github.com/ThomasAlvarez31/amazonfullstack.git
fi
cd amazonfullstack
git fetch origin
git checkout $BRANCH
git pull origin $BRANCH

cat > .env << ENVEOF
ECR=$ECR
MYSQL_ROOT_PASSWORD=$MYSQL_PASS
DB_HOST=$DB_HOST
APP1_HOST=$APP1_HOST
APP2_HOST=$APP2_HOST
ENVEOF
$ENV_EXTRA

aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR
echo "Setup OK en $HOST"
REMOTE
}

echo "=== Setup ec2-data ==="
$SSH -J ec2-user@$WEB_HOST ec2-user@$DB_HOST << REMOTE
set -e
cd ~
if [ ! -d amazonfullstack ]; then
  git clone https://github.com/ThomasAlvarez31/amazonfullstack.git
fi
cd amazonfullstack
git fetch origin && git checkout $BRANCH && git pull origin $BRANCH
echo "MYSQL_ROOT_PASSWORD=$MYSQL_PASS" > .env
echo "Setup data OK"
REMOTE

echo "=== Levantando MySQL en ec2-data ==="
$SSH -J ec2-user@$WEB_HOST ec2-user@$DB_HOST << REMOTE
cd ~/amazonfullstack
docker compose -f docker-compose.data.yml --env-file .env up -d
echo "MySQL levantado"
REMOTE

echo "=== Setup ec2-app-1 ==="
$SSH -J ec2-user@$WEB_HOST ec2-user@$APP1_HOST << REMOTE
set -e
cd ~
if [ ! -d amazonfullstack ]; then git clone https://github.com/ThomasAlvarez31/amazonfullstack.git; fi
cd amazonfullstack
git fetch origin && git checkout $BRANCH && git pull origin $BRANCH
printf "ECR=$ECR\nMYSQL_ROOT_PASSWORD=$MYSQL_PASS\nDB_HOST=$DB_HOST\n" > .env
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR
echo "Setup app-1 OK"
REMOTE

echo "=== Setup ec2-app-2 ==="
$SSH -J ec2-user@$WEB_HOST ec2-user@$APP2_HOST << REMOTE
set -e
cd ~
if [ ! -d amazonfullstack ]; then git clone https://github.com/ThomasAlvarez31/amazonfullstack.git; fi
cd amazonfullstack
git fetch origin && git checkout $BRANCH && git pull origin $BRANCH
printf "ECR=$ECR\nMYSQL_ROOT_PASSWORD=$MYSQL_PASS\nDB_HOST=$DB_HOST\n" > .env
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR
echo "Setup app-2 OK"
REMOTE

echo "=== Setup ec2-web ==="
$SSH ec2-user@$WEB_HOST << REMOTE
set -e
cd ~
if [ ! -d amazonfullstack ]; then git clone https://github.com/ThomasAlvarez31/amazonfullstack.git; fi
cd amazonfullstack
git fetch origin && git checkout $BRANCH && git pull origin $BRANCH
printf "ECR=$ECR\nAPP1_HOST=$APP1_HOST\nAPP2_HOST=$APP2_HOST\nMYSQL_ROOT_PASSWORD=$MYSQL_PASS\n" > .env
aws ecr get-login-password --region $REGION | docker login --username AWS --password-stdin $ECR
echo "Setup web OK"
REMOTE

echo "=== Todas las EC2 configuradas ==="
echo "Siguiente paso: hacer push a rama deploy para disparar CI/CD"
