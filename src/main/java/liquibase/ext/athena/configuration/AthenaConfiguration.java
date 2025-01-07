package liquibase.ext.athena.configuration;

import liquibase.configuration.ConfigurationDefinition;
import liquibase.configuration.AutoloadedConfigurations;

public class AthenaConfiguration implements AutoloadedConfigurations {

    public static final ConfigurationDefinition<String> LIQUIBASE_S3_TABLES_LOCATION;
    public static final ConfigurationDefinition<Boolean> S3_SKIP_UNSUPPORTED;

    static {
        ConfigurationDefinition.Builder builder = new ConfigurationDefinition.Builder("liquibase.athena");

        LIQUIBASE_S3_TABLES_LOCATION = builder.define("liquibaseS3TablesLocation", String.class)
            .setDescription("S3 Location where Iceberg Tables will be stored")
            .build();
        
        S3_SKIP_UNSUPPORTED = builder.define("s3SkipUnsupported", Boolean.class)
            .setDescription("Whether or not to skip unsupported changes.")
            .build();
    }

    public static String getS3TablesLocation() {
        return LIQUIBASE_S3_TABLES_LOCATION.getCurrentValue();
    }

    public static Boolean getS3SkipUnsupported() {
        return S3_SKIP_UNSUPPORTED.getCurrentValue() != null ? S3_SKIP_UNSUPPORTED.getCurrentValue() : false;
    }
}
