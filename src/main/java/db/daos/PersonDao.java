package db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.Person;

public class PersonDao {
	
	// Here we decided that it's a better idea for a PersonDao to be a singleton as there is no need to have multiple instances of it
	// This class is also stateless so it's a good candidate for a singleton
	
	// Double-Checked Locking Singleton might be an overkill but we like the syntax more than the Bill Pugh used in previous classes
	
    private static volatile PersonDao instance;

    // Private constructor to prevent instantiation from other classes
    private PersonDao() {}

    // Public method to get the single instance of the class
    public static PersonDao getInstance() {
        if (instance == null) {
            synchronized (PersonDao.class) {
                if (instance == null) {
                    instance = new PersonDao();
                }
            }
        }
        return instance;
    }

	public List<Person> getPersons() {
		
		List<Person> listOfPersons = new ArrayList<>();
		
		try (Connection connection =  DataSourceFactory.getConnection()) {
			try(Statement statement = connection.createStatement()) {
				try(ResultSet results = statement.executeQuery("SELECT * FROM person")) {
					while(results.next()) {
						Person person = new Person(results.getInt("idperson"),
												results.getString("lastname"),
												results.getString("firstname"),
												results.getString("nickname"),
												results.getString("phone_number"),
												results.getString("adress"),
												results.getString("email_adress"),
												results.getDate("birth_date").toLocalDate(),
												results.getString("picture_path"));
						listOfPersons.add(person);
					}
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return listOfPersons;
	}
	
	public Person addPerson(Person person) {
		try (Connection connection =  DataSourceFactory.getConnection()) {
			String sqlQuery = "INSERT INTO person(lastname,firstname,nickname,phone_number,adress,email_adress,birth_date,picture_path) VALUES(?,?,?,?,?,?,?,?)";
			try(PreparedStatement statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, person.getLastname());
				statement.setString(2, person.getFirstname());
				statement.setString(3, person.getNickname());
				statement.setString(4, person.getPhone_number());
				statement.setString(5, person.getAdress());
				statement.setString(6, person.getEmail_adress());
				statement.setDate(7, java.sql.Date.valueOf(person.getBirth_date()));
				statement.setString(8, person.getPicture_path());
				statement.executeUpdate();
				ResultSet ids = statement.getGeneratedKeys();
				if (ids.next()) {
					return new Person(ids.getInt(1), person.getLastname(), person.getFirstname(),
							person.getNickname(), person.getPhone_number(), person.getAdress(), person.getEmail_adress(), person.getBirth_date(), person.getPicture_path());
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Person updatePerson(Person person) {
        String sqlQuery = "UPDATE person SET lastname = ?, firstname = ?, nickname = ?, phone_number = ?, adress = ?, email_adress = ?, birth_date = ?, picture_path = ? WHERE idperson = ?";
        try (Connection connection = DataSourceFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, person.getLastname());
            statement.setString(2, person.getFirstname());
            statement.setString(3, person.getNickname());
            statement.setString(4, person.getPhone_number());
            statement.setString(5, person.getAdress());
            statement.setString(6, person.getEmail_adress());
            statement.setDate(7, java.sql.Date.valueOf(person.getBirth_date()));
            statement.setString(8, person.getPicture_path());
            statement.setInt(9, person.getIdperson());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
	
    public Person deletePerson(Person person) {
        String sqlQuery = "DELETE FROM person WHERE idperson = ?";
        try (Connection connection = DataSourceFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setInt(1, person.getIdperson());
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
	
}
