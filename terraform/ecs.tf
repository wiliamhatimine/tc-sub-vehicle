resource "aws_ecs_cluster" "main" {
  name = "vehicle-cluster"
}

# Task role & execution role (minimal)
resource "aws_iam_role" "ecs_task_execution" {
  name = "ecsTaskExecutionRole"
  assume_role_policy = data.aws_iam_policy_document.ecs_task_assume.json
}
data "aws_iam_policy_document" "ecs_task_assume" {
  statement {
    effect = "Allow"
    principals {
      type = "Service"
      identifiers = ["ecs-tasks.amazonaws.com"]
    }
    actions = ["sts:AssumeRole"]
  }
}

resource "aws_iam_role_policy_attachment" "ecs_exec_attach" {
  role       = aws_iam_role.ecs_task_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_task_definition" "vehicle_task" {
  family                   = "vehicle-api-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "512"
  memory                   = "1024"
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn
  container_definitions = jsonencode([
    {
      name = "vehicle-api"
      image = var.vehicle_image
      portMappings = [{ containerPort = 8080, hostPort = 8080, protocol = "tcp" }]
      environment = [
        { name = "DB_HOST", value = "" },
        { name = "DB_PORT", value = "5432" },
        { name = "DB_NAME", value = var.db_name },
        { name = "DB_USER", value = var.db_user },
        { name = "DB_PASSWORD", value = var.db_password }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group" = "/ecs/vehicle-api"
          "awslogs-region" = var.aws_region
          "awslogs-stream-prefix" = "vehicle"
        }
      }
    }
  ])
}

resource "aws_ecs_task_definition" "auth_task" {
  family                   = "auth-service-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn
  container_definitions = jsonencode([
    {
      name = "auth-service"
      image = var.auth_image
      portMappings = [{ containerPort = 8081, hostPort = 8081, protocol = "tcp" }]
      environment = []
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group" = "/ecs/auth-service"
          "awslogs-region" = var.aws_region
          "awslogs-stream-prefix" = "auth"
        }
      }
    }
  ])
}

# NOTE: This is a minimal example. You likely want ALB, security groups, subnets, and an ECS service.
output "ecs_cluster" {
  value = aws_ecs_cluster.main.id
}
