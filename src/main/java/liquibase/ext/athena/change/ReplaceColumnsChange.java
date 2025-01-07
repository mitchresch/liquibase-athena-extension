package liquibase.ext.athena.change;

import liquibase.change.AbstractChange;
import liquibase.change.ChangeMetaData;
import liquibase.change.DatabaseChange;
import liquibase.database.Database;
import liquibase.statement.SqlStatement;
import liquibase.ext.athena.database.AthenaDatabase;

@DatabaseChange(name="replaceColumns",
        description = "",
        priority = ChangeMetaData.PRIORITY_DEFAULT)
public class ReplaceColumnsChange extends AbstractChange {

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
        return "Columns replaced";
    }
}
