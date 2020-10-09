package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.ReportComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ReportCommentDaoImpl implements ReportCommentDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final String SELECTION ="select cr.comment_id, cr.report_id,c.framework_id," +
            " c.user_id as user_id_owner,c.description as comment_description,cr.description as report_description," +
            "c.tstamp,c.reference,f.framework_name,f.category, uc.user_name as user_name_owner, " +
            "ucr.user_name as user_name_reporter from comment_report cr left join comments c on c.comment_id = cr.comment_id " +
            "left join frameworks f on c.framework_id = f.framework_id left join users uc on uc.user_id = c.user_id " +
            "left join users ucr on ucr.user_id=cr.user_id ";
    private final String ORDER_BY=" order by cr.comment_id";
    private final ResultSetExtractor<List<ReportComment>> EXTRACTOR = ReportCommentDaoImpl::extractor;

    @Autowired
    public ReportCommentDaoImpl(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("comment_report")
                .usingGeneratedKeyColumns("report_id");
    }

    private static List<ReportComment> extractor(ResultSet rs) throws SQLException {
        List<ReportComment> list=new ArrayList<>();
        Long currentComment = null;
        ReportComment rc = null;
        while(rs.next()){
            Long nextComment=rs.getLong("comment_id");
            if(currentComment==null||currentComment!=nextComment){
                if(rc!=null){
                    list.add(rc);
                }
                currentComment = nextComment;
                rc = new ReportComment(currentComment,
                        rs.getLong("framework_id"),
                        rs.getLong("user_id_owner"),
                        rs.getString("comment_description"),
                        rs.getString("report_description"),
                        rs.getTimestamp("tstamp"),
                        rs.getLong("reference"),
                        rs.getString("framework_name"),
                        rs.getString("user_name_owner"),
                        FrameworkCategories.getByName(rs.getString("category"))
                );
                rc.addUserReporter(rs.getLong("report_id"),rs.getString("user_name_reporter"));
            }else{
                rc.addUserReporter(rs.getLong("report_id"),rs.getString("user_name_reporter"));
            }
        }
        if(rc!=null){
            list.add(rc);
        }
        return list;
    }


    @Override
    public Optional<ReportComment> getById(long reportId) {
        return Optional.empty();
    }

    @Override
    public List<ReportComment> getAll(long page, long pageSize) {
        return jdbcTemplate.query(SELECTION+ORDER_BY + " LIMIT "+ pageSize + " OFFSET " + (page-1)*pageSize,EXTRACTOR);
    }

    @Override
    public List<ReportComment> getByFramework(long frameworkId) {
        return jdbcTemplate.query(SELECTION+"where f.framework_id = ?"+ORDER_BY,new Object[]{frameworkId},EXTRACTOR);
    }

    @Override
    public Optional<ReportComment> getByComment(long commentId) {
        String value = SELECTION+"where cr.comment_id = ?"+ORDER_BY;
        return jdbcTemplate.query(value,new Object[]{commentId},EXTRACTOR).stream().findFirst();
    }

    @Override
    public void add(long commentId, long userId, String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("comment_id", commentId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("description",description);
        jdbcInsert.executeAndReturnKey(args);
    }

    @Override
    public void delete(long reportId) {
         jdbcTemplate.update("DELETE FROM comment_report WHERE report_id = ?", new Object[]{reportId});

    }

    @Override
    public void deleteReportByComment(long commentId) {
        jdbcTemplate.update("DELETE FROM comment_report WHERE comment_id = ?", new Object[]{commentId});

    }
}
