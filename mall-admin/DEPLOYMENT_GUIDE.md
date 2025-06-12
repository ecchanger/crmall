# 商品管理后台部署指南

## 📋 概述

本指南提供了商品管理后台系统的完整部署流程，包括环境配置、数据库设置、应用部署等步骤。

## 🔧 环境要求

### 基础环境
- **Java**: JDK 8 或更高版本
- **Maven**: 3.6+ 
- **MySQL**: 5.7+ 或 8.0+
- **Redis**: 5.0+
- **Elasticsearch**: 7.17.3 (可选，用于商品搜索)

### 推荐配置
- **CPU**: 4核心以上
- **内存**: 8GB以上
- **存储**: 100GB以上SSD
- **网络**: 100Mbps以上

## 🗄️ 数据库配置

### 1. MySQL 数据库初始化

```sql
-- 创建数据库
CREATE DATABASE mall CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 创建用户（可选）
CREATE USER 'mall_user'@'%' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON mall.* TO 'mall_user'@'%';
FLUSH PRIVILEGES;
```

### 2. 导入数据库脚本

```bash
# 导入基础数据库结构
mysql -u root -p mall < document/sql/mall.sql

# 如果有初始化数据
mysql -u root -p mall < document/sql/mall_data.sql
```

### 3. 数据库连接配置

编辑 `mall-admin/src/main/resources/application-dev.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mall?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: mall_user
    password: your_password
    druid:
      initial-size: 5
      min-idle: 5
      max-active: 20
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
      validation-query: SELECT 1 FROM DUAL
      test-while-idle: true
      test-on-borrow: false
      test-on-return: false
```

## 🔴 Redis 配置

### 1. Redis 安装和启动

```bash
# Ubuntu/Debian
sudo apt-get install redis-server
sudo systemctl start redis-server
sudo systemctl enable redis-server

# CentOS/RHEL
sudo yum install redis
sudo systemctl start redis
sudo systemctl enable redis
```

### 2. Redis 连接配置

编辑 `mall-admin/src/main/resources/application-dev.yml`:

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: # 如果设置了密码
    timeout: 3000ms
    database: 0
    lettuce:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0
```

## 🔍 Elasticsearch 配置（可选）

### 1. Elasticsearch 安装

```bash
# 下载并安装 Elasticsearch 7.17.3
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-7.17.3-linux-x86_64.tar.gz
tar -xzf elasticsearch-7.17.3-linux-x86_64.tar.gz
cd elasticsearch-7.17.3

# 启动 Elasticsearch
./bin/elasticsearch
```

### 2. Elasticsearch 配置

编辑 `mall-search/src/main/resources/application.yml`:

```yaml
spring:
  elasticsearch:
    rest:
      uris: http://localhost:9200
      username: # 如果启用了安全认证
      password: # 如果启用了安全认证
```

## 🚀 应用部署

### 1. 编译打包

```bash
# 进入项目根目录
cd /path/to/mall

# 编译整个项目
mvn clean compile

# 打包 mall-admin 模块
cd mall-admin
mvn clean package -Dmaven.test.skip=true
```

### 2. 运行应用

#### 开发环境运行

```bash
# 方式1: 使用 Maven 运行
mvn spring-boot:run

# 方式2: 使用 Java 运行
java -jar target/mall-admin-1.0-SNAPSHOT.jar

# 方式3: 指定配置文件
java -jar target/mall-admin-1.0-SNAPSHOT.jar --spring.profiles.active=dev
```

#### 生产环境运行

```bash
# 创建运行脚本
cat > start.sh << 'EOF'
#!/bin/bash
nohup java -Xms2g -Xmx4g -jar \
  -Dspring.profiles.active=prod \
  -Dserver.port=8080 \
  target/mall-admin-1.0-SNAPSHOT.jar \
  > logs/mall-admin.log 2>&1 &
echo $! > mall-admin.pid
EOF

chmod +x start.sh
./start.sh
```

### 3. 停止应用

```bash
# 创建停止脚本
cat > stop.sh << 'EOF'
#!/bin/bash
if [ -f mall-admin.pid ]; then
  PID=$(cat mall-admin.pid)
  kill $PID
  rm mall-admin.pid
  echo "Application stopped"
else
  echo "PID file not found"
fi
EOF

