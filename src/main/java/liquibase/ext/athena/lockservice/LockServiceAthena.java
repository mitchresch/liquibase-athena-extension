package liquibase.ext.athena.lockservice;

import liquibase.lockservice.StandardLockService;
import liquibase.database.Database;
import liquibase.ext.athena.database.AthenaDatabase;
import liquibase.Scope;
import liquibase.executor.Executor;
import liquibase.executor.ExecutorService;
import liquibase.statement.core.CreateDatabaseChangeLogLockTableStatement;
import liquibase.database.ObjectQuotingStrategy;
import liquibase.exception.DatabaseException;

public class LockServiceAthena extends StandardLockService {
        
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

        /*
         * Create database changelog lock table statement
         * before doing everything else in the main init() method
         */
        Executor executor = Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor("jdbc", database);
        executor.execute(new CreateDatabaseChangeLogLockTableStatement());
        database.commit();
        super.init();
    }
}
