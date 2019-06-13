package pl.symentis.junit5.flyway;

import static org.junit.Assert.assertFalse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import pl.symentis.junit5.flyway.FlywayMigration;

@Testcontainers
public class IntegrationTest {

  
  @Container
  public PostgreSQLContainer<?> db = new PostgreSQLContainer<>();

  @RegisterExtension
  public FlywayMigration flywayMigration = new FlywayMigration();
  
  @Test
  void usersTableExists() throws Exception {
    try (Connection connection = DriverManager.getConnection(db.getJdbcUrl(), db.getUsername(), db.getPassword());
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");) {
      assertFalse(resultSet.next());
    }
  }
}
