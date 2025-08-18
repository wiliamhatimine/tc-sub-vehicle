# Deploy kit: Postgres + Flyway + ECR/ECS (Fargate) + GitHub Actions

This package adds:
- Postgres-ready Spring Boot apps (vehicle-api & auth-service) with Flyway migrations.
- Multi-stage Dockerfiles for both services.
- GitHub Actions workflow to build images, push to ECR and run Terraform to create infra and deploy to ECS Fargate.
- Terraform skeleton that creates ECR repositories and an ECS cluster + IAM roles and a sample Fargate service that can be updated to use built images.

**Important secrets (GitHub repository secrets)**:
- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY
- AWS_REGION (e.g. us-east-1)
- AWS_ACCOUNT_ID (your AWS account ID)
- TF_VAR_db_password (Postgres password for RDS if you choose to create one)
- TF_VAR_db_user
- TF_VAR_db_name
- ECR_REPO_VEHICLE (optional; default created by terraform)
- ECR_REPO_AUTH (optional)

**High level flow in CI**:
1. Build both images and push to ECR (vehicle-api and auth-service).
2. Run `terraform init` and `terraform apply -auto-approve` to create ECR repos, ECS cluster, task roles, security groups, and an application load balancer + service.
3. Terraform outputs the service URL(s).

**Caveats & security**:
- This is a skeleton. Review IAM permissions before using in prod.
- Terraform `apply -auto-approve` is used for automation; consider manual approval in sensitive environments.
- RDS (managed Postgres) is *not* created by default in this skeleton — you can extend terraform to create an RDS instance or use an existing database and set connection info via secrets.
