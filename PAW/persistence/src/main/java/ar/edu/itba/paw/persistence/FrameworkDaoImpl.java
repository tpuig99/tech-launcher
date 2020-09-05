package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FrameworkDaoImpl implements FrameworkDao {
    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Framework> ROW_MAPPER = new
            RowMapper<Framework>() {
                @Override
                public Framework mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Framework(rs.getInt("frameworkid"), rs.getString("frameworkname"),FrameworkCategories.valueOf(rs.getString("category")),rs.getString("description"));
                }
            };

    @Autowired
    public FrameworkDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("frameworkid");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS frameworks ("
                + "frameworkid SERIAL PRIMARY KEY,"
                + "frameworkname varchar(50),"
                + "category varchar(50),"
                + "description varchar(500)"
                + ")");
    }

    @Override
    public Framework findById(long id) {
        final List<Framework> list = jdbcTemplate.query("SELECT * FROM frameworks WHERE frameworkid = ?", ROW_MAPPER, id);
        if (list.isEmpty()) {
            return new Framework(id, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." );
        }
        return list.get(0);
        }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        final List<Framework> toReturn = jdbcTemplate.query("SELECT * FROM frameworks WHERE category = ?", ROW_MAPPER, category.name());
        if (toReturn.isEmpty()) {
            toReturn.add(new Framework(1, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." ));
        }
        return toReturn;
    }

    @Override
    public List<Framework> getAll() {
        final List<Framework> toReturn = jdbcTemplate.query("SELECT * FROM frameworks", ROW_MAPPER);
        if (toReturn.isEmpty()) {
            toReturn.add(new Framework(1, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." ));
        }
        return toReturn;
    }


    private Framework create(String frameworkname,FrameworkCategories category,String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("frameworkname", frameworkname); // la key es el nombre de la columna
        args.put("category", category.name()); // la key es el nombre de la columna
        args.put("description", description); // la key es el nombre de la columna
        final Number frameworkId = jdbcInsert.executeAndReturnKey(args);
        return new Framework(frameworkId.longValue(), frameworkname,category,description);
    }
}
