package pl.symentis.junit5.flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.*;

@Testcontainers
public class FlywayExtensionIT {

    @Container
    public PostgreSQLContainer<?> db = new PostgreSQLContainer<>();

    @RegisterExtension
    public FlywayInTestcontainersExtension flywayMigration = new FlywayInTestcontainersExtension();

    @Test
    void test_can_read_username_from_db() throws Exception {
        try (Connection connection = DriverManager
                .getConnection(db.getJdbcUrl(),
                        db.getUsername(),
                        db.getPassword())) {

            // when
            ResultSet resultSet = connection
                    .createStatement()
                    .executeQuery("SELECT * FROM users WHERE id = 1");

            // then
            boolean hasMoreResults = resultSet.next();
            assertTrue(hasMoreResults);
            String name = resultSet.getString("name");
            assertEquals("TestUser", name);
        }
    }
}
