package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FrameworkDaoImpl implements FrameworkDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Framework> ROW_MAPPER = FrameworkDaoImpl::mapRow;
    private final String SELECTION="select frameworks.framework_id,type,framework_name,category,description,introduction,logo,COALESCE(avg(stars),0) as stars,count(stars) as votes_cant from frameworks left join framework_votes on frameworks.framework_id = framework_votes.framework_id ";
    private final String GROUP_BY=" group by framework_name, frameworks.framework_id, category, type, description, introduction, logo";

    private final String SELECTION2 = " SELECT f.framework_id, f.type, framework_name, category, description, introduction, logo, COALESCE(avg(stars),0) as stars, count(stars) as votes_cant " +
            "FROM frameworks AS f LEFT JOIN framework_votes AS v ON f.framework_id = v.framework_id ";
    private final String GROUP_BY2 = " GROUP BY framework_name, f.framework_id, category, f.type, description, introduction, logo ";


    @Autowired
    public FrameworkDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
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
                rs.getString("logo"),
                rs.getDouble("stars"),
                rs.getInt("votes_cant"),
                FrameworkType.getByName(rs.getString("type")));
    }

    @Override
    public Optional<Framework> findById(long id) {
        String value=SELECTION+"WHERE frameworks.framework_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ id }, ROW_MAPPER).stream().findFirst();
        }

    @Override
    public List<Framework> getByCategory(FrameworkCategories category) {
        String value=SELECTION+"WHERE category = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ category.getNameCat() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByType(FrameworkType type) {
        String value=SELECTION+"WHERE type = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ type.getType() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryAndType(FrameworkType type, FrameworkCategories category) {
        String value=SELECTION+"WHERE type = ? and category = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ type.getType(),category.getNameCat() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryAndWord(FrameworkCategories category, String toSearch) {
        String value = "%"+toSearch+"%";
        String query=SELECTION+"WHERE framework_name ILIKE ? OR category ILIKE ?"+GROUP_BY;
        return jdbcTemplate.query(query, new Object[]{ value, value }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByTypeAndWord(FrameworkType type, String toSearch) {
        String value = "%"+toSearch+"%";
        String query=SELECTION+"WHERE framework_name ILIKE ? and type = ?"+GROUP_BY;
        return jdbcTemplate.query(query, new Object[]{ type.getType(),value}, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryAndTypeAndWord(FrameworkType type, FrameworkCategories category, String toSearch) {
        String value = "%"+toSearch+"%";
        String query=SELECTION+"WHERE framework_name ILIKE ? and type = ? and category = ?"+GROUP_BY;
        return jdbcTemplate.query(query, new Object[]{ type.getType(),category.getNameCat(),value}, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByWord(String toSearch) {
        String value = "%"+toSearch+"%";
        String query=SELECTION+"WHERE framework_name ILIKE ? OR category ILIKE ? OR type ILIKE ?"+GROUP_BY;
        return jdbcTemplate.query(query, new Object[]{ value, value, value}, ROW_MAPPER);
    }

    @Override
    public List<Framework> getUserInterests(long userId) {
        String query = SELECTION2 + " inner join content as c on f.framework_id = c.framework_id where c.user_id = ? " + GROUP_BY2;
        return jdbcTemplate.query(query, new Object[] { userId }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByCategoryOrType(FrameworkType type, FrameworkCategories category) {
        String value=SELECTION+"WHERE type = ? or category = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ type.getType(),category.getNameCat() }, ROW_MAPPER);
    }

    @Override
    public List<Framework> getByMultipleCategories(List<FrameworkCategories> categories) {
        List<String> catNames = categories.stream().map(FrameworkCategories::getNameCat).collect(Collectors.toList());
        SqlParameterSource parameters = new MapSqlParameterSource("categories", catNames);
        String value = SELECTION+"where category in (:categories)"+GROUP_BY;

        return namedJdbcTemplate.query(value,parameters,ROW_MAPPER);
    }

    @Override
    public List<Framework> getByMinStars(int stars) {
        String value=SELECTION+"WHERE stars>=?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ stars }, ROW_MAPPER);

    }

    @Override
    public List<Framework> getByMultipleTypes(List<FrameworkType> types) {
        List<String> typesNames = types.stream().map(FrameworkType::getType).collect(Collectors.toList());
        SqlParameterSource parameters = new MapSqlParameterSource("types", typesNames);
        String value = SELECTION+"where type in (:types)"+GROUP_BY;

        return namedJdbcTemplate.query(value,parameters,ROW_MAPPER);
    }

    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer stars) {
        if(toSearch==null && categories==null && types == null && (stars == null || stars == 0))
            return jdbcTemplate.query(SELECTION+GROUP_BY,ROW_MAPPER);
        String aux="where ";
        Map<String,List<String>> params = new HashMap<>();
        if(toSearch!=null && !toSearch.isEmpty()){
            aux = aux.concat("frameworks.framework_name ILIKE '%"+toSearch+"%' ");
        }
        if(categories!=null){
            if(!aux.equals("where "))
                aux =aux.concat("and ");
            aux = aux.concat("category in (:cat) ");
            params.put("cat",categories.stream().map(FrameworkCategories::getNameCat).collect(Collectors.toList()));
        }
        if(types!=null){
            if(!aux.equals("where "))
                aux =aux.concat("and ");
            aux = aux.concat("type in (:type) ");
            params.put("type",types.stream().map(FrameworkType::getType).collect(Collectors.toList()));
        }
        if(stars!=null && stars!=0){
            if(!aux.equals("where "))
                aux =aux.concat("and ");
            aux = aux.concat("stars>="+stars+" ");
        }
        return namedJdbcTemplate.query(SELECTION+aux+GROUP_BY,params,ROW_MAPPER);
    }

    @Override
    public List<Framework> getAll() {
        return jdbcTemplate.query(SELECTION+GROUP_BY, ROW_MAPPER);
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
        return new Framework(frameworkId.longValue(), framework_name,category,description,introduction,logo,0,0,type);
    }
}
