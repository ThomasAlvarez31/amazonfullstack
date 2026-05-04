#!/bin/bash
set -e
VPC_ID=vpc-0058f1845022237d0
AMI=ami-0c101f26f147fa7fd
AZ=us-east-1a
KEY=devops-key

echo "=== Creando subnets ==="
SUBNET_APP1=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.2.0/24 --availability-zone $AZ --query 'Subnet.SubnetId' --output text)
aws ec2 create-tags --resources $SUBNET_APP1 --tags Key=Name,Value=subnet-app-1

SUBNET_APP2=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.4.0/24 --availability-zone $AZ --query 'Subnet.SubnetId' --output text)
aws ec2 create-tags --resources $SUBNET_APP2 --tags Key=Name,Value=subnet-app-2

SUBNET_DATA=$(aws ec2 create-subnet --vpc-id $VPC_ID --cidr-block 10.0.3.0/24 --availability-zone $AZ --query 'Subnet.SubnetId' --output text)
aws ec2 create-tags --resources $SUBNET_DATA --tags Key=Name,Value=subnet-data
echo "subnets: $SUBNET_APP1 $SUBNET_APP2 $SUBNET_DATA"

echo "=== NAT Gateway ==="
SUBNET_WEB=$(aws ec2 describe-subnets --filters Name=vpc-id,Values=$VPC_ID Name=cidrBlock,Values=10.0.1.0/24 --query 'Subnets[0].SubnetId' --output text)
EIP=$(aws ec2 allocate-address --domain vpc --query 'AllocationId' --output text)
NAT=$(aws ec2 create-nat-gateway --subnet-id $SUBNET_WEB --allocation-id $EIP --query 'NatGateway.NatGatewayId' --output text)
echo "NAT creado: $NAT - esperando disponibilidad..."
aws ec2 wait nat-gateway-available --nat-gateway-ids $NAT
echo "NAT disponible"

echo "=== Route table privada ==="
RT=$(aws ec2 create-route-table --vpc-id $VPC_ID --query 'RouteTable.RouteTableId' --output text)
aws ec2 create-tags --resources $RT --tags Key=Name,Value=rt-privada
aws ec2 create-route --route-table-id $RT --destination-cidr-block 0.0.0.0/0 --nat-gateway-id $NAT
aws ec2 associate-route-table --route-table-id $RT --subnet-id $SUBNET_APP1
aws ec2 associate-route-table --route-table-id $RT --subnet-id $SUBNET_APP2
aws ec2 associate-route-table --route-table-id $RT --subnet-id $SUBNET_DATA
echo "Route table OK"

echo "=== Security Groups ==="
SG_WEB=$(aws ec2 describe-security-groups --filters Name=vpc-id,Values=$VPC_ID Name=group-name,Values=SG-Web --query 'SecurityGroups[0].GroupId' --output text)
SG_APP=$(aws ec2 create-security-group --group-name SG-App --description "App microservicios" --vpc-id $VPC_ID --query 'GroupId' --output text)
aws ec2 authorize-security-group-ingress --group-id $SG_APP --protocol tcp --port 22 --source-group $SG_WEB
aws ec2 authorize-security-group-ingress --group-id $SG_APP --ip-permissions "IpProtocol=tcp,FromPort=8080,ToPort=8092,UserIdGroupPairs=[{GroupId=$SG_WEB}]"
aws ec2 authorize-security-group-ingress --group-id $SG_APP --protocol icmp --port -1 --source-group $SG_WEB

SG_DATA=$(aws ec2 create-security-group --group-name SG-Data --description "Base de datos" --vpc-id $VPC_ID --query 'GroupId' --output text)
aws ec2 authorize-security-group-ingress --group-id $SG_DATA --protocol tcp --port 3306 --source-group $SG_APP
aws ec2 authorize-security-group-ingress --group-id $SG_DATA --protocol icmp --port -1 --source-group $SG_APP
echo "SG_APP=$SG_APP  SG_DATA=$SG_DATA"

echo "=== Lanzando EC2 ==="
USER_DATA=$(cat <<'USERDATA'
#!/bin/bash
yum update -y
yum install -y docker git
systemctl start docker
systemctl enable docker
usermod -a -G docker ec2-user
USERDATA
)

EC2_WEB=$(aws ec2 run-instances --image-id $AMI --instance-type t3.micro \
  --subnet-id $SUBNET_WEB --security-group-ids $SG_WEB \
  --key-name $KEY --associate-public-ip-address \
  --iam-instance-profile Name=LabInstanceProfile \
  --user-data "$USER_DATA" \
  --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=ec2-web}]" \
  --query 'Instances[0].InstanceId' --output text)

EC2_APP1=$(aws ec2 run-instances --image-id $AMI --instance-type t3.micro \
  --subnet-id $SUBNET_APP1 --security-group-ids $SG_APP \
  --key-name $KEY --iam-instance-profile Name=LabInstanceProfile \
  --user-data "$USER_DATA" \
  --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=ec2-app-1}]" \
  --query 'Instances[0].InstanceId' --output text)

EC2_APP2=$(aws ec2 run-instances --image-id $AMI --instance-type t3.micro \
  --subnet-id $SUBNET_APP2 --security-group-ids $SG_APP \
  --key-name $KEY --iam-instance-profile Name=LabInstanceProfile \
  --user-data "$USER_DATA" \
  --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=ec2-app-2}]" \
  --query 'Instances[0].InstanceId' --output text)

EC2_DATA=$(aws ec2 run-instances --image-id $AMI --instance-type t3.micro \
  --subnet-id $SUBNET_DATA --security-group-ids $SG_DATA \
  --key-name $KEY --iam-instance-profile Name=LabInstanceProfile \
  --user-data "$USER_DATA" \
  --tag-specifications "ResourceType=instance,Tags=[{Key=Name,Value=ec2-data}]" \
  --query 'Instances[0].InstanceId' --output text)

echo "=== IDs finales ==="
echo "EC2_WEB=$EC2_WEB"
echo "EC2_APP1=$EC2_APP1"
echo "EC2_APP2=$EC2_APP2"
echo "EC2_DATA=$EC2_DATA"
