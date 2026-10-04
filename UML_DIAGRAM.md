# TÀI LIỆU THIẾT KẾ UML - NGƯỜI 6 (USER & AUTH SUBDOMAIN)

> **Dự án:** Task Management System  
> **Kiến trúc:** Domain-Driven Design (DDD) & Clean Architecture  
> **Phạm vi tài liệu:** Module Người 6 phụ trách (User, Role, Permission, Authentication, Authorization, Assignment)

---

## 1. Biểu đồ Ca sử dụng (Use Case Diagram)

Biểu đồ này thể hiện phân cấp quyền hạn giữa các tác nhân (**Guest**, **Member**, **Manager**, **Admin**) theo đúng ma trận phân quyền đề bài giao:

```mermaid
flowchart LR
    Guest["Khách / Chưa đăng nhập"]
    Member["Thành viên (Member)"]
    Manager["Quản lý (Manager)"]
    Admin["Quản trị viên (Admin)"]

    subgraph AuthContext["Hệ thống Xác thực (AuthN)"]
        UC_Login(["Đăng nhập (Login)"])
        UC_Register(["Đăng ký (Register)"])
        UC_ViewMe(["Xem thông tin cá nhân (/me)"])
    end

    subgraph TaskPermissions["Quyền hạn trên Task (AuthZ)"]
        UC_ViewTask(["Xem danh sách Task (view_task)"])
        UC_UpdateOwn(["Cập nhật task của mình (update_own_task)"])
        UC_CreateTask(["Tạo Task mới (create_task)"])
        UC_UpdateTask(["Cập nhật mọi Task (update_task)"])
        UC_AssignTask(["Phân công Task (assign_task)"])
        UC_DeleteTask(["Xoá Task (delete_task)"])
    end

    Guest --> UC_Login
    Guest --> UC_Register

    Member --> UC_ViewMe
    Member --> UC_ViewTask
    Member --> UC_UpdateOwn

    Manager --> UC_ViewMe
    Manager --> UC_ViewTask
    Manager --> UC_CreateTask
    Manager --> UC_UpdateTask
    Manager --> UC_AssignTask

    Admin --> UC_ViewMe
    Admin --> UC_ViewTask
    Admin --> UC_CreateTask
    Admin --> UC_UpdateTask
    Admin --> UC_AssignTask
    Admin --> UC_DeleteTask
```

---

## 2. Biểu đồ Lớp (Class Diagram) - Chuẩn Clean Architecture & DDD

Tách bạch 4 tầng kiến trúc: **Domain**, **Application**, **Infrastructure**, **Presentation**.

```mermaid
classDiagram
    %% TẦNG DOMAIN (LÕI NGHIỆP VỤ)
    namespace Domain_Layer {
        class Permission {
            <<enumeration>>
            CREATE_TASK
            UPDATE_TASK
            DELETE_TASK
            ASSIGN_TASK
            VIEW_TASK
            UPDATE_OWN_TASK
            +getValue() String
        }

        class Role {
            <<enumeration>>
            ADMIN
            MANAGER
            MEMBER
            -Set~Permission~ permissions
            +getPermissions() Set~Permission~
        }

        class User {
            -UUID id
            -String username
            -String email
            -String passwordHash
            -Role role
            -boolean active
            +hasPermission(Permission) boolean
            +canBeAssignedTask() boolean
            +activate() void
            +deactivate() void
            +changeRole(Role) void
        }

        class UserRepository {
            <<interface>>
            +findById(UUID) Optional~User~
            +findByUsername(String) Optional~User~
            +findByEmail(String) Optional~User~
            +save(User) User
            +existsByUsername(String) boolean
            +existsByEmail(String) boolean
        }
    }

    %% TẦNG APPLICATION (USE CASES & PORTS)
    namespace Application_Layer {
        class PasswordEncoderPort {
            <<interface>>
            +encode(String) String
            +matches(String, String) boolean
        }

        class TokenProviderPort {
            <<interface>>
            +generateToken(User) String
            +extractUserId(String) UUID
            +validateToken(String) boolean
        }

        class AuthenticateUserUseCase {
            -UserRepository userRepository
            -PasswordEncoderPort passwordEncoder
            -TokenProviderPort tokenProvider
            +execute(LoginCommand) AuthTokenResponse
        }

        class RegisterUserUseCase {
            -UserRepository userRepository
            -PasswordEncoderPort passwordEncoder
            +execute(RegisterUserCommand) UserResponse
        }
    }

    %% TẦNG INFRASTRUCTURE (SECURITY & DATABASE)
    namespace Infrastructure_Layer {
        class BcryptPasswordEncoderAdapter {
            -PasswordEncoder passwordEncoder
            +encode(String) String
            +matches(String, String) boolean
        }

        class JwtTokenProvider {
            -Key key
            -long expiration
            +generateToken(User) String
            +extractUsername(String) String
            +extractRole(String) String
            +extractPermissions(String) List~String~
        }

        class UserRepositoryImpl {
            -SpringDataJpaUserRepository jpaRepository
            +findById(UUID) Optional~User~
            +save(User) User
        }

        class UserJpaEntity {
            -UUID id
            -String username
            -String email
            -String passwordHash
            -Role role
            -boolean active
        }
    }

    %% TẦNG PRESENTATION (CONTROLLER)
    namespace Presentation_Layer {
        class AuthController {
            -RegisterUserUseCase registerUserUseCase
            -AuthenticateUserUseCase authenticateUserUseCase
            +register(RegisterUserCommand) ResponseEntity
            +login(LoginCommand) ResponseEntity
            +getCurrentUserInfo(Authentication) ResponseEntity
        }
    }

    %% MỐI QUAN HỆ GIỮA CÁC LỚP
    Role "1" *-- "many" Permission : contains
    User "1" *-- "1" Role : has

    AuthenticateUserUseCase ..> UserRepository : uses
    AuthenticateUserUseCase ..> PasswordEncoderPort : uses
    AuthenticateUserUseCase ..> TokenProviderPort : uses

    RegisterUserUseCase ..> UserRepository : uses
    RegisterUserUseCase ..> PasswordEncoderPort : uses

    BcryptPasswordEncoderAdapter ..|> PasswordEncoderPort : implements
    JwtTokenProvider ..|> TokenProviderPort : implements
    UserRepositoryImpl ..|> UserRepository : implements
    UserRepositoryImpl ..> UserJpaEntity : maps to/from

    AuthController ..> AuthenticateUserUseCase : calls
    AuthController ..> RegisterUserUseCase : calls
```

