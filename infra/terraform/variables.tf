variable "project_name" {
  description = "Project name used in tags and names."
  type        = string
  default     = "amazonfullstack"
}

variable "aws_region" {
  description = "AWS region where resources are created."
  type        = string
  default     = "us-east-1"
}

variable "vpc_id" {
  description = "Existing VPC ID from the lab account."
  type        = string
}

variable "public_subnet_id" {
  description = "Public subnet where ec2-web will run."
  type        = string
}

variable "private_app1_subnet_id" {
  description = "Private subnet for ec2-app-1."
  type        = string
}

variable "private_app2_subnet_id" {
  description = "Private subnet for ec2-app-2."
  type        = string
}

variable "private_data_subnet_id" {
  description = "Private subnet for ec2-data."
  type        = string
}

variable "ami_id" {
  description = "Amazon Linux AMI id used by all EC2 instances."
  type        = string
}

variable "instance_type" {
  description = "EC2 instance type for all nodes."
  type        = string
  default     = "t3.micro"
}

variable "github_repo" {
  description = "Repository in owner/name format cloned on instances."
  type        = string
  default     = "ThomasAlvarez31/amazonfullstack"
}

variable "deploy_branch" {
  description = "Branch checked out in EC2 instances."
  type        = string
  default     = "deploy"
}

variable "ssh_allowed_cidrs" {
  description = "CIDR list allowed to SSH to ec2-web (bastion)."
  type        = list(string)
  default     = ["0.0.0.0/0"]
}

variable "key_name" {
  description = "EC2 key pair name (already existing in account)."
  type        = string
}

variable "root_volume_size_gb" {
  description = "Root EBS size in GB for all instances."
  type        = number
  default     = 20
}

variable "iam_instance_profile_name" {
  description = "Existing IAM instance profile name (labs usually provide LabInstanceProfile)."
  type        = string
  default     = "LabInstanceProfile"
}

variable "lab_role_arn" {
  description = "ARN of the existing LabRole used for EKS cluster and node group (AWS Academy)."
  type        = string
}
