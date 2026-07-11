# Phase 01 – Platform Foundation

## Overview

Phase 01 establishes the foundation of the Enterprise AI Knowledge Platform.

This phase focuses entirely on building the platform infrastructure required for all future phases. No AI, RAG, document ingestion, vector databases, or LLM integration are implemented during this phase.

The objective is to create a secure, scalable, and enterprise-ready platform capable of managing users, organizational structure, workspaces, and authorization.

At the completion of this phase, the application should function as an enterprise collaboration platform where employees can authenticate, join workspaces, and access only the resources they are authorized to use.

---

# Objectives

- Build a secure authentication system using JWT Access Tokens and Refresh Tokens.
- Implement Role-Based Access Control (RBAC).
- Model the organization's structure.
- Introduce workspaces as the primary collaboration and knowledge boundary.
- Design the backend using an API-first approach.
- Create a scalable database schema for future RAG features.

---

# Scope

## Included

- Authentication
- Authorization
- User Management
- Department Management
- Workspace Management
- Workspace Membership
- Swagger / OpenAPI Documentation
- PostgreSQL Database Schema
- Dockerized Development Environment

---

## Excluded

The following features are intentionally deferred to later phases.

- Document Upload
- Knowledge Sources
- GitHub Integration
- Confluence Integration
- Jira Integration
- Google Drive Integration
- Chunking
- Embedding Generation
- Vector Database
- Semantic Search
- RAG Pipeline
- LLM Integration
- Chat Interface
- Conversation History

---

# Architecture Overview

The platform separates organizational hierarchy from collaboration.

```
Departments
        │
        ▼
      Users
        │
        ▼
Workspace Membership
        │
        ▼
    Workspaces
```

Departments represent organizational structure.

Workspaces represent products, projects, or collaboration spaces.

Future knowledge retrieval will be scoped using workspaces instead of departments.

---

# Authentication

Authentication is based on JWT.

Flow:

```
Login

↓

Access Token

↓

Protected APIs

↓

Refresh Token

↓

New Access Token
```

## User Registration

The platform does **not** support public registration.

Users are created only by the SUPER_ADMIN.

Every newly created user receives:

- Temporary Password
- `mustChangePassword = true`

During the first login, users are redirected to change their password before accessing the platform.

---

# Authorization

Authorization consists of two independent layers.

## System Roles

System roles define permissions across the platform.

Current roles:

- SUPER_ADMIN
- USER

### SUPER_ADMIN

Responsible for:

- User Management
- Department Management
- Workspace Management
- Workspace Administration
- Platform Configuration

### USER

Represents every employee.

Users only access resources they are authorized to use.

---

## Workspace Roles

Workspace roles define permissions inside a workspace.

Current roles:

- ADMIN
- MEMBER

Example:

| User | Workspace | Role |
|------|-----------|------|
| Alice | AI Platform | ADMIN |
| Alice | Payment Gateway | MEMBER |

A user may have different roles in different workspaces.

---

# Organizational Model

## Departments

Departments represent the company's organizational hierarchy.

Examples:

- Engineering
- HR
- Finance
- Product
- DevOps

Rules:

- Every user belongs to exactly one department.
- Departments cannot be deleted.
- Disabled departments cannot receive new employees.

Departments exist for organizational purposes only.

They are **not** used for RAG search.

---

## Workspaces

A workspace represents a product, project, or collaboration space.

Examples:

- AI Platform
- Payment Gateway
- CRM
- Company Knowledge

A workspace becomes the primary boundary for:

- Collaboration
- Authorization
- Knowledge
- Future RAG Search

Every workspace will eventually contain:

- Members
- Documents
- Knowledge Sources
- Chunks
- Embeddings

Only membership is implemented during Phase 01.

---

# Workspace Membership

Workspace membership controls collaboration.

Users may belong to multiple workspaces.

Every member has a workspace role.

Example:

```
AI Platform

John      ADMIN

Alice     MEMBER

David     MEMBER
```

Business Rules:

- Users cannot join the same workspace twice.
- Every workspace must always contain at least one ADMIN.
- The last ADMIN cannot be removed.
- The last ADMIN cannot leave the workspace.
- The last ADMIN cannot be downgraded.

Workspace membership will become the authorization boundary for document retrieval in future phases.

---

# Database Schema

Phase 01 introduces the following entities.

- User
- Role
- UserRole
- Department
- Workspace
- WorkspaceRole
- WorkspaceMember

Document-related entities are intentionally excluded until Phase 02.

---

# API Design

The project follows an API-first development approach.

All REST endpoints are designed before implementation.

OpenAPI Specification:

```
docs/api/phase-1.yaml
```

Modules:

- Authentication
- Users
- Departments
- Workspaces
- Workspace Membership

---

# Security

Authentication:

- JWT Access Token
- Refresh Token

Authorization:

- Role-Based Access Control (RBAC)

Future phases will extend authorization to workspace-level document permissions.

---

# Business Rules

## Users

- Users cannot self-register.
- SUPER_ADMIN creates users.
- New users receive temporary passwords.
- Password change is mandatory during first login.
- Disabled users cannot authenticate.

---

## Departments

- Every user belongs to one department.
- Departments cannot be deleted.
- Disabled departments cannot receive new users.

---

## Workspaces

- Workspaces represent collaboration spaces.
- Workspaces cannot be deleted.
- Disabled workspaces cannot receive new members.

---

## Membership

- Users may belong to multiple workspaces.
- Membership controls access.
- Workspace ADMIN manages workspace members.
- SUPER_ADMIN can manage every workspace.

---

# Deliverables

The following should be completed during Phase 01.

## Authentication

- JWT Authentication
- Refresh Tokens
- Password Change Flow
- Logout
- Current User API

## User Management

- Create User
- Update User Profile
- Department Transfer
- Role Assignment
- Enable / Disable User

## Department Management

- Create Department
- Update Department
- Enable / Disable Department
- List Department Users

## Workspace Management

- Create Workspace
- Update Workspace
- Enable / Disable Workspace

## Workspace Membership

- Add Members
- Remove Members
- Change Workspace Roles
- List Workspace Members
- Leave Workspace

## Infrastructure

- PostgreSQL
- Docker Compose
- Swagger UI
- OpenAPI Documentation

---

# Expected Outcome

A company administrator should be able to perform the following workflow.

```
Bootstrap SUPER_ADMIN

↓

Login

↓

Create Departments

↓

Create Users

↓

Create Workspaces

↓

Assign Workspace Admins

↓

Add Members

↓

Employees Login

↓

Change Password

↓

Employees Access Assigned Workspaces
```

At the completion of this phase, the platform functions as a secure enterprise collaboration system.

The system is fully prepared for introducing document management and Retrieval-Augmented Generation (RAG) in the next phase.

---

# Success Criteria

Phase 01 is considered complete when:

- All APIs defined in `docs/api/phase-1.yaml` are implemented.
- JWT authentication is fully functional.
- Refresh Token flow works correctly.
- Password change flow is enforced.
- Authorization rules are implemented.
- Users, departments, and workspaces are fully manageable.
- Workspace membership is operational.
- Database schema is stable.
- Swagger documentation is complete.
- Docker Compose starts the application successfully.
- Integration tests pass for all Phase 01 endpoints.

---

# Next Phase

**Phase 02 – Document Management & Knowledge Ingestion**

This phase introduces:

- Manual document upload
- Document metadata
- Workspace document management
- Document processing pipeline
- Knowledge source abstraction

These capabilities will establish the foundation for the Retrieval-Augmented Generation (RAG) pipeline introduced in later phases.