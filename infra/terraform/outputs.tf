output "ec2_web_instance_id" {
  value       = aws_instance.web.id
  description = "Instance ID for ec2-web"
}

output "ec2_app1_instance_id" {
  value       = aws_instance.app1.id
  description = "Instance ID for ec2-app-1"
}

output "ec2_app2_instance_id" {
  value       = aws_instance.app2.id
  description = "Instance ID for ec2-app-2"
}

output "ec2_data_instance_id" {
  value       = aws_instance.data.id
  description = "Instance ID for ec2-data"
}

output "ec2_web_public_ip" {
  value       = aws_instance.web.public_ip
  description = "Public IP for ec2-web"
}

output "ec2_app1_private_ip" {
  value       = aws_instance.app1.private_ip
  description = "Private IP for ec2-app-1"
}

output "ec2_app2_private_ip" {
  value       = aws_instance.app2.private_ip
  description = "Private IP for ec2-app-2"
}

output "ec2_data_private_ip" {
  value       = aws_instance.data.private_ip
  description = "Private IP for ec2-data"
}

output "eks_cluster_name" {
  value       = aws_eks_cluster.main.name
  description = "EKS cluster name"
}

output "eks_cluster_endpoint" {
  value       = aws_eks_cluster.main.endpoint
  description = "EKS API server endpoint"
}
