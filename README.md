# 📇 Contact Manager Application

> **JUNIA ISEN - Java 2 Module Project**  
> A modern desktop application for managing contacts using JavaFX and SQLite.

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-17-blue)
![Maven](https://img.shields.io/badge/Maven-3.8+-red)
![SQLite](https://img.shields.io/badge/SQLite-3.51-green)

---

## 👥 Team Members

| Name | GitHub | Role |
|------|--------|------|
| **Aymeric Droulers** | [@AymericDroulers](https://github.com/AymericDroulers) | 
| **Salah Eddine Bitti** | [-](https://github.com/Salah-eddine-boudi)  |
| **Mahmoud Ali El Sayed** | |
| **Hekla Scheving** |  [-](https://github.com/Heklast) |

**Course:** Java 2 - 2026  
**Institution:** JUNIA ISEN Engineering School  
**Instructor:** Philippe Duval (philippe.duval@worldline.com)  


---

## 📋 Project Requirements

### Functional Requirements

✅ **Implemented:**
- List all persons from database
- Add new person with form validation
- Update existing person data (in-place editing)
- Real-time search by name
- Field validation (required fields, email format, phone format)

⏳ **In Progress:**
- Delete person functionality

### Non-Functional Requirements

✅ **Completed:**
- Maven project structure
- SQLite database integration
- Unit tests for DAO methods (9 tests)
- JavaFX GUI implementation
- MVC architecture
- Resource management (try-with-resources)

---

## 🎯 Features

### Core Features
- ✅ **CRUD Operations**: Create, Read, Update contacts
- ✅ **Search**: Real-time filtering by first name or last name
- ✅ **Validation**: 
  - Required fields (Last Name, First Name, Nickname)
  - Email format: `firstname.lastname@domain.com`
  - Phone format: 10 digits
- ✅ **In-place Editing**: Edit contacts directly in the main view
- ✅ **Persistent Storage**: SQLite database

### Technical Features
- ✅ **MVC Architecture**: Clear separation of concerns
- ✅ **DAO Pattern**: Data access abstraction
- ✅ **PreparedStatement**: SQL injection prevention
- ✅ **ObservableList**: Automatic UI updates
- ✅ **FilteredList**: Dynamic search functionality
- ✅ **Exception Handling**: User-friendly error messages

---

## 🛠️ Technologies Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21 | Core programming language |
| **JavaFX** | 17.0.6 | GUI framework |
| **SQLite JDBC** | 3.51.1.0 | Database connectivity |
| **Maven** | 3.8+ | Build & dependency management |
| **JUnit Jupiter** | 5.9.2 | Unit testing |
| **AssertJ** | 3.24.2 | Fluent test assertions |

---

## 📊 Database Schema
```sql
CREATE TABLE IF NOT EXISTS person (
    idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
    lastname VARCHAR(45) NOT NULL,  
    firstname VARCHAR(45) NOT NULL,
    nickname VARCHAR(45) NOT NULL,
    phone_number VARCHAR(15) NULL,
    address VARCHAR(200) NULL,
    email_address VARCHAR(150) NULL,
    birth_date DATE NULL
);
```

**Fields:**
- `idperson`: Auto-generated unique identifier
- `lastname`, `firstname`, `nickname`: **Required** fields
- `phone_number`, `address`, `email_address`, `birth_date`: **Optional** fields

---

## 🚀 Installation & Setup

### Prerequisites

Ensure you have the following installed:

- ✅ **JDK 21** or higher ([Download](https://www.oracle.com/java/technologies/downloads/))
- ✅ **Maven 3.8+** ([Download](https://maven.apache.org/download.cgi))
- ✅ **Git** (for cloning)

**Verify installation:**
```bash
java -version   # Should show Java 21.x.x
mvn -version    # Should show Maven 3.8.x or higher
```

### Installation Steps
```bash
# 1. Clone the repository
git clone https://github.com/AymericDroulers/ISEN-Java2-contactapp.git
cd ISEN-Java2-contactapp

# 2. Build the project
mvn clean install

# 3. Run the application
mvn javafx:run
```

**First Launch:**
- The application automatically creates `sqlite.db` if it doesn't exist
- Demo data (Aymeric Droulers) is inserted on first run
- Database is initialized in `App.java` using the `init()` method

---

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage

**9 comprehensive tests** covering:

| Test | Description |
|------|-------------|
| `shouldListPersons` | Verify listing all persons |
| `shouldCreatePerson` | Test person creation |
| `shouldUpdatePerson` | Test person update |
| `shouldUpdatePersonWithNullValues` | Test null handling in updates |
| `shouldUpdateOnlyTargetedPerson` | Verify update isolation |
| `shouldThrowExceptionWhenUpdatingNonExistentPerson` | Test error handling |
| `shouldUpdatePersonMultipleTimes` | Test multiple updates |
| `shouldGetPersonById` | Test retrieval by ID |
| `shouldDeletePerson` | Test deletion |

**All tests use:**
- Separate test database (`sqlitetest.db`)
- `@BeforeEach` for clean state
- Direct SQL verification (not relying on DAO for assertions)
- Try-with-resources for proper cleanup

---

## 📁 Project Structure
```
ISEN-Java2-contactapp/
│
├── src/
│   ├── main/
│   │   ├── java/isen/contactapp/
│   │   │   ├── App.java                    # Application entry point
│   │   │   │
│   │   │   ├── model/                      # Data layer (Model)
│   │   │   │   ├── Person.java             # POJO with validation
│   │   │   │   └── PersonDao.java          # Data Access Object (CRUD)
│   │   │   │
│   │   │   ├── view/                       # Presentation layer (Controller)
│   │   │   │   ├── MainPageController.java       # Main view logic
│   │   │   │   ├── CreatePersonController.java   # Create form logic
│   │   │   │   └── EditPersonController.java     # Edit form logic
│   │   │   │
│   │   │   └── util/                       # Utilities
│   │   │       ├── PersonValueFactory.java       # TableView cell factory
│   │   │       └── PersonChangeListener.java     # Selection listener
│   │   │
│   │   └── resources/isen/contactapp/view/
│   │       ├── Main-page.fxml              # Main UI layout (View)
│   │       ├── createPerson.fxml           # Create form UI
│   │       └── edit-person.fxml            # Edit form UI
│   │
│   └── test/
│       └── java/isen/contactapp/
│           └── PersonDaoTestCase.java      # DAO unit tests
│
├── pom.xml                                 # Maven configuration
├── .gitignore                              # Git ignore rules
├── README.md                               # This file
└── sqlite.db                               # SQLite database (gitignored)
```

---

## 🎨 Architecture

### MVC Pattern
```
┌─────────────────────────────────────────────────┐
│                    USER                         │
└────────────────────┬────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────┐
│              VIEW (FXML)                        │
│  • Main-page.fxml                               │
│  • createPerson.fxml                            │
│  • edit-person.fxml                             │
└────────────────────┬────────────────────────────┘
                     │ Events (clicks, input)
                     ▼
┌─────────────────────────────────────────────────┐
│           CONTROLLER (Java)                     │
│  • MainPageController                           │
│  • CreatePersonController                       │
│  • EditPersonController                         │
└────────────────────┬────────────────────────────┘
                     │ Business logic calls
                     ▼
┌─────────────────────────────────────────────────┐
│              MODEL (Java)                       │
│  • Person (POJO)                                │
│  • PersonDao (Database Access)                  │
└────────────────────┬────────────────────────────┘
                     │ JDBC
                     ▼
              ┌──────────────┐
              │   SQLite DB  │
              └──────────────┘
```

### Key Design Patterns

- **DAO (Data Access Object)**: `PersonDao` encapsulates all database operations
- **POJO (Plain Old Java Object)**: `Person` is a simple data container with validation
- **MVC (Model-View-Controller)**: Clear separation of data, presentation, and logic
- **Observer Pattern**: `ObservableList` for automatic UI updates
- **Factory Pattern**: `PersonValueFactory` for TableView cells

---

## 📖 User Guide

### Creating a Contact

1. Launch the application
2. Click **"Add New Person"** button
3. Fill in the form:
   - **Required**: Last Name, First Name, Nickname
   - **Optional**: Phone (10 digits), Address, Email, Birthday
4. Click **"Save"**
5. Success message appears and you return to main page

### Viewing Contacts

- All contacts are displayed in the left table
- Click on a contact to view details in the right panel

### Editing a Contact

**Method 1: In-place editing (Quick)**
1. Select a person from the list
2. Edit fields directly in the detail panel
3. Click **"Save"** to update

**Method 2: Edit page (Not implemented)**
- Future feature

### Searching Contacts

- Type in the **search bar** at the top
- Results filter automatically as you type
- Search works on both first name and last name
- Clear the search to show all contacts

### Deleting a Contact

⏳ **Coming soon** - Delete functionality is planned but not yet implemented

---

## 🔒 Data Validation

### Required Fields
- Last Name
- First Name  
- Nickname

### Optional Fields with Format Validation

**Email Address:**
- Format: `firstname.lastname@domain.extension`
- Example: `john.doe@gmail.com`
- Regex: `^[A-z]+\.[A-z]+@[A-z]+\.[A-z.]+$`

**Phone Number:**
- Format: Exactly 10 digits
- Example: `0612345678`
- Regex: `^\d{10}$`

**Birthday:**
- Cannot be in the future
- Uses DatePicker for easy selection

---




## 🎓 Learning Outcomes

This project demonstrates mastery of:

✅ **Java Fundamentals**
- Object-Oriented Programming (OOP)
- Exception handling
- Regular expressions
- Collections (List, ObservableList, FilteredList)
- Lambda expressions

✅ **JavaFX**
- FXML layouts
- Controllers and data binding
- TableView and form controls
- Event handling
- Alerts and dialogs
- MVC architecture

✅ **Database**
- JDBC connectivity
- CRUD operations
- PreparedStatement (SQL injection prevention)
- Transaction management
- SQLite integration

✅ **Testing**
- JUnit 5 unit tests
- Test isolation with @BeforeEach
- AssertJ fluent assertions
- Database testing strategies

✅ **Build Tools**
- Maven project structure
- Dependency management
- Build lifecycle
- Plugin configuration

✅ **Version Control**
- Git workflow
- .gitignore best practices
- Collaborative development
- Commit conventions

---

## 📚 Code Quality

### Best Practices Implemented

- ✅ **Try-with-resources** for all JDBC operations
- ✅ **PreparedStatement** to prevent SQL injection
- ✅ **Validation in layers** (UI, Model, DAO)
- ✅ **Encapsulation** (private fields, public methods)
- ✅ **JavaDoc comments** on public methods
- ✅ **Consistent naming** (camelCase, PascalCase)
- ✅ **Exception handling** with user-friendly messages
- ✅ **Resource cleanup** (connections, statements, result sets)
- ✅ **Separation of concerns** (MVC pattern)
- ✅ **No code duplication** (DRY principle)

### Code Metrics

- **Total Lines of Code**: ~1500
- **Test Coverage**: 9 unit tests
- **Classes**: 11 (Model: 2, View: 3, Util: 2, Test: 1, Main: 1)
- **FXML Files**: 3
- **Maven Dependencies**: 5

---

## 📞 Support & Contact

### Project Repository
- **GitHub**: [ISEN-Java2-contactapp](https://github.com/AymericDroulers/ISEN-Java2-contactapp)
- **Issues**: [Report a bug](https://github.com/AymericDroulers/ISEN-Java2-contactapp/issues)

### Team Contact
For questions about this project, contact any team member or create an issue on GitHub.

### Course Instructor
- **Name**: Philippe Duval
- **Email**: philippe.duval@worldline.com
- **Institution**: JUNIA ISEN

---

## 📄 License

**Educational Project** - JUNIA ISEN Java 2 Module 2026

This project is developed for educational purposes as part of the Java 2 curriculum.  
Not for commercial use.

---

## 🙏 Acknowledgments

- **JUNIA ISEN** for the comprehensive Java 2 curriculum
- **Philippe Duval** for guidance and project requirements
- **JavaFX Community** for excellent documentation
- **SQLite** for the lightweight database solution
- **Maven Central** for dependency management
- **JUnit & AssertJ** teams for testing frameworks

---

## 📝 Project Submission Details

**Submission Method**: GitHub Repository  
**Repository**: https://github.com/AymericDroulers/ISEN-Java2-contactapp  


**Submitted Files:**
- ✅ `pom.xml` - Maven configuration
- ✅ Java source files - All `.java` classes
- ✅ FXML resources - All `.fxml` layouts
- ✅ README.md - This documentation
- ✅ .gitignore - Git ignore rules
- ✅ SQL schema - Embedded in `App.java` init()

**NOT Submitted** (as per requirements):
- ❌ No `.zip` or `.rar` archives
- ❌ No compiled `.class` files (target/ excluded)
- ❌ No database files (*.db excluded)
- ❌ No IDE configuration files (.idea/, .settings/ excluded)

---

**Built with ☕ and 💻 by JUNIA ISEN students - 2026**

---
