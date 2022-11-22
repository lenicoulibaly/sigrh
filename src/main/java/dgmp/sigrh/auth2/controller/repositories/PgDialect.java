package dgmp.sigrh.auth2.controller.repositories;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;

public class PgDialect extends PostgreSQL94Dialect
{
    public PgDialect() {
        super();
        registerFunction("get_hierarchy_sigles", new StandardSQLFunction("get_hierarchy_sigles", StandardBasicTypes.STRING));
    }
}
//dgmp.sigrh.auth2.controller.repositories.PgDialect