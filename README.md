# Task Management System - Person 6: User & Auth Subdomain
**Kiến trúc:** Domain-Driven Design (DDD) & Clean Architecture  
**Ngôn ngữ & Framework:** Java 17, Spring Boot 3, Spring Security 6, JJWT  
**Vị trí lưu trữ:** `D:\dai_hoc\thiet_ke_va_xay_dung_phan_mem\Gk`

---

## 1. Cách mở project trong VS Code

1. Mở phần mềm **Visual Studio Code**.
2. Nhấn menu **File -> Open Folder...** (hoặc tổ hợp phím `Ctrl + K, Ctrl + O`).
3. Điều hướng và chọn thư mục:
   ```text
   D:\dai_hoc\thiet_ke_va_xay_dung_phan_mem\Gk
   ```
4. Để chạy ứng dụng:
   - Mở file `src/main/java/com/example/taskmanagement/TaskManagementApplication.java`
   - Nhấn nút **Run** (hoặc `F5`).
   - Ứng dụng chạy tại: `http://localhost:8080`.

---

## 2. Tài liệu UML & Thiết kế kiến trúc

Toàn bộ bản thiết kế UML chi tiết cho Người số 6 được lưu tại:  
👉 **[UML_DIAGRAM.md](UML_DIAGRAM.md)** (bao gồm: Class Diagram, Use Case Diagram, Sequence Diagram).

---

## 3. Cấu trúc thư mục Clean Architecture (Module Người 6)

```text
D:\dai_hoc\thiet_ke_va_xay_dung_phan_mem\Gk/
├── pom.xml                                    # Cấu hình Maven
├── UML_DIAGRAM.md                             # Tài liệu bản vẽ UML Người 6
├── README.md                                  # Hướng dẫn sử dụng
│
└── src/
    ├── main/java/com/example/taskmanagement/
    │   ├── TaskManagementApplication.java     # Main Spring Boot
    │   │
    │   └── user/
    │       ├── domain/                        # [TẦNG 1: DOMAIN - PURE JAVA]
    │       │   ├── model/
    │       │   │   ├── Permission.java        # Enum quyền: create_task, delete_task...
    │       │   │   ├── Role.java              # Enum Role và ma trận quyền
    │       │   │   └── User.java              # Entity User chứa Rich Domain Logic
    │       │   └── repository/
    │       │       └── UserRepository.java    # Pure Interface
    │       │
    │       ├── application/                   # [TẦNG 2: APPLICATION - USE CASES]
    │       │   ├── dto/
    │       │   │   ├── LoginCommand.java
    │       │   │   ├── RegisterUserCommand.java
    │       │   │   ├── AuthTokenResponse.java
    │       │   │   └── UserResponse.java
    │       │   ├── port/
    │       │   │   ├── PasswordEncoderPort.java
    │       │   │   └── TokenProviderPort.java
    │       │   └── usecase/
    │       │       ├── AuthenticateUserUseCase.java
    │       │       └── RegisterUserUseCase.java
    │       │
    │       ├── infrastructure/                # [TẦNG 3: INFRASTRUCTURE - SECURITY & DB]
    │       │   ├── security/
    │       │   │   ├── BcryptPasswordEncoderAdapter.java
    │       │   │   ├── JwtTokenProvider.java
    │       │   │   ├── JwtAuthenticationFilter.java
    │       │   │   └── SecurityConfig.java
    │       │   ├── persistence/
    │       │   │   ├── UserJpaEntity.java
    │       │   │   ├── SpringDataJpaUserRepository.java
    │       │   │   └── UserRepositoryImpl.java
    │       │   └── DataInitializer.java      # Tự động tạo 3 tài khoản mẫu
    │       │
    │       └── presentation/                  # [TẦNG 4: PRESENTATION - API]
    │           ├── AuthController.java
    │           └── TaskExampleController.java # Minh họa phân quyền @PreAuthorize
    │
    └── test/java/com/example/taskmanagement/user/domain/
        └── UserTest.java                      # Unit Test ma trận quyền
```

---

## 4. Dữ liệu thử nghiệm có sẵn

Khi ứng dụng chạy, `DataInitializer` sẽ tự động khởi tạo 3 tài khoản mẫu:

| Tài khoản | Mật khẩu | Vai trò (Role) | Danh sách quyền (Permissions) |
| :--- | :--- | :--- | :--- |
| `admin` | `admin123` | **ADMIN** | `create_task`, `update_task`, `delete_task`, `assign_task`, `view_task` |
| `manager` | `manager123` | **MANAGER** | `create_task`, `update_task`, `assign_task`, `view_task` |
| `member` | `member123` | **MEMBER** | `view_task`, `update_own_task` |
