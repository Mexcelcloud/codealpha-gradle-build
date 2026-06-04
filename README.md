# codealpha-gradle-build

**QuickCart API — Build Engineering with Gradle**

> Part of the QuickCart DevOps Pipeline Series | CodeAlpha DevOps Internship — Task 3

---

## Business Context

QuickCart is a growing online grocery platform. As the engineering team expanded, a critical problem emerged:

> *"Every developer was building the application differently. Developer A compiled it one way. Developer B compiled it another. The output was inconsistent. Deployments were unpredictable. The business couldn't scale."*

This repository solves that problem by introducing a **standardised, automated build system** using Gradle.

The goal is not simply to write a Java application.  
The goal is to produce **one repeatable build process** that works identically across every developer, every machine, and every environment.

---

## What This Repository Demonstrates

| Concern | Without This | With This |
|---|---|---|
| Building the app | Manual `javac` commands, per developer | Single `gradle clean build` command |
| Dependencies | Managed manually, conflict-prone | Declared in `build.gradle`, auto-resolved |
| Testing | Skipped or run inconsistently | Automated on every build |
| Output | Inconsistent JARs | Versioned, reproducible artifact |
| Onboarding | Days of environment setup | Clone and build |

---

## Technology Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Application language |
| Spring Boot | 3.4.5 | REST API framework |
| Gradle | 9.5.1 | Build automation tool |
| JUnit 5 | (via Spring Boot) | Automated testing |
| Apache Tomcat | (embedded) | Web server (bundled inside JAR) |

---

## Project Structure

```
codealpha-gradle-build/
│
├── build.gradle                        # Build configuration (dependencies, plugins, tasks)
├── settings.gradle                     # Project name declaration
│
└── src/
    ├── main/
    │   ├── java/com/quickcart/
    │   │   ├── QuickCartApplication.java       # Application entry point
    │   │   ├── controller/
    │   │   │   └── ProductController.java      # REST API endpoints
    │   │   ├── service/
    │   │   │   └── ProductService.java         # Business logic layer
    │   │   └── model/
    │   │       └── Product.java                # Data model
    │   └── resources/
    │       └── application.properties          # Runtime configuration
    │
    └── test/
        └── java/com/quickcart/
            └── ProductServiceTest.java         # Automated unit tests
```

---

## Architecture

```
HTTP Request
     │
     ▼
ProductController        ← Handles incoming requests, returns responses
     │
     ▼
ProductService           ← Business logic (would connect to a database in production)
     │
     ▼
Product (Model)          ← Data structure representing a grocery product
```

This separation of concerns (Controller → Service → Model) is standard production architecture. Each layer has one responsibility. Changes in one layer do not break others.

---

## API Endpoints

### `GET /products`

Returns the QuickCart grocery product catalogue.

**Example Response:**
```json
[
  { "id": 1, "name": "Rice",   "price": 25.50 },
  { "id": 2, "name": "Beans",  "price": 15.00 },
  { "id": 3, "name": "Milk",   "price": 8.75  }
]
```

### `GET /health`

Returns API health status. Used by load balancers and monitoring tools to verify the application is running.

**Response:**
```
Application Healthy
```

---

## Prerequisites

| Requirement | Version | Download |
|---|---|---|
| Java JDK | 17 | https://adoptium.net |
| Gradle | 9.5.1 | https://gradle.org/install |

**Verify your installation:**
```bash
java -version
gradle -version
```

---

## Build Instructions

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/codealpha-gradle-build.git
cd codealpha-gradle-build
```

### 2. Build the project

```bash
gradle clean build
```

This single command:
- Resolves and downloads all dependencies from Maven Central
- Compiles all Java source files
- Runs all automated tests
- Packages the application into a deployable JAR

### 3. Verify the artifact was produced

```bash
ls build/libs/
```

Expected output:
```
quickcart-api-1.0.0.jar
quickcart-api-1.0.0-plain.jar
```

The file that matters for deployment is `quickcart-api-1.0.0.jar`.

---

## Running the Application

```bash
java -jar build/libs/quickcart-api-1.0.0.jar
```

Expected console output:
```
Tomcat started on port 8080 (http) with context path '/'
Started QuickCartApplication in 2.799 seconds
```

---

## Testing the Endpoints

Once the application is running, open a browser or use curl:

```bash
# Health check
curl http://localhost:8080/health

