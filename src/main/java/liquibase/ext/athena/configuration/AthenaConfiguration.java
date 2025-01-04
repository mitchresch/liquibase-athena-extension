package liquibase.ext.athena.configuration;

import liquibase.configuration.ConfigurationDefinition;
import liquibase.configuration.AutoloadedConfigurations;

public class AthenaConfiguration implements AutoloadedConfigurations {

    public static final ConfigurationDefinition<String> LIQUIBASE_S3_TABLES_LOCATION;

    static {
        ConfigurationDefinition.Builder builder = new ConfigurationDefinition.Builder("liquibase.athena");

        LIQUIBASE_S3_TABLES_LOCATION = builder.define("liquibaseS3TablesLocation", String.class)
            .setDescription("S3 Location where Iceberg Tables will be stored")
            .build();
    }

    public static String getS3TablesLocation() {
        return LIQUIBASE_S3_TABLES_LOCATION.getCurrentValue();
    }
}
