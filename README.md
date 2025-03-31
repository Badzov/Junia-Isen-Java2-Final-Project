# JavaFX Contact Manager

![Java](https://img.shields.io/badge/Java-17-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-19-orange)
![SQLite](https://img.shields.io/badge/SQLite-3-lightgrey)
![JUnit5](https://img.shields.io/badge/JUnit-5-brightgreen)

A complete contact management solution with database integration and comprehensive testing.

## Features

- **Full CRUD operations** with SQLite backend
- **Image upload and storage system**
- **Real-time search functionality**
- **Input validation** and error handling
- **Responsive JavaFX UI**

## Testing

The project includes comprehensive testing for data access layers and business logic.

### Testing Approach

- **Data Access Layer Tests**:
  - CRUD operation verification
  - Database state assertions
  - Isolation with `@BeforeEach` reset

- **Test Frameworks**:
  - **JUnit 5** for test cases
  - **AssertJ** for fluent assertions

- **Test Coverage**:
  - All **DAO methods**
  - **Edge cases**
  - **Data integrity checks**

### Example Test Case

```java
@Test
public void shouldUpdatePerson() {
    Person person = new Person(1, "Updated", "Contact", ...);
    Person updated = personDao.updatePerson(person);
    
    assertThat(updated.getLastname()).isEqualTo("Updated");
    // Plus database state verification
}
```

## Technical Details

### Design Patterns

- **Singleton** for services
- **Factory** for database connections
- **MVC** architecture for separation of concerns

### Database

- Automatic schema initialization
- **Prepared statements** for security
- Proper **connection handling**

### Code Quality

- Clear separation of concerns
- Comprehensive **input validation**
- Clean **JavaDoc** comments for maintainability

## Requirements

- **Java 17+**


## Development Notes

- Pure **JavaFX** (no external UI libraries)
- Manual **dependency management**
- Focus on **clean architecture**
- Emphasis on **testability**

---
