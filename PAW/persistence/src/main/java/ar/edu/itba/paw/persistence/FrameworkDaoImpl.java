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
import java.util.*;

@Repository
public class FrameworkDaoImpl implements FrameworkDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Framework> ROW_MAPPER = FrameworkDaoImpl::mapRow;

    @Autowired
    public FrameworkDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("framework_id");

    }

    private static Framework mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Framework(rs.getInt("framework_id"),
                rs.getString("framework_name"),
                FrameworkCategories.getByName(rs.getString("category")),
                rs.getString("description"),
                rs.getString("introduction"),
                rs.getString("logo"),FrameworkType.getByName(rs.getString("type")));
    }

    @Override
    public Optional<Framework> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE framework_id = ?", new Object[]{ id }, ROW_MAPPER).stream().findFirst();
        }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
            //toReturn.add(new Framework(1, "Angular", FrameworkCategories.Back_End_Development, "Angular is a framework for dynamic websited." ));
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE category = ?", new Object[]{ category.getNameCat() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByType(FrameworkType type) {
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE type = ?", new Object[]{ type.getType() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryAndType(FrameworkType type, FrameworkCategories category) {
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE type = ? and category = ? ", new Object[]{ type.getType(),category.getNameCat() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryAndWord(FrameworkCategories category, String toSearch) {
        String value = "%"+toSearch+"%";
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE category = ? and framework_name ILIKE ?", new Object[]{ category.getNameCat(),value}, ROW_MAPPER);

    }

    @Override
    public List<Framework> getByTypeAndWord(FrameworkType type, String toSearch) {
        String value = "%"+toSearch+"%";
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE type = ? and framework_name ILIKE ?", new Object[]{ type.getType(),value}, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryAndTypeAndWord(FrameworkType type, FrameworkCategories category, String toSearch) {
        String value = "%"+toSearch+"%";
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE type = ? and category = ? and framework_name ILIKE ?", new Object[]{ type.getType(),category.getNameCat(),value}, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByWord(String toSearch) {
        String value = "%"+toSearch+"%";
        return jdbcTemplate.query("SELECT * FROM frameworks WHERE framework_name ILIKE ? OR category ILIKE ? OR type ILIKE ?", new Object[]{ value, value, value}, ROW_MAPPER);
    }

    @Override
    public List<Framework> getAll() {
        return jdbcTemplate.query("SELECT * FROM frameworks", ROW_MAPPER);
    }

    private Framework create(String framework_name,FrameworkCategories category,String description,String introduction,String logo,FrameworkType type) {
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_name", framework_name); // la key es el nombre de la columna
        args.put("category", category.name()); // la key es el nombre de la columna
        args.put("description", description); // la key es el nombre de la columna
        args.put("introduction",introduction);
        args.put("logo",logo);
        args.put("type",type.getType());
        final Number frameworkId = jdbcInsert.executeAndReturnKey(args);
        return new Framework(frameworkId.longValue(), framework_name,category,description,introduction,logo,type);
    }
}