chmod +x stop.sh
./stop.sh
```

## 🐳 Docker 部署

### 1. 创建 Dockerfile

```dockerfile
FROM openjdk:8-jre-alpine

LABEL maintainer="your-email@example.com"

# 设置时区
RUN apk add --no-cache tzdata
ENV TZ=Asia/Shanghai

# 创建应用目录
WORKDIR /app

# 复制应用文件
COPY target/mall-admin-1.0-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 启动应用
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### 2. 构建镜像

```bash
# 构建镜像
docker build -t mall-admin:latest .

# 查看镜像
docker images | grep mall-admin
```

### 3. 运行容器

```bash
# 运行容器
docker run -d \
  --name mall-admin \
  -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/mall \
  -e SPRING_REDIS_HOST=host.docker.internal \
  mall-admin:latest

# 查看日志
docker logs -f mall-admin
```

## 🔧 配置文件说明

### 生产环境配置

创建 `application-prod.yml`:

```yaml
server:
  port: 8080
  servlet:
    context-path: /admin

spring:
  datasource:
    url: jdbc:mysql://your-db-host:3306/mall?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:mall_user}
    password: ${DB_PASSWORD:your_password}
    
  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    
  servlet:
    multipart:
      max-file-size: 50MB
      max-request-size: 50MB

logging:
  level:
    com.macro.mall: INFO
  file:
    name: logs/mall-admin.log
  pattern:
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

## 🔒 安全配置

### 1. JWT 密钥配置

```yaml
jwt:
  secret: ${JWT_SECRET:your-very-long-and-secure-secret-key}
  expiration: 604800 # 7天
```

### 2. 数据库连接池配置

```yaml
spring:
  datasource:
    druid:
      initial-size: 10
      min-idle: 10
      max-active: 50
      max-wait: 60000
      # 启用监控统计
      stat-view-servlet:
        enabled: true
        url-pattern: /druid/*
        login-username: admin
        login-password: ${DRUID_PASSWORD:your_password}
```

## 📊 监控配置

### 1. 应用监控

```yaml
management:
  endpoints:
    web:
      exposure:
        include: "*"
  endpoint:
    health:
      show-details: always
  metrics:
    export:
      prometheus:
        enabled: true
```

### 2. 日志配置

```yaml
logging:
  level:
    root: INFO
    com.macro.mall: DEBUG
    org.springframework.web: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/mall-admin.log
    max-size: 100MB
    max-history: 30
```

## 🔧 性能优化

### 1. JVM 参数优化

```bash
java -Xms2g -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+PrintGCDetails \
  -XX:+PrintGCTimeStamps \
  -Xloggc:logs/gc.log \
  -jar mall-admin.jar
```

### 2. 数据库连接池优化

```yaml
spring:
  datasource:
    druid:
      initial-size: 20
      min-idle: 20
      max-active: 100
      max-wait: 60000
      time-between-eviction-runs-millis: 60000
      min-evictable-idle-time-millis: 300000
```

## 🚨 故障排查

### 1. 常见问题

**问题**: 应用启动失败
```bash
# 检查日志
tail -f logs/mall-admin.log

# 检查端口占用
netstat -tlnp | grep 8080

# 检查Java进程
jps -l | grep mall
```

**问题**: 数据库连接失败
```bash
# 测试数据库连接
mysql -h your-db-host -u mall_user -p mall

# 检查防火墙
sudo ufw status
```

**问题**: Redis连接失败
```bash
# 测试Redis连接
redis-cli -h your-redis-host ping

# 检查Redis状态
sudo systemctl status redis
```

### 2. 健康检查

```bash
# 应用健康检查
curl http://localhost:8080/actuator/health

# 检查应用指标
curl http://localhost:8080/actuator/metrics
```

## 📋 部署检查清单

- [ ] 数据库已创建并导入初始数据
- [ ] Redis 服务正常运行
- [ ] 应用配置文件已正确配置
- [ ] 防火墙端口已开放
- [ ] 日志目录已创建
- [ ] 应用能正常启动
- [ ] 健康检查接口正常
- [ ] 前端能正常访问后端API
- [ ] 文件上传功能正常
- [ ] 数据库连接池正常

---

**注意**: 生产环境部署时，请确保所有敏感信息（如数据库密码、JWT密钥等）通过环境变量或配置中心管理，不要硬编码在配置文件中。