# Product catalogue
curl http://localhost:8080/products
```

Or open directly in any browser:
- http://localhost:8080/health
- http://localhost:8080/products

---

## Running Tests Only

```bash
gradle test
```

Test report is generated at:
```
build/reports/tests/test/index.html
```

---

## Understanding the Build Artifact

After a successful build, Gradle produces:

```
build/
└── libs/
    ├── quickcart-api-1.0.0.jar          ← Runnable JAR (deployable artifact)
    └── quickcart-api-1.0.0-plain.jar    ← Plain JAR (code only, not runnable)
```

### What is a JAR?

A JAR (Java ARchive) is a single file that contains:
- All compiled application code
- All dependency libraries
- Configuration files
- An embedded Tomcat web server
- A manifest telling Java which class to run

**Non-technical analogy:**  
Think of a JAR like an Amazon shipment box. All individual items (code, libraries, configs, web server) are packed into one container that can be shipped to any server and opened to run the application. No matter where you send it — development, staging, or production — the contents are identical.

### Why Version the Artifact?

The artifact is named `quickcart-api-1.0.0.jar` intentionally. In production:

- Every release produces a new versioned artifact
- Bad deployments can be rolled back to a previous version instantly
- The exact artifact running in production is always traceable

---

## How the Build Works

When `gradle clean build` is executed, the following happens in order:

```
gradle clean build
        │
        ▼
settings.gradle        → Identifies project name → quickcart-api
        │
        ▼
build.gradle
        │
        ├── plugins        → Loads tools (Java compiler, Spring Boot packager)
        ├── group/version  → Stamps identity on output (quickcart-api-1.0.0)
        ├── java           → Locks Java version (17)
        ├── repositories   → Points to Maven Central for dependency downloads
        ├── dependencies   → Downloads required libraries
        ├── compileJava    → Converts .java source files into bytecode
        ├── test           → Runs JUnit tests (quality gate)
        └── bootJar        → Packages everything into quickcart-api-1.0.0.jar
```

---

## Where This Fits in the QuickCart Pipeline

```
Developer writes code
        │
        ▼
[ This Repository ]
  gradle clean build
  → Dependencies resolved
  → Tests pass
  → quickcart-api-1.0.0.jar produced
        │
        ▼
  Jenkins picks up the artifact       ← Next: codealpha-jenkins-remoting
        │
        ▼
  Docker packages the artifact        ← Next: codealpha-docker-webserver
        │
        ▼
  Azure deploys to production         ← Next: codealpha-azure-cicd
```

---

## Build Tools Landscape

Gradle is one of several build tools that solve the same fundamental problem — transforming source code into deployable artifacts. The tool changes depending on the language and company. The concept is identical everywhere.

| Tool | Language | Config File | Used By |
|---|---|---|---|
| Gradle | Java | `build.gradle` | Android, Spring Boot projects |
| Maven | Java | `pom.xml` | Most enterprise Java companies |
| Ant | Java | `build.xml` | Legacy Java projects |
| npm | JavaScript | `package.json` | Node.js applications |
| pip | Python | `requirements.txt` | Python applications |
| Make | C/C++ | `Makefile` | Systems and DevOps tooling |

### Why This Matters for DevOps

A DevOps engineer does not specialise in one build tool. You specialise in the **concept**:

> *Declare dependencies → Define build steps → Produce a versioned artifact → Deliver reliably*

In your career you will encounter Java teams using Maven, JavaScript teams using npm, Python teams using pip. The pipeline you build around them follows the same pattern regardless of the tool sitting in the middle.

This repository uses Gradle — but the build engineering thinking applied here transfers directly to any language and any build tool you will encounter in production.

## Learning Outcomes

By completing this task demonstrated:

- Setting up a Gradle-based Java project from scratch
- Understanding the role of a build tool in a software delivery pipeline
- Structuring a Spring Boot application using layered architecture
- Writing automated unit tests that run as part of the build
- Producing a versioned, deployable JAR artifact
- Running a Spring Boot application and verifying live API endpoints
- Thinking about software builds as a business reliability problem

---

## Author

Built by **[DENNIS SUNDAY CHIMEZIE]** as part of the CodeAlpha DevOps Internship.  
This repository is Task 3 of a 4-part DevOps pipeline series built around a fictional 
grocery startup called QuickCart.

[GitHub Profile](https://github.com/Mexcelcloud)