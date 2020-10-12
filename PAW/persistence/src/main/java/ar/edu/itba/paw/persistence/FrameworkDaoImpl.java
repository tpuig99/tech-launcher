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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
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
    private final static RowMapper<Integer> ROW_MAPPER_COUNT = FrameworkDaoImpl::mapRowCount;

    private final String SELECTION="select f.framework_id, count(distinct comment_id) as comment_amount,f.type,framework_name,category,f.description,introduction,logo,COALESCE(avg(stars),0) as stars,count(distinct fv.user_id) as votes_cant,user_name as author,f.date, max(c.tstamp) as last_comment, f.picture from frameworks f left join framework_votes fv on f.framework_id = fv.framework_id left join users u on u.user_id=f.author left join comments c on f.framework_id=c.framework_id  ";
    private final String SELECTION_COUNT="select count(*) as count from frameworks f left join framework_votes fv on f.framework_id = fv.framework_id left join users u on u.user_id=f.author left join comments c on f.framework_id=c.framework_id ";

    private final String GROUP_BY=" group by framework_name, f.framework_id, category, f.type, f.description, introduction, logo,u.user_name,f.date ";


    @Autowired
    public FrameworkDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("frameworks")
                .usingGeneratedKeyColumns("framework_id");

    }

    private static Integer mapRowCount(ResultSet rs, int i) throws SQLException {
        return rs.getInt("count");
    }

    private static String mapRowNames(ResultSet rs, int i) throws SQLException {
        return rs.getString("framework_name");
    }
    private static Framework mapRow(ResultSet rs, int rowNum) throws SQLException {

        byte[] image = rs.getBytes("picture");
        String encodeBase64 = "";
        String contentType = "";

        if (image != null) {
            byte[] encodedByteArray = Base64.getEncoder().encode(rs.getBytes("picture"));
            encodeBase64 = new String(encodedByteArray, StandardCharsets.UTF_8);
            try {
                contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(rs.getBytes("picture")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
                rs.getInt("comment_amount"),
                contentType,
                encodeBase64);
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
    public List<Framework> getByCategory(FrameworkCategories category, long page, long pageSize) {
        String value=SELECTION+"WHERE category = ?"+GROUP_BY + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(value, new Object[]{ category.getNameCat(), pageSize, (page-1)*pageSize }, ROW_MAPPER);
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
        String query = SELECTION + " inner join content as cont on f.framework_id = cont.framework_id where cont.user_id = ? " + GROUP_BY;
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
    public List<Framework> getByUser(long userId, long page, long pageSize) {
        String value=SELECTION+"WHERE author = ?"+GROUP_BY + " LIMIT ? OFFSET ?";
        return jdbcTemplate.query(value, new Object[]{ userId, pageSize, pageSize*(page-1) }, ROW_MAPPER);    }

    @Override
    public List<Framework> search(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft,Integer starsRight,boolean nameFlag,Integer commentAmount,Timestamp lastComment,Timestamp lastUpdated,Integer order, long page, long pageSize) {
        if(toSearch==null && categories==null && types == null && starsLeft == 0 && starsRight == 5 && commentAmount==0 && lastComment==null && lastUpdated==null && (order==null||order==0))
            return jdbcTemplate.query(SELECTION+GROUP_BY + " LIMIT " + pageSize +" OFFSET " + pageSize*(page-1),ROW_MAPPER);
        String aux = "";
        if(toSearch!=null || categories!=null || types != null || lastUpdated!=null)
             aux="where ";
        Map<String,List<String>> params = new HashMap<>();
        if(toSearch!=null && !toSearch.isEmpty()){
            if(nameFlag || toSearch.length()<3)
                aux = aux.concat("f.framework_name ILIKE '%"+toSearch+"%' ");
            else
                aux = aux.concat("(f.framework_name ILIKE '%"+toSearch+"%' or f.description ILIKE '%"+toSearch+"%' or f.description ILIKE '%"+toSearch+"%') ");
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
        if(lastUpdated!=null){
            if(!aux.equals("where "))
                aux =aux.concat("and ");
            aux =aux.concat("f.date>='"+lastUpdated+"' ");
        }
        String orderAux="";

        if(order!=null && order!=0){
              orderAux=" order by ";
            switch (Math.abs(order)){
                case 1:{
                    orderAux = orderAux.concat("COALESCE(avg(stars),0) " +( order>0?"desc ":""));
                    break;
                }
                case 2:{
                    orderAux = orderAux.concat("count(distinct comment_id) " +( order>0?"desc ":""));
                    break;
                }
                case 3:{
                    orderAux = orderAux.concat("date " +( order>0?"desc ":""));
                    break;
                }
                case 4:{
                    orderAux = orderAux.concat("last_comment " +( order>0?"desc NULLS last ":""));
                    break;
                }

            }
        }
        String have =" having COALESCE(avg(stars),0)>="+starsLeft+" and COALESCE(avg(stars),0)<="+starsRight+" and count(distinct comment_id)>="+commentAmount;
        if(lastComment!=null)
            have=have.concat(" and max(c.tstamp)>='"+lastComment+"'");
        return namedJdbcTemplate.query(SELECTION+aux+GROUP_BY+have+orderAux+" LIMIT " + pageSize +" OFFSET " + pageSize*(page-1),params,ROW_MAPPER);
    }

    @Override
    public Integer searchResultsNumber(String toSearch, List<FrameworkCategories> categories, List<FrameworkType> types, Integer starsLeft,Integer starsRight,boolean nameFlag,Integer commentAmount,Timestamp lastComment,Timestamp lastUpdated){
        if(toSearch==null && categories==null && types == null && starsLeft == 0 && starsRight == 5 && commentAmount==0 && lastComment==null && lastUpdated==null)
            return jdbcTemplate.query("select count(*) from (" + SELECTION+GROUP_BY + ") as searchResult",ROW_MAPPER_COUNT).get(0);
        String aux = "";
        if(toSearch!=null || categories!=null || types != null || lastUpdated!=null)
            aux="where ";
        Map<String,List<String>> params = new HashMap<>();
        if(toSearch!=null && !toSearch.isEmpty()){
            if(nameFlag || toSearch.length()<3)
                aux = aux.concat("f.framework_name ILIKE '%"+toSearch+"%' ");
            else
                aux = aux.concat("(f.framework_name ILIKE '%"+toSearch+"%' or f.description ILIKE '%"+toSearch+"%' or f.description ILIKE '%"+toSearch+"%') ");
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
        if(lastUpdated!=null){
            if(!aux.equals("where "))
                aux =aux.concat("and ");
            aux =aux.concat("f.date>='"+lastUpdated+"' ");
        }
        String have =" having COALESCE(avg(stars),0)>="+starsLeft+" and COALESCE(avg(stars),0)<="+starsRight+" and count(distinct comment_id)>="+commentAmount;
        if(lastComment!=null)
            have=have.concat(" and max(c.tstamp)>='"+lastComment+"'");
        return namedJdbcTemplate.query("select count(*) from (" + SELECTION+aux+GROUP_BY+have + ") as searchResult" ,params,ROW_MAPPER_COUNT).get(0);
    }

    @Override
    public List<Framework> getAll() {
        return jdbcTemplate.query(SELECTION+GROUP_BY, ROW_MAPPER);
    }

    @Override
    public Optional<Framework> create(String framework_name,FrameworkCategories category,String description,String introduction,FrameworkType type,long userId, byte[] picture) {
        final Map<String, Object> args = new HashMap<>();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        args.put("framework_name", framework_name); // la key es el nombre de la columna
        args.put("category", category.getNameCat()); // la key es el nombre de la columna
        args.put("description", description); // la key es el nombre de la columna
        args.put("introduction",introduction);
        args.put("logo",null);
        args.put("author",userId);
        args.put("type",type.getType());
        args.put("date",ts);
        args.put("picture", picture);

        final Number commentId = jdbcInsert.executeAndReturnKey(args);
        return findById(commentId.longValue());
    }

    @Override
    public Optional<Framework> update(long id, String name, FrameworkCategories category, String description, String introduction, FrameworkType type, byte[] picture) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        if(picture.length>0)
            jdbcTemplate.update("UPDATE frameworks SET framework_name = ?, category = ?, description = ?, introduction = ?, type=?, picture=?, date=?  WHERE framework_id = ?",new Object[]{name, category.getNameCat(), description,introduction,type.getType(),picture,ts,id});
        else
            jdbcTemplate.update("UPDATE frameworks SET framework_name = ?, category = ?, description = ?, introduction = ?, type=?,date =?  WHERE framework_id = ?",new Object[]{name, category.getNameCat(), description,introduction,type.getType(),ts,id});
        return findById(id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM frameworks WHERE framework_id = ?",new Object[]{id});
    }
}
