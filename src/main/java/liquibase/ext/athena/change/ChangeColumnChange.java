package liquibase.ext.athena.change;

import liquibase.change.AbstractChange;
import liquibase.database.Database;
import liquibase.ext.athena.database.AthenaDatabase;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.change.AbstractChange;
import liquibase.statement.SqlStatement;

@DatabaseChange(name="changeColumn",
        description = "",
        priority = ChangeMetaData.PRIORITY_DEFAULT)
public class ChangeColumnChange extends AbstractChange {

    @Override
    public boolean supports(Database database) {
        return database instanceof AthenaDatabase;
    }

    @Override
    public SqlStatement[] generateStatements(Database database) {
        // TODO: implement generateStatements
        return new SqlStatement[0];
    }

    @Override
    public String getConfirmationMessage() {
        return "Column changed";
    }
}
