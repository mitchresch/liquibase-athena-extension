package liquibae.ext.athena.lockservice;

import liquibase.lockservice.StandardLockService;
import liquibase.database.Database;
import liquibase.ext.athena.database.AthenaDatabase;
import liquibase.Scope;
import liquibase.exception.DatabaseException;
import liquibase.executor.Executor;
import liquibase.executor.ExecutorService;
import liquibase.executor.LoggingExecutor;
import liquibase.statement.core.CreateDatabaseChangeLogLockTableStatement;
import liquibase.statement.core.InitializeDatabaseChangeLogLockTableStatement;
import liquibase.database.ObjectQuotingStrategy;
import java.security.SecureRandom;

public class LockServiceAthena extends StandardLockService {

    private Boolean hasDatabaseChangeLogLockTable;
    private boolean isDatabaseChangeLogLockTableInitialized;
    private ObjectQuotingStrategy quotingStrategy;
    protected final SecureRandom random = new SecureRandom();

        
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(Database database) {
        return database instanceof AthenaDatabase;
    }

    @Override
    public void init() throws DatabaseException {
        boolean createdTable = false;
        Executor executor = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", database);

        int maxIterations = 10;

        if (executor instanceof LoggingExecutor) {
            if (super.isDatabaseChangeLogLockTableCreated()) {
                maxIterations = 0;
            } else {
                maxIterations = 1;
            }
        }

        for (int i = 0; i < maxIterations; i++) {
            try {
                if (!super.isDatabaseChangeLogLockTableCreated(true)) {
                    executor.comment("Create Database Lock Table");
                    Scope.getCurrentScope().getLog(getClass()).fine(
                        "Creating database lock table with name: " +
                            database.escapeTableName(
                                database.getLiquibaseCatalogName(),
                                database.getLiquibaseSchemaName(),
                                database.getDatabaseChangeLogLockTableName()
                            )
                    );
                    executor.execute(new CreateDatabaseChangeLogLockTableStatement());
                    database.commit();
                    Scope.getCurrentScope().getLog(getClass()).fine(
                        "Created database lock table with name: " +
                            database.escapeTableName(
                                database.getLiquibaseCatalogName(),
                                database.getLiquibaseSchemaName(),
                                database.getDatabaseChangeLogLockTableName()
                            )
                    );
                    this.hasDatabaseChangeLogLockTable = true;
                    createdTable = true;
                }

                if (!isDatabaseChangeLogLockTableInitialized(createdTable, true)) {
                    executor.comment("Initialize Database Lock Table");
                    executor.execute(new InitializeDatabaseChangeLogLockTableStatement());
                    database.commit();
                }

                break;
            } catch (Exception e) {
                if (i == maxIterations - 1) {
                    throw e;
                } else {
                    Scope.getCurrentScope().getLog(getClass()).fine("Failed to create Athena databasechangeloglock table.  Attempting again in 1 second");
                    database.rollback();
                    try {
                        Thread.sleep(random.nextInt(1000));
                    } catch (InterruptedException ex) {
                        Scope.getCurrentScope().getLog(getClass()).warning("Sleep interrupted while waiting to retry creating databasechangeloglock table", ex);
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
