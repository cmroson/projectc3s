# projectc3s

This project runs a containerized basic web application through a CI/CD pipeline built in Jenkins that integrates Docker, Kubernetes through AWS EKS, and an EC2 Instance acting as the main server.

Build Steps:

Create an EC2 Instance:

Linux AMI, t.2Medium, 20 GB storage on a General Purpose SSD Volume

Security Group: Open ports 22, 8080, 8090, 9418, 80, 443

Create IAM Roles in the console:

EKSClusterRole: Choose EKS from the list, then EKS - Cluster for the use case

EC2NodeInstanceRole: Choose EC2 as the common use case, then in permissions --> AmazonEKSWorkerNodePolicy, AmazonEKS_CNI_Policy, AmazonEC2ContainerRegistryReadOnly

SSH into the EC2 Instance:

Install Amazon CLI:

curl https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip -o awscliv2.zip
unzip awscliv2.zip
sudo ./aws/install
Configure AWS Credentials:

aws configure
Install Kubectl and Eksctl and move to a folder in your path:

curl -o kubectl https://amazon-eks.s3.us-west-2.amazonaws.com/1.17.9/2020-08-04/bin/linux/amd64/kubectl
chmod +x ./kubectl
sudo mv ./kubectl /usr/local/bin
kubectl version --short --client
sudo curl --silent --location https://github.com/weaveworks/eksctl/releases/latest/download/eksctl_$(uname -s)_amd64.tar.gz | tar xz -C /tmp
sudo mv /tmp/eksctl /usr/local/bin
eksctl version
Create an AWS EKS Cluster:

aws eks create-cluster --name helloworld --role-arn <EKS Cluster ARN> --resources-vpc-config subnetIds=<subnetId1,subnetId2,...>,securityGroupIds=<securityGroupId1,securityGroupId2,...>
While AWS is creating the cluster, install services -->

Git:

sudo yum update -y
sudo yum install install git
Jenkins:

sudo yum remove java-1.7.0
sudo wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import http://pkg.jenkins.io/redhat-stable/jenkins.io.key
sudo yum upgrade
sudo yum install jenkins java-1.8.0-openjdk-devel
sudo service jenkins start
Docker, give the Instance and Jenkins access:

sudo yum install docker
sudo service docker start
sudo usermod -a -G docker ec2-user
sudo chmod 666 /var/run/docker.sock
sudo chmod 755 /var/run/docker.sock
Test Docker access:

docker info
Maven, put in an easily accessible file:

cd /opt
sudo wget https://downloads.apache.org/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz
ls (To get Maven File Name)
sudo tar -xvzf <File Name>
sudo mv <File Name> maven
Once the Cluster is complete, create the Kubeconfig file and give access to Jenkins:

aws eks --region <Your Region> update-kubeconfig --name <Cluster Name>
kubectl get svc (Test configuration)
sudo cp /home/ec2-user/.kube/config /home (Move to home directory)
sudo chmod 755 /home/config
Clone your Git Repository to your EC2:

sudo git clone <Git Repository URL>
cd <Folder of Git Clone>
kubectl apply -f <Deployment.Yaml Name>
Create Load Balancer:

kubectl expose deployment <Deployment Name from Deployment.Yaml> --type=LoadBalancer --name=loadbalancer
Login to Jenkins:

Go to <EC2 DNS URL/8080> in web browser In EC2 SSH Shell --> sudo cat /var/lib/jenkins/secrets/initialAdminPassword Enter temporary password into Jenkins portal --> create user credentials Setup default plugins to continue to Jenkins Portal

Configure Jenkins:

Download Plugins:

-Docker Plugin, Docker Compose Build Step, Cloudbees Docker Build and Publish, Docker-build-step, Docker Pipeline
-Cloudbees AWS Plugin, Pipeline: AWS Steps Plugin
-Kubernetes CLI, Kubernetes
-maven invoker, maven integration
Global Tool Configuration:

-JDK
  --Agree to terms and click Oracle Login link to input Oracle credentials
  --Check Install Automatically
  --Name anything (match name in Jenkinsfile)
  --Select JKD8 Version
-Maven
  --Name anything (match name in Jenkinsfile)
  --Check Install Automatically
  --Select version 3.6.0 under Install from Apache
-Docker
  --Name Docker
  --Check Install Automatically
Manage Credentials

-Create Username and Password
  --Username - Docker Username
  --Password - Docker Password
  --ID - anything (Match name in Jenkinsfile)
-Create AWS Credentials
  --Insert AWS account Credentials
  --ID - anything (Match name in Jenkinsfile)
Create Pipeline:

-Click New Item
-Name your pipeline and select pipeline
-In Build Trigger Select
  --Check GitHub hook trigger for GITScm polling
-Under pipeline
  --SCM --> Git
    --Repository URL --> <Your Github Repository URL>
  --Branches to Build
    --Branch Specifier --> */main
-Save
GitHub:

Create a GitHub Webhook:

-Under settings, click webhooks
-Add webhhok
  --Payload URL --> <EC2 DNS:8080/github-webhook/>
  --Select JSON option
  --Events Select --> check send me everything
In Jenkins, in your pipeline, Click Build Now

Access web app:

In a web browser, insert DNS for the load balancer:8090
