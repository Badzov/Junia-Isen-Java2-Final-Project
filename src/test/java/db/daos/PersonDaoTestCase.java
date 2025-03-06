package db.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import models.Person;

public class PersonDaoTestCase {

    private PersonDao personDao = PersonDao.getInstance();

    @BeforeEach
    public void initDb() throws Exception {
        Connection connection = DataSourceFactory.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS person (\r\n"
                + "  idperson INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n"
                + "  lastname VARCHAR(45) NOT NULL,\r\n"
                + "  firstname VARCHAR(45) NOT NULL,\r\n"
                + "  nickname VARCHAR(45) NOT NULL,\r\n"
                + "  phone_number VARCHAR(15) NULL,\r\n"
                + "  adress VARCHAR(200) NULL,\r\n"
                + "  email_adress VARCHAR(150) NULL,\r\n"
                + "  birth_date DATE NULL,\r\n"
                + "  picture_path TEXT NULL\r\n" 
                + ");");
        stmt.executeUpdate("DELETE FROM person");
        stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='person'");
        stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, adress, email_adress, birth_date, picture_path) "
                + "VALUES (1, 'Doe', 'John', 'Johnny', '123456789', '123 Main St', 'john.doe@example.com', '1990-01-01 00:00:00.000', 'images/john_doe.jpg')");
        stmt.executeUpdate("INSERT INTO person(idperson, lastname, firstname, nickname, phone_number, adress, email_adress, birth_date, picture_path) "
                + "VALUES (2, 'Smith', 'Jane', 'Janey', '987654321', '456 Elm St', 'jane.smith@example.com', '1985-05-15 00:00:00.000', 'images/jane_smith.jpg')");
        stmt.close();
        connection.close();
    }

    @Test
    public void shouldListPersons() {
        // WHEN
        List<Person> persons = personDao.getPersons();
        // THEN
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting(
                Person::getIdperson,
                Person::getLastname,
                Person::getFirstname,
                Person::getNickname,
                Person::getPhone_number,
                Person::getAdress,
                Person::getEmail_adress,
                Person::getBirth_date,
                Person::getPicture_path).containsOnly(
                tuple(1, "Doe", "John", "Johnny", "123456789", "123 Main St", "john.doe@example.com", LocalDate.parse("1990-01-01"), "images/john_doe.jpg"),
                tuple(2, "Smith", "Jane", "Janey", "987654321", "456 Elm St", "jane.smith@example.com", LocalDate.parse("1985-05-15"), "images/jane_smith.jpg")
                );
    }

    @Test
    public void shouldAddPerson() throws Exception {
        // GIVEN
        Person person = new Person("Brown", "Michael", "Mike", "555555555", "789 Oak St", "michael.brown@example.com",
        		LocalDate.parse("1975-10-10"), "images/michael_brown.jpg"); 
        // WHEN
        Person addedPerson = personDao.addPerson(person);
        // THEN
        assertThat(addedPerson).isNotNull();
        assertThat(addedPerson.getIdperson()).isNotNull();
        assertThat(addedPerson.getLastname()).isEqualTo("Brown");
        assertThat(addedPerson.getFirstname()).isEqualTo("Michael");
        assertThat(addedPerson.getNickname()).isEqualTo("Mike");
        assertThat(addedPerson.getPhone_number()).isEqualTo("555555555");
        assertThat(addedPerson.getAdress()).isEqualTo("789 Oak St");
        assertThat(addedPerson.getEmail_adress()).isEqualTo("michael.brown@example.com");
        assertThat(addedPerson.getBirth_date()).isEqualTo(LocalDate.parse("1975-10-10"));
        assertThat(addedPerson.getPicture_path()).isEqualTo("images/michael_brown.jpg");

        // Verify the person was added to the database
        Connection connection = DataSourceFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE lastname='Brown'");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getInt("idperson")).isEqualTo(addedPerson.getIdperson());
        assertThat(resultSet.getString("lastname")).isEqualTo("Brown");
        assertThat(resultSet.getString("firstname")).isEqualTo("Michael");
        assertThat(resultSet.getString("nickname")).isEqualTo("Mike");
        assertThat(resultSet.getString("phone_number")).isEqualTo("555555555");
        assertThat(resultSet.getString("adress")).isEqualTo("789 Oak St");
        assertThat(resultSet.getString("email_adress")).isEqualTo("michael.brown@example.com");
        assertThat(resultSet.getDate("birth_date").toLocalDate()).isEqualTo(LocalDate.parse("1975-10-10"));
        assertThat(resultSet.getString("picture_path")).isEqualTo("images/michael_brown.jpg");
        assertThat(resultSet.next()).isFalse();
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldUpdatePerson() throws Exception {
        // GIVEN
        Person person = new Person(1, "UpdatedDoe", "UpdatedJohn", "UpdatedJohnny", "999999999", "456 Updated St",
        		"updated.john.doe@example.com", LocalDate.parse("1995-01-01"), "images/updated_john_doe.jpg");
        // WHEN
        Person updatedPerson = personDao.updatePerson(person);
        // THEN
        assertThat(updatedPerson).isNotNull();
        assertThat(updatedPerson.getIdperson()).isEqualTo(1);
        assertThat(updatedPerson.getLastname()).isEqualTo("UpdatedDoe");
        assertThat(updatedPerson.getFirstname()).isEqualTo("UpdatedJohn");
        assertThat(updatedPerson.getNickname()).isEqualTo("UpdatedJohnny");
        assertThat(updatedPerson.getPhone_number()).isEqualTo("999999999");
        assertThat(updatedPerson.getAdress()).isEqualTo("456 Updated St");
        assertThat(updatedPerson.getEmail_adress()).isEqualTo("updated.john.doe@example.com");
        assertThat(updatedPerson.getBirth_date()).isEqualTo(LocalDate.parse("1995-01-01"));
        assertThat(updatedPerson.getPicture_path()).isEqualTo("images/updated_john_doe.jpg");

        // Verify the person was updated in the database
        Connection connection = DataSourceFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=1");
        assertThat(resultSet.next()).isTrue();
        assertThat(resultSet.getString("lastname")).isEqualTo("UpdatedDoe");
        assertThat(resultSet.getString("firstname")).isEqualTo("UpdatedJohn");
        assertThat(resultSet.getString("nickname")).isEqualTo("UpdatedJohnny");
        assertThat(resultSet.getString("phone_number")).isEqualTo("999999999");
        assertThat(resultSet.getString("adress")).isEqualTo("456 Updated St");
        assertThat(resultSet.getString("email_adress")).isEqualTo("updated.john.doe@example.com");
        assertThat(resultSet.getDate("birth_date").toLocalDate()).isEqualTo(LocalDate.parse("1995-01-01"));
        assertThat(resultSet.getString("picture_path")).isEqualTo("images/updated_john_doe.jpg");
        assertThat(resultSet.next()).isFalse();
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        // GIVEN
        Person person = new Person();
        person.setIdperson(1); 
        // WHEN
        Person deletedPerson = personDao.deletePerson(person);
        // THEN
        assertThat(deletedPerson).isNotNull();
        assertThat(deletedPerson.getIdperson()).isEqualTo(1);

        // Verify the person was deleted from the database
        Connection connection = DataSourceFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM person WHERE idperson=1");
        assertThat(resultSet.next()).isFalse(); // The person should no longer exist
        resultSet.close();
        statement.close();
        connection.close();
    }
}