---

## 3. Biểu đồ Tuần tự (Sequence Diagram)

### Luồng 1: Đăng nhập và Nhận Token JWT (Authentication Flow)
Mô tả quy trình người dùng gửi thông tin đăng nhập, hệ thống kiểm tra mật khẩu đã mã hóa và sinh JWT Token chứa danh sách quyền hạn.

```mermaid
sequenceDiagram
    autonumber
    actor Client as Người dùng (Client)
    participant AuthCtrl as AuthController
    participant AuthUC as AuthenticateUserUseCase
    participant UserRepo as UserRepository
    participant Hasher as PasswordEncoderPort
    participant JWT as TokenProviderPort

    Client->>AuthCtrl: POST /api/v1/auth/login (username, password)
    AuthCtrl->>AuthUC: execute(LoginCommand)
    AuthUC->>UserRepo: findByUsername(username)
    UserRepo-->>AuthUC: Return Optional<User>

    alt Không tìm thấy User hoặc User bị vô hiệu hóa
        AuthUC-->>AuthCtrl: Throw Exception (401/403)
        AuthCtrl-->>Client: Trả về lỗi xác thực
    else Tìm thấy User
        AuthUC->>Hasher: matches(rawPassword, passwordHash)
        alt Sai mật khẩu
            AuthUC-->>AuthCtrl: Throw Exception (401 Bad Credentials)
            AuthCtrl-->>Client: Trả về 401 Unauthorized
        else Đúng mật khẩu
            AuthUC->>JWT: generateToken(user)
            Note over JWT: Nhúng claims: user_id, role, permissions
            JWT-->>AuthUC: Trả về accessToken (JWT String)
            AuthUC-->>AuthCtrl: Return AuthTokenResponse
            AuthCtrl-->>Client: 200 OK (accessToken, role, username)
        end
    end
```

---

### Luồng 2: Xác thực & Phân quyền khi gọi API được bảo vệ (Authorization Flow)
Minh họa khi Người dùng gọi API bảo vệ (ví dụ: `DELETE /api/v1/tasks/{id}`) có gắn `@PreAuthorize("hasAuthority('delete_task')")`.

```mermaid
sequenceDiagram
    autonumber
    actor Client as Người dùng (Client)
    participant JwtFilter as JwtAuthenticationFilter
    participant JwtProvider as JwtTokenProvider
    participant SecCtx as SecurityContextHolder
    participant TaskCtrl as TaskController (Người 5)
    participant TaskUC as TaskUseCase (Người 3)

    Client->>JwtFilter: Request + Header "Authorization: Bearer <token>"
    JwtFilter->>JwtProvider: validateToken(token)

    alt Token không hợp lệ hoặc hết hạn
        JwtFilter-->>Client: 401 Unauthorized
    else Token hợp lệ
        JwtFilter->>JwtProvider: extractPermissions(token) & extractRole(token)
        JwtProvider-->>JwtFilter: Trả về List<GrantedAuthority> (delete_task, ROLE_ADMIN, ...)
        JwtFilter->>SecCtx: setAuthentication(UsernamePasswordAuthenticationToken)
        JwtFilter->>TaskCtrl: Forward request vào Controller

        Note over TaskCtrl: Kiểm tra @PreAuthorize("hasAuthority('delete_task')")
        alt User không có quyền 'delete_task' (Manager hoặc Member)
            TaskCtrl-->>Client: 403 Forbidden (Không đủ quyền)
        else User có quyền 'delete_task' (Admin)
            TaskCtrl->>TaskUC: Thực thi xoá Task
            TaskUC-->>TaskCtrl: Kết quả thành công
            TaskCtrl-->>Client: 200 OK
        end
    end
```
