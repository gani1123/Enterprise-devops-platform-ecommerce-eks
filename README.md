# 🚀 Enterprise DevOps Platform — AWS EKS Deployment

## 🌐 Live Application

The application is successfully deployed on AWS EKS and accessible via LoadBalancer.

URL:
http://a9c049c0143b243088601ba6ad8c5349-1627a30220a41251.elb.us-east-1.amazonaws.com

Features:
- Product catalog listing
- Inventory dashboard
- REST APIs (Spring Boot)
- Kubernetes auto-scaling
- Zero-downtime deployments

---

## 📸 Application Screenshot

docs/screenshots/inventory-dashboard.png

---

## 🏗️ Architecture

GitHub → Jenkins → Maven → Nexus → SonarQube → Docker → Trivy → ECR → EKS

---

## ⚙️ CI/CD Pipeline Flow

1. Code pushed to GitHub
2. Jenkins pipeline triggered
3. Maven build & tests
4. SonarQube scan
5. Artifact upload to Nexus
6. Docker build
7. Trivy scan
8. Push to ECR
9. Deploy to EKS

---

## 🚀 Kubernetes Deployment

Components:
- Deployment: ecommerce-api
- Service: LoadBalancer
- Namespace: ecommerce

Verify:

kubectl get pods -n ecommerce
kubectl get svc -n ecommerce

---

## ⚡ Auto Scaling

- Min replicas: 2
- Max replicas: 6
- CPU-based scaling

---

## 🔐 Security

- Trivy vulnerability scanning
- SonarQube code analysis
- IAM role-based authentication
- Secure ECR registry

---

## 🧪 API Testing

Get Products:

curl http://a9c049c0143b243088601ba6ad8c5349-1627a30220a41251.elb.us-east-1.amazonaws.com/ecommerce-api/products

Health Check:

curl http://a9c049c0143b243088601ba6ad8c5349-1627a30220a41251.elb.us-east-1.amazonaws.com/ecommerce-api/health

---

## 🏆 Key Achievements

- Built end-to-end CI/CD pipeline using Jenkins
- Deployed application on AWS EKS
- Implemented rolling deployments
- Integrated security scanning (Trivy + SonarQube)
- Managed IAM + EKS RBAC

---

## 🔮 Future Enhancements

- Custom domain (Route53)
- HTTPS (ACM)
- Ingress Controller
- GitOps (ArgoCD)

---

## ✅ Final Status

CI/CD Pipeline — Working
ECR Push — Working
EKS Deployment — Working
Pods — Running
LoadBalancer — Active
Application — LIVE

---

## 👨‍💻 Author

Ganesh Nalli  
DevOps Engineer | AWS | Kubernetes | CI/CD

