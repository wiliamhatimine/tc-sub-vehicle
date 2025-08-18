resource "aws_ecr_repository" "vehicle" {
  name = "vehicle-api"
  image_tag_mutability = "MUTABLE"
}

resource "aws_ecr_repository" "auth" {
  name = "auth-service"
  image_tag_mutability = "MUTABLE"
}

output "vehicle_ecr_repository" {
  value = aws_ecr_repository.vehicle.repository_url
}

output "auth_ecr_repository" {
  value = aws_ecr_repository.auth.repository_url
}
