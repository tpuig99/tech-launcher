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
                    return new Framework(rs.getInt("framework_id"), rs.getString("framework_name"),FrameworkCategories.getByName(rs.getString("category")),rs.getString("description"),rs.getString("introduction"),rs.getString("logo"));
                }
            };

    @Autowired
    public FrameworkDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("framework_id");

        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS frameworks ("
                + "framework_id SERIAL PRIMARY KEY,"
                + "framework_name varchar(50) NOT NULL,"
                + "category varchar(50) NOT NULL,"
                + "description varchar(500) NOT NULL,"
                + "introduction varchar(5000) NOT NULL,"
                + "logo varchar(150)"
                + ")");
    }

    @Override
    public Framework findById(long id) {
        final List<Framework> list = jdbcTemplate.query("SELECT * FROM frameworks WHERE framework_id = ?", ROW_MAPPER, id);
        if (list.isEmpty()) {
            //return new Framework(id, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." );
            return null;
        }
        return list.get(0);
        }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        final List<Framework> toReturn = jdbcTemplate.query("SELECT * FROM frameworks WHERE category = ?", ROW_MAPPER, category.getNameCat());
        if (toReturn.isEmpty()) {
            //toReturn.add(new Framework(1, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." ));
            return null;
        }
        return toReturn;
    }

    @Override
    public List<Framework> getAll() {
        final List<Framework> toReturn = jdbcTemplate.query("SELECT * FROM frameworks", ROW_MAPPER);
        if (toReturn.isEmpty()) {
            //toReturn.add(new Framework(1, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." ));
            return null;
        }
        return toReturn;
    }


    private Framework create(String framework_name,FrameworkCategories category,String description,String introduction,String logo) {
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_name", framework_name); // la key es el nombre de la columna
        args.put("category", category.name()); // la key es el nombre de la columna
        args.put("description", description); // la key es el nombre de la columna
        args.put("introduction",introduction);
        args.put("logo",logo);
        final Number frameworkId = jdbcInsert.executeAndReturnKey(args);
        return new Framework(frameworkId.longValue(), framework_name,category,description,introduction,logo);
    }
}
