variable "aws_region" {
  type = string
  default = "us-east-1"
}

variable "vehicle_image" {
  type = string
}

variable "auth_image" {
  type = string
}

variable "db_name" {
  type = string
  default = "vehicledb"
}

variable "db_user" {
  type = string
  default = "postgres"
}

variable "db_password" {
  type = string
  default = ""
}
