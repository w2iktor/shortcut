package pl.symentis.junit5.flyway;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionConfigurationException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.JdbcDatabaseContainer;

public class FlywayMigration implements BeforeEachCallback{

  private static final Logger LOG = LoggerFactory.getLogger(FlywayMigration.class);
  
  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Optional<Object> instance = context.getTestInstance();
    
    JdbcDatabaseContainer<?> databaseContainer = instance
      .map(Object::getClass)
      .map(Class::getFields)
      .flatMap(
          fields -> Arrays.stream(fields).filter(field -> JdbcDatabaseContainer.class.isAssignableFrom(field.getType())).findFirst()
      )
      .flatMap(field -> getField(field, instance.get()))
      .map(JdbcDatabaseContainer.class::cast)
      // this is not the best thing we can do
      // we should use namespace store
      // but TestContainers made it protected for whatever reason
      .orElseThrow(()-> new ExtensionConfigurationException("cannot find JdbcDatabaseContainer field in test instance or it is not set"));
        
    FluentConfiguration configuration = Flyway.configure();
    configuration.dataSource(databaseContainer.getJdbcUrl(), databaseContainer.getUsername(), databaseContainer.getPassword());
    Flyway flyway = new Flyway(configuration);
    flyway.migrate();

  }
  
  private static Optional<Object> getField(Field field, Object instance){
    try {
      return Optional.ofNullable(field.get(instance));
    } catch (IllegalArgumentException | IllegalAccessException e) {
      LOG.error("cannot get field {} from test instance {}", field, instance, e);
      return Optional.empty();
    }
  }
 
}
