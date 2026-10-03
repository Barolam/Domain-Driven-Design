# Task Management System

Dự án Java mô phỏng hệ thống quản lý công việc theo kiến trúc Clean Architecture và DDD.

## Mô tả

Project này bao gồm:
- UI Console
- GUI Swing
- Web mock API
- Persistence bằng Memory và SQLite
- Domain model, use case, DTO và presenter tách biệt theo lớp

## Yêu cầu hệ thống

- Java 17+
- Maven 3.8+

## Clone dự án

```bash
git clone <url-repository>
cd task-management-system
```

## Chạy chương trình

### 1) Build project

```bash
mvn clean compile
```

### 2) Chạy ứng dụng

```bash
mvn exec:java
```

Hoặc nếu bạn đã cài đặt Maven wrapper và muốn dùng command tương đương:

```bash
mvn clean exec:java
```

## Kết quả khi chạy

Khi khởi động, chương trình sẽ chạy 3 demo:
1. Console UI + Memory database
2. Swing GUI + SQLite storage
3. Web REST API mock + SQLite storage

## Cấu trúc thư mục chính

```text
.
├── pom.xml
├── dependencies/
│   └── src/
│       └── main/
│           └── java/
│               └── com/
│                   └── taskmanagement/
├── README.md
└── target/   (sau khi build)
```

## Gỡ lỗi thường gặp

### Lỗi Maven không tìm thấy Main class

Nếu project không chạy do lỗi `ClassNotFoundException: com.taskmanagement.infrastructure.config.Main`, hãy đảm bảo bạn đang ở thư mục gốc của project và đã chạy:

```bash
mvn clean compile
mvn exec:java
```

### Lỗi Java version

Nếu máy của bạn đang dùng Java cũ, hãy kiểm tra phiên bản:

```bash
java -version
```

Yêu cầu tối thiểu là Java 17.

## Tác giả

Dự án mẫu quản lý task theo mô hình Clean Architecture / DDD.
