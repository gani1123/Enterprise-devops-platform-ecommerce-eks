# 🚀 Enterprise DevOps Platform — E-Commerce on AWS EKS

> **End-to-end CI/CD platform** for Java WAR-based microservices — built with Jenkins Shared Libraries, AWS EKS, Helm, and a full DevSecOps toolchain.

---

## 🏗️ Architecture Overview

```
GitHub → Jenkins → Maven Build → Nexus → SonarQube → Docker → Trivy → ECR → EKS (Helm)
```

| Layer | Tool |
|---|---|
| Source Control | GitHub |
| CI/CD Orchestration | Jenkins + Shared Library |
| Build | Maven 3.9 + JDK 17 |
| Artifact Management | Nexus Repository |
| Code Quality | SonarQube |
| Containerization | Docker |
| Security Scanning | Trivy |
| Container Registry | AWS ECR |
| Deployment | AWS EKS + Helm |
| Notifications | Slack + Email |

---

## 📁 Repository Structure

```
Enterprise-devops-platform-ecommerce-eks/
├── app-monolith/               # Monolith Spring Boot WAR application
├── app-order-service/          # Order microservice
├── app-product-service/        # Product microservice
├── helm-monolith/              # Helm chart — monolith deployment
├── helm-order-service/         # Helm chart — order service
├── helm-product-service/       # Helm chart — product service
├── docker-compose/             # Local development setup
├── jenkins/                    # Jenkinsfile for pipeline
├── vars/                       # Jenkins Shared Library functions
│   ├── checkoutCode.groovy
│   ├── buildWar.groovy
│   ├── nexusUpload.groovy
│   ├── sonarScan.groovy
│   ├── dockerBuild.groovy
│   ├── trivyScan.groovy
│   ├── dockerPush.groovy
│   ├── deployEKS.groovy
│   ├── deployWarToEKS.groovy
│   ├── updateHelm.groovy
│   ├── cleanup.groovy
│   ├── slackNotification.groovy
│   └── emailNotification.groovy
└── README.md
```

---

## 📚 Jenkins Shared Library — Core Design

This project uses a **reusable Jenkins Shared Library** (`devops-pipeline-library`) instead of monolithic Jenkinsfiles. Each pipeline stage calls a dedicated Groovy function from `vars/` — making pipelines clean, DRY, and maintainable across multiple applications.

### Why Shared Libraries?

| Without Shared Library | With Shared Library |
|---|---|
| 300+ line Jenkinsfile per app | 50-line Jenkinsfile per app |
| Copy-paste across teams | Single source of truth |
| Hard to update pipeline logic | Change once, applies everywhere |
| Inconsistent standards | Enforced standards across all apps |

### Available Functions

| Function | Purpose |
|---|---|
| `checkoutCode()` | Git checkout with credentials |
| `buildWar()` | Maven WAR build + unit tests |
| `nexusUpload()` | Publish artifact to Nexus |
| `sonarScan()` | SonarQube static code analysis |
| `dockerBuild()` | Build Docker image |
| `trivyScan()` | Trivy container vulnerability scan |
| `dockerPush()` | Push image to AWS ECR |
| `deployEKS()` | Helm-based rolling deploy to EKS |
| `updateHelm()` | Update Helm chart image tag |
| `cleanup()` | Remove local Docker images post-deploy |
| `slackNotification()` | Slack build status alerts |
| `emailNotification()` | Email build status alerts |

---

## ⚙️ CI/CD Pipeline — 8 Stages

```groovy
@Library('devops-pipeline-library') _

pipeline {
  stages {
    stage('Checkout')            // Pull code from GitHub
    stage('Build WAR')           // Maven build + tests
    stage('Publish to Nexus')    // Upload WAR artifact
    stage('SonarQube Scan')      // Code quality gate
    stage('Docker Build')        // Build container image
    stage('Trivy Scan')          // Security vulnerability scan
    stage('Push to ECR')         // Push to AWS container registry
    stage('Deploy to EKS')       // Helm rolling deployment
  }
}
```

**Pipeline Features:**
- ⏱️ 60-minute timeout with timestamps
- 🔄 Rolling deployments — zero downtime
- 🧹 Automatic cleanup of local images post-deploy
- 📧 Slack + Email notifications on success/failure
- 🔒 Credentials managed via Jenkins credential store (no hardcoded secrets)

---

## ☸️ Kubernetes — EKS Deployment

**Cluster:** `enterprise-eks-us-east-1`  
**Namespace:** `ecommerce`

```bash
# Verify deployment
kubectl get pods -n ecommerce
kubectl get svc -n ecommerce
kubectl get hpa -n ecommerce
```

**Auto Scaling (HPA):**
- Minimum replicas: 2
- Maximum replicas: 6
- Scale trigger: CPU utilization

**Deployment Strategy:** Rolling update — ensures zero downtime during releases.

---

## 🔐 DevSecOps — Security Built Into Pipeline

Security is **not a final gate** — it's integrated at every layer:

| Stage | Tool | What It Checks |
|---|---|---|
| Code commit | SonarQube | Code quality, bugs, vulnerabilities, coverage |
| Container build | Trivy | OS packages, dependency CVEs in Docker image |
| AWS access | IAM Roles | No long-lived credentials — role-based auth |
| Registry | ECR | Private registry with IAM-controlled access |

> Pipeline **automatically fails** if Trivy detects CRITICAL vulnerabilities — image is never pushed to ECR.

---

## 🧩 Microservices

| Service | Description | Tech Stack |
|---|---|---|
| `app-monolith` | Core e-commerce WAR app | Spring Boot, Java 17 |
| `app-order-service` | Order processing REST API | Spring Boot |
| `app-product-service` | Product catalog REST API | Spring Boot |

Each service has its own:
- Helm chart for Kubernetes deployment
- Independent pipeline stage
- ECR repository

---

## 🚀 How to Run Locally

```bash
# Clone the repo
git clone https://github.com/gani1123/Enterprise-devops-platform-ecommerce-eks.git

# Start local environment
cd docker-compose
docker-compose up -d

# Verify services
docker ps
```

---

## 📊 Key Metrics

| Metric | Result |
|---|---|
| Pipeline execution time | ~12 minutes end-to-end |
| Deployment strategy | Rolling (zero downtime) |
| Security gates | Trivy + SonarQube (blocks on CRITICAL) |
| Environments supported | Dev, Staging, Production |
| Auto-scaling range | 2 → 6 replicas |

---

## 🔮 Roadmap

- [ ] ArgoCD GitOps — replace Jenkins deploy stage
- [ ] Ingress Controller + Route53 custom domain
- [ ] HTTPS via AWS ACM
- [ ] Prometheus + Grafana monitoring stack
- [ ] Canary deployments via Argo Rollouts

---

## 👨‍💻 Author

**Ganesh Nalli** — AWS DevOps Engineer  
🔗 [LinkedIn](https://linkedin.com/in/ganeshnalli) • [GitHub](https://github.com/gani1123)  
📧 ganeshnalli.devops@gmail.com
