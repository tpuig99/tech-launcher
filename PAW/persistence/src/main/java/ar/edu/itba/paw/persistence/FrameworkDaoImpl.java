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
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FrameworkDaoImpl implements FrameworkDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Framework> ROW_MAPPER = FrameworkDaoImpl::mapRow;
    private final static RowMapper<String> ROW_MAPPER_FRAMEWORK_NAME = FrameworkDaoImpl::mapRowNames;

    private final String SELECTION="select f.framework_id, count(distinct comment_id) as comment_amount,f.type,framework_name,category,f.description,introduction,logo,COALESCE(avg(stars),0) as stars,count(stars) as votes_cant,user_name as author,f.date, max(tstamp) as last_comment from frameworks f left join framework_votes fv on f.framework_id = fv.framework_id left join users u on u.user_id=f.author left join comments c on f.framework_id=c.framework_id  ";
    private final String GROUP_BY=" group by framework_name, f.framework_id, category, f.type, f.description, introduction, logo,u.user_name";


    @Autowired
    public FrameworkDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("framework_id");

    }
    private static String mapRowNames(ResultSet rs, int i) throws SQLException {
        return rs.getString("framework_name");
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
                FrameworkType.getByName(rs.getString("type")),
                rs.getString("author"),
                rs.getTimestamp("date"),
                rs.getTimestamp("last_comment"),
                rs.getInt("comment_amount"));
    }

    @Override
    public Optional<Framework> findById(long id) {
        String value=SELECTION+"WHERE f.framework_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[]{ id }, ROW_MAPPER).stream().findFirst();
        }

    @Override
    public List<String> getFrameworkNames() {
        return jdbcTemplate.query("select framework_name from frameworks",ROW_MAPPER_FRAMEWORK_NAME);
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
        String query = SELECTION + " inner join content as c on f.framework_id = c.framework_id where c.user_id = ? " + GROUP_BY;
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
        String value=SELECTION+GROUP_BY+" having COALESCE(avg(stars),0)>=?";
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
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer stars,boolean nameFlag) {
        if(toSearch==null && categories==null && types == null && (stars == null || stars == 0))
            return jdbcTemplate.query(SELECTION+GROUP_BY,ROW_MAPPER);
        String aux="where ";
        Map<String,List<String>> params = new HashMap<>();
        if(toSearch!=null && !toSearch.isEmpty()){
            aux = aux.concat("f.framework_name ILIKE '%"+toSearch+"%' ");
            if(toSearch.length()>3 && !nameFlag)
                aux = aux.concat("or f.description ILIKE '%"+toSearch+"%' or f.description ILIKE '%"+toSearch+"%' ");
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

    @Override
    public void create(String framework_name,FrameworkCategories category,String description,String introduction,FrameworkType type,long userId) {
        final Map<String, Object> args = new HashMap<>();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        args.put("framework_name", framework_name); // la key es el nombre de la columna
        args.put("category", category.name()); // la key es el nombre de la columna
        args.put("description", description); // la key es el nombre de la columna
        args.put("introduction",introduction);
        args.put("logo",null);
        args.put("author",userId);
        args.put("type",type.getType());
        args.put("date",ts);
        jdbcInsert.executeAndReturnKey(args);
    }
}
