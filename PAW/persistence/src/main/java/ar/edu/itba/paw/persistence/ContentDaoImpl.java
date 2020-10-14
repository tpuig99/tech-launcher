package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class ContentDaoImpl implements ContentDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private final String SELECTION ="select cru.user_name as user_name_reporter,u.user_name,c.content_id,c.framework_id,c.user_id,title,link,tstamp,c.type,count(case when vote=-1 then vote end) as neg,count(case when vote=1 then vote end) as pos, framework_name,category,(CASE WHEN vu.pending IS false THEN true ELSE false END) AS is_verify,(case when a.user_id is null then false else true end) as is_admin";
    private final String FROM=" from content c left join content_votes cv on c.content_id = cv.content_id left join frameworks f on c.framework_id = f.framework_id left join verify_users vu on c.user_id=vu.user_id and f.framework_id = vu.framework_id left join admins a on c.user_id=a.user_id left join content_report cr on c.content_id = cr.content_id left join users u on u.user_id = c.user_id left join users cru on cru.user_id = cr.user_id ";
    private final String GROUP_BY = " group by c.content_id ,framework_name,category,a.user_id,cru.user_name,u.user_name, pending order by c.content_id";
    private final String USER_AUTH_VOTE=",coalesce((select vote from content_votes where user_id=? and content_id=c.content_id),0) as user_vote ";

    private final ResultSetExtractor<List<Content>> SET_EXTRACTOR = ContentDaoImpl::extractor;
    private final ResultSetExtractor<List<Content>> SET_EXTRACTOR_USER_VOTE = ContentDaoImpl::extractorUserVote;
    private final RowMapper<Integer> ROW_MAPPER_COUNT = ContentDaoImpl::mapRowCount;

    @Autowired
    public ContentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content")
                .usingGeneratedKeyColumns("content_id");

    }

    private static List<Content> extractor(ResultSet rs) throws SQLException {
        List<Content> list=new ArrayList<>();
        Long currentContent = null;
        Content rc = null;
        while(rs.next()){
            Long nextContent=rs.getLong("content_id");
            if(currentContent==null||currentContent!=nextContent){
                if(rc!=null){
                    list.add(rc);
                }
                currentContent = nextContent;
                rc = new Content(rs.getLong("content_id"),
                        rs.getLong("framework_id"),
                        rs.getLong("user_id"),
                        rs.getString("title"),
                        rs.getInt("pos"),
                        rs.getInt("neg"),
                        rs.getTimestamp("tstamp"),
                        rs.getString("link"),
                        Enum.valueOf(ContentTypes.class, rs.getString("type")),
                        rs.getString("framework_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getBoolean("is_verify"),
                        rs.getBoolean("is_admin"),
                        rs.getString("user_name"));
                String report =rs.getString("user_name_reporter");
                if(report!=null)
                    rc.addReporter(report);
            }else{
                rc.addReporter(rs.getString("user_name_reporter"));
            }
        }
        if(rc!=null){
            list.add(rc);
        }
        return list;
    }

    private static List<Content> extractorUserVote(ResultSet rs) throws SQLException {
        List<Content> list=new ArrayList<>();
        Long currentContent = null;
        Content rc = null;
        while(rs.next()){
            Long nextContent=rs.getLong("content_id");
            if(currentContent==null||currentContent!=nextContent){
                if(rc!=null){
                    list.add(rc);
                }
                currentContent = nextContent;
                rc = new Content(rs.getLong("content_id"),
                        rs.getLong("framework_id"),
                        rs.getLong("user_id"),
                        rs.getString("title"),
                        rs.getInt("pos"),
                        rs.getInt("neg"),
                        rs.getTimestamp("tstamp"),
                        rs.getString("link"),
                        Enum.valueOf(ContentTypes.class, rs.getString("type")),
                        rs.getString("framework_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getBoolean("is_verify"),
                        rs.getBoolean("is_admin"),
                        rs.getString("user_name"),
                        rs.getInt("user_vote"));
                String report =rs.getString("user_name_reporter");
                if(report!=null)
                    rc.addReporter(report);
            }else{
                rc.addReporter(rs.getString("user_name_reporter"));
            }
        }
        if(rc!=null){
            list.add(rc);
        }
        return list;
    }

    private static Integer mapRowCount(ResultSet rs, int i) throws SQLException {
        return rs.getInt("count");
    }

    @Override
    public Optional<Content> getById(long contentId) {
        return jdbcTemplate.query(SELECTION+FROM+" WHERE c.content_id = ?"+GROUP_BY, new Object[] {contentId}, SET_EXTRACTOR).stream().findFirst();
    }

    @Override
    public List<Content> getContentByFramework(long frameworkId) {
        return jdbcTemplate.query(SELECTION+FROM+" where c.framework_id = ?"+GROUP_BY, new Object[] { frameworkId }, SET_EXTRACTOR);
    }

    @Override
    public List<Content> getContentByFrameworkAndUser(long frameworkId, long userId) {
        return jdbcTemplate.query(SELECTION+FROM+" WHERE c.framework_id = ? AND c.user_id = ?"+GROUP_BY, new Object[] { frameworkId, userId }, SET_EXTRACTOR);
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type, long page, long pageSize) {
        return jdbcTemplate.query(SELECTION+FROM+" WHERE c.framework_id = ? AND c.type = ?"+GROUP_BY + " LIMIT ? OFFSET ?", new Object[] { frameworkId, type.name(), pageSize, (page-1)*pageSize }, SET_EXTRACTOR);
    }


    @Override
    public List<Content> getContentByUser(long userId, long page, long pageSize) {
        return jdbcTemplate.query(SELECTION+FROM+" where c.user_id=?"+GROUP_BY + " LIMIT ? OFFSET ?", new Object[] { userId, pageSize, pageSize*(page-1) }, SET_EXTRACTOR);
    }

    @Override
    public Optional<Integer> getContentCountByUser(long userId){
        return jdbcTemplate.query("select count (*) from content inner join users on content.user_id = users.user_id where content.user_id = ?", new Object[] { userId}, ROW_MAPPER_COUNT).stream().findFirst();

    }

    @Override
    public List<Content> getContentByFrameworkAndTypeAndTitle(long frameworkId, ContentTypes type, String title) {
        return jdbcTemplate.query(SELECTION+FROM+" WHERE c.framework_id = ? AND c.type = ? AND c.title = ?"+GROUP_BY, new Object[] { frameworkId, type.name(),title }, SET_EXTRACTOR);
    }

    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id",userId);
        args.put("title", title);
        args.put("tstamp", ts);
        args.put("link", link);
        args.put("type", type.name());

        final Number contentId = jdbcInsert.executeAndReturnKey(args);
        return getById(contentId.longValue()).get();
    }

    @Override
    public int deleteContent(long contentId) {
        return jdbcTemplate.update("DELETE FROM content WHERE content_id = ?",new Object[]{contentId});
    }

    @Override
    public Optional<Content> changeContent(long contentId, String title, String link, ContentTypes type) {
        jdbcTemplate.update("UPDATE content SET title = ?, link = ?, type = ? WHERE content_id = ?", title, link, type.name(),contentId);
        return getById(contentId);
    }

}
