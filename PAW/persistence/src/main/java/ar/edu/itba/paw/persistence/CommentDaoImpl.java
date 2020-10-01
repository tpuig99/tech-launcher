package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.ReportComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
public class CommentDaoImpl implements CommentDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final ResultSetExtractor<List<Comment>> SET_EXTRACTOR = CommentDaoImpl::extractor;
    private final ResultSetExtractor<List<Comment>> SET_EXTRACTOR_USER_VOTE = CommentDaoImpl::extractorUserVote;
    private final String SELECTION ="select cru.user_name as user_name_reporter, (CASE WHEN vu.pending IS false THEN true ELSE false END) AS is_verify,c.comment_id,c.framework_id,c.user_id,c.description,tstamp,reference,framework_name,f.category,count(case when vote=-1 then vote end) as neg,count(case when vote=1 then vote end) as pos, cu.user_name,(case when a.user_id is null then false else true end) as is_admin";
    private final String FROM =" from comments c left join comment_votes cv on c.comment_id = cv.comment_id left join frameworks f on c.framework_id = f.framework_id left join users cu on cu.user_id = c.user_id left join verify_users vu on c.user_id=vu.user_id and f.framework_id = vu.framework_id left join admins a on c.user_id=a.user_id left join comment_report cr on c.comment_id = cr.comment_id left join users cru on cru.user_id = cr.user_id ";
    private final String GROUP_BY = " group by c.comment_id , framework_name, cru.user_name,cu.user_name,f.category,pending,a.user_id order by c.comment_id";
    private final String USER_VOTE =",coalesce((select vote from comment_votes where user_id=? and comment_id=c.comment_id),0) as user_vote";

    @Autowired
    public CommentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);

        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comments")
                .usingGeneratedKeyColumns("comment_id");

    }
    private static List<Comment> extractor(ResultSet rs) throws SQLException {
        List<Comment> list=new ArrayList<>();
        Long currentComment = null;
        Comment rc = null;
        while(rs.next()){
            Long nextComment=rs.getLong("comment_id");
            if(currentComment==null||currentComment!=nextComment){
                if(rc!=null){
                    list.add(rc);
                }
                currentComment = nextComment;
                rc = new Comment(rs.getLong("comment_id"),
                        rs.getInt("framework_id"),
                        rs.getLong("user_id"),
                        rs.getString("description"),
                        rs.getInt("pos"),
                        rs.getInt("neg"),
                        rs.getTimestamp("tstamp"),
                        rs.getLong("reference"),
                        rs.getString("framework_name"),
                        rs.getString("user_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getBoolean("is_verify"),
                        rs.getBoolean("is_admin")
                );
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

    private static List<Comment> extractorUserVote(ResultSet rs) throws SQLException {
        List<Comment> list=new ArrayList<>();
        Long currentComment = null;
        Comment rc = null;
        while(rs.next()){
            Long nextComment=rs.getLong("comment_id");
            if(currentComment==null||currentComment!=nextComment){
                if(rc!=null){
                    list.add(rc);
                }
                currentComment = nextComment;
                rc = new Comment(rs.getLong("comment_id"),
                        rs.getInt("framework_id"),
                        rs.getLong("user_id"),
                        rs.getString("description"),
                        rs.getInt("pos"),
                        rs.getInt("neg"),
                        rs.getTimestamp("tstamp"),
                        rs.getLong("reference"),
                        rs.getString("framework_name"),
                        rs.getString("user_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getBoolean("is_verify"),
                        rs.getBoolean("is_admin"),
                        rs.getInt("user_vote")
                );
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

    private static Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("comment_id"),
                rs.getInt("framework_id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getInt("pos"),
                rs.getInt("neg"),
                rs.getTimestamp("tstamp"),
                rs.getLong("reference"),
                rs.getString("framework_name"),
                rs.getString("user_name"),
                FrameworkCategories.getByName(rs.getString("category")),
                rs.getBoolean("is_verify"),
                rs.getBoolean("is_admin")
        );
    }

    private static Comment mapRowUserAuthVote(ResultSet rs, int rowNum) throws SQLException {
        return new Comment(rs.getLong("comment_id"),
                rs.getInt("framework_id"),
                rs.getLong("user_id"),
                rs.getString("description"),
                rs.getInt("pos"),
                rs.getInt("neg"),
                rs.getTimestamp("tstamp"),
                rs.getLong("reference"),
                rs.getString("framework_name"),
                rs.getString("user_name"),
                FrameworkCategories.getByName(rs.getString("category")),
                rs.getBoolean("is_verify"),
                rs.getBoolean("is_admin"),
                rs.getInt("user_vote")
        );
    }


    @Override
    public Optional<Comment> getById(long commentId) {
        String value = SELECTION+FROM+"WHERE c.comment_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {commentId},SET_EXTRACTOR).stream().findFirst();
    }

    @Override
    public List<Comment> getCommentsByFramework(long frameworkId,Long userId) {
        String value;
        if(userId!=null)
        {
            value = SELECTION+USER_VOTE+FROM+"where c.framework_id = ?"+GROUP_BY;
            return jdbcTemplate.query(value, new Object[] {userId,frameworkId},SET_EXTRACTOR_USER_VOTE  );
        }
        value = SELECTION+FROM+"where c.framework_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId},  SET_EXTRACTOR );
    }

    @Override
    public List<Comment> getCommentsWithoutReferenceByFramework(long frameworkId) {
        String value = SELECTION+FROM+"where c.framework_id = ? AND c.reference IS NULL"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId},  SET_EXTRACTOR );
    }

    @Override
    public List<Comment> getCommentsByFrameworkAndUser(long frameworkId, long userId) {
        String value = SELECTION+FROM+"WHERE c.framework_id = ? AND c.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {frameworkId, userId},  SET_EXTRACTOR);
    }

    @Override
    public List<Comment> getCommentsByUser(long userId) {
        String value = SELECTION+FROM+"WHERE c.user_id = ?"+GROUP_BY;
        return jdbcTemplate.query(value, new Object[] {userId}, SET_EXTRACTOR);
    }

    //TODO optimize queries
    @Override
    public Map<Long, List<Comment>> getRepliesByFramework(long frameworkId) {
        Map<Long, List<Comment>> toReturn = new HashMap<>();
        List<Comment> replies = new ArrayList<>();
        long commentId;

        List<Comment> commentsWithoutRef = getCommentsWithoutReferenceByFramework(frameworkId);

        if(!commentsWithoutRef.isEmpty()) {
            String value = SELECTION+FROM+"where c.framework_id = ? AND c.reference = ?"+GROUP_BY;
            for (Comment comment : commentsWithoutRef) {
                commentId = comment.getCommentId();
                toReturn.put(commentId, jdbcTemplate.query(value, new Object[]{frameworkId, commentId}, SET_EXTRACTOR));

            }
        }
        return toReturn;
    }


    @Override
    public Optional<Comment> insertComment(long frameworkId, long userId, String description, Long reference) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        final Map<String, Object> args = new HashMap<>();
        args.put("framework_id", frameworkId);
        args.put("user_id", userId);
        args.put("description", description);
        args.put("tstamp", ts);
        args.put("reference", reference);

        final Number commentId = jdbcInsert.executeAndReturnKey(args);
        return getById(commentId.longValue());
    }

    @Override
    public int deleteComment(long commentId) {
        return jdbcTemplate.update("DELETE FROM comments WHERE comment_id = ?", new Object[]{commentId});
    }

    @Override
    public Optional<Comment> changeComment(long commentId, String description) {
        jdbcTemplate.update("UPDATE comments SET description = ? WHERE comment_id = ?", new Object[]{description, commentId});
        return getById(commentId);
    }

}
