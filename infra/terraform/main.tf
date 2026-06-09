provider "aws" {
  region = var.aws_region

  default_tags {
    tags = {
      Project   = var.project_name
      ManagedBy = "terraform"
    }
  }
}

locals {
  app_ports = [8080, 8082, 8083, 8084, 8085, 8086, 8087, 8088, 8089, 8090, 8091, 8092, 9000]
}

resource "aws_security_group" "web" {
  name        = "${var.project_name}-web-sg"
  description = "Public access for web and bastion"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = var.ssh_allowed_cidrs
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "app" {
  name        = "${var.project_name}-app-sg"
  description = "Internal app services"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.web.id]
  }

  dynamic "ingress" {
    for_each = local.app_ports
    content {
      from_port       = ingress.value
      to_port         = ingress.value
      protocol        = "tcp"
      security_groups = [aws_security_group.web.id]
    }
  }

  ingress {
    from_port = 0
    to_port   = 65535
    protocol  = "tcp"
    self      = true
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "data" {
  name        = "${var.project_name}-data-sg"
  description = "MySQL access for app instances"
  vpc_id      = var.vpc_id

  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.web.id]
  }

  ingress {
    from_port       = 3306
    to_port         = 3306
    protocol        = "tcp"
    security_groups = [aws_security_group.app.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "web" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = var.public_subnet_id
  vpc_security_group_ids = [aws_security_group.web.id, aws_security_group.app.id]
  key_name               = var.key_name
  iam_instance_profile   = var.iam_instance_profile_name

  associate_public_ip_address = true

  root_block_device {
    volume_size = var.root_volume_size_gb
    volume_type = "gp3"
  }

  user_data = templatefile("${path.module}/user_data.sh.tftpl", {
    github_repo   = var.github_repo
    deploy_branch = var.deploy_branch
  })

  tags = {
    Name = "${var.project_name}-ec2-web"
    Role = "web"
  }
}

resource "aws_instance" "app1" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = var.private_app1_subnet_id
  vpc_security_group_ids = [aws_security_group.app.id]
  key_name               = var.key_name
  iam_instance_profile   = var.iam_instance_profile_name

  root_block_device {
    volume_size = var.root_volume_size_gb
    volume_type = "gp3"
  }

  user_data = templatefile("${path.module}/user_data.sh.tftpl", {
    github_repo   = var.github_repo
    deploy_branch = var.deploy_branch
  })

  tags = {
    Name = "${var.project_name}-ec2-app-1"
    Role = "app1"
  }
}

resource "aws_instance" "app2" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = var.private_app2_subnet_id
  vpc_security_group_ids = [aws_security_group.app.id]
  key_name               = var.key_name
  iam_instance_profile   = var.iam_instance_profile_name

  root_block_device {
    volume_size = var.root_volume_size_gb
    volume_type = "gp3"
  }

  user_data = templatefile("${path.module}/user_data.sh.tftpl", {
    github_repo   = var.github_repo
    deploy_branch = var.deploy_branch
  })

  tags = {
    Name = "${var.project_name}-ec2-app-2"
    Role = "app2"
  }
}

resource "aws_instance" "data" {
  ami                    = var.ami_id
  instance_type          = var.instance_type
  subnet_id              = var.private_data_subnet_id
  vpc_security_group_ids = [aws_security_group.data.id]
  key_name               = var.key_name
  iam_instance_profile   = var.iam_instance_profile_name

  root_block_device {
    volume_size = var.root_volume_size_gb
    volume_type = "gp3"
  }

  user_data = templatefile("${path.module}/user_data.sh.tftpl", {
    github_repo   = var.github_repo
    deploy_branch = var.deploy_branch
  })

  tags = {
    Name = "${var.project_name}-ec2-data"
    Role = "data"
  }
}

# ─── EKS ────────────────────────────────────────────────────────────────────

resource "aws_security_group" "eks_nodes" {
  name        = "${var.project_name}-eks-nodes-sg"
  description = "EKS node group communication"
  vpc_id      = var.vpc_id

  ingress {
    from_port = 0
    to_port   = 65535
    protocol  = "tcp"
    self      = true
  }

  ingress {
    from_port       = 0
    to_port         = 65535
    protocol        = "tcp"
    security_groups = [aws_security_group.app.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_eks_cluster" "main" {
  name     = "${var.project_name}-eks"
  role_arn = var.lab_role_arn
  version  = "1.31"

  vpc_config {
    subnet_ids = [
      var.public_subnet_id,
      var.private_app1_subnet_id,
      var.private_app2_subnet_id,
    ]
    endpoint_public_access  = true
    endpoint_private_access = true
  }
}

data "aws_ssm_parameter" "eks_ami" {
  name = "/aws/service/eks/optimized-ami/1.31/amazon-linux-2/recommended/image_id"
}

resource "aws_launch_template" "eks_nodes" {
  name = "${var.project_name}-eks-nodes"

  image_id      = data.aws_ssm_parameter.eks_ami.value
  instance_type = "t3.medium"

  iam_instance_profile {
    name = var.iam_instance_profile_name
  }

  vpc_security_group_ids = [aws_security_group.eks_nodes.id, aws_eks_cluster.main.vpc_config[0].cluster_security_group_id]

  user_data = base64encode(<<-EOF
    #!/bin/bash
    /etc/eks/bootstrap.sh ${var.project_name}-eks
  EOF
  )

  block_device_mappings {
    device_name = "/dev/xvda"
    ebs {
      volume_size = 20
      volume_type = "gp3"
    }
  }

  tag_specifications {
    resource_type = "instance"
    tags = {
      Name                                              = "${var.project_name}-eks-node"
      "kubernetes.io/cluster/${var.project_name}-eks"  = "owned"
    }
  }
}

resource "aws_autoscaling_group" "eks_nodes" {
  name             = "${var.project_name}-eks-nodes-asg"
  min_size         = 1
  max_size         = 3
  desired_capacity = 2

  vpc_zone_identifier = [var.private_app1_subnet_id, var.private_app2_subnet_id]

  launch_template {
    id      = aws_launch_template.eks_nodes.id
    version = "$Latest"
  }

  tag {
    key                 = "kubernetes.io/cluster/${var.project_name}-eks"
    value               = "owned"
    propagate_at_launch = true
  }

  tag {
    key                 = "Name"
    value               = "${var.project_name}-eks-node"
    propagate_at_launch = true
  }

  depends_on = [aws_eks_cluster.main]
}
