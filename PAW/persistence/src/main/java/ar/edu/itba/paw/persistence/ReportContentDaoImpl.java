package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.FrameworkCategories;
import ar.edu.itba.paw.models.ReportContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ReportContentDaoImpl implements ReportContentDao{
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final String SELECTION ="select cr.content_id, c.framework_id, title,  c.tstamp,c.link,c.type,f.framework_name,f.category, uc.user_name as user_name_owner,ucr.user_name as user_name_reporter, cr.report_id,c.user_id as user_id_owner,cr.description as report_description " +
            "from content_report cr left join content c on c.content_id = cr.content_id left join frameworks f on c.framework_id = f.framework_id left join users uc on uc.user_id = c.user_id left join users ucr on ucr.user_id=cr.user_id ";
    private final String ORDER_BY=" order by cr.content_id";
    private final ResultSetExtractor<List<ReportContent>> EXTRACTOR = ReportContentDaoImpl::extractor;

    @Autowired
    public ReportContentDaoImpl(final DataSource ds) {
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("content_report")
                .usingGeneratedKeyColumns("report_id");
    }

    private static List<ReportContent> extractor(ResultSet rs) throws SQLException {
        List<ReportContent> list=new ArrayList<>();
        Long currentContent = null;
        ReportContent rc = null;
        while(rs.next()){
            Long nextContent=rs.getLong("content_id");
            if(currentContent==null||currentContent!=nextContent){
                if(rc!=null){
                    list.add(rc);
                }
                currentContent = nextContent;
                rc = new ReportContent(currentContent,
                        rs.getLong("framework_id"),
                        rs.getLong("user_id_owner"),
                        rs.getString("title"),
                        rs.getTimestamp("tstamp"),
                        rs.getString("link"),
                        Enum.valueOf(ContentTypes.class,rs.getString("type")),
                        rs.getString("framework_name"),
                        FrameworkCategories.getByName(rs.getString("category")),
                        rs.getString("user_name_owner"),
                        rs.getString("report_description")
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
    public Optional<ReportContent> getById(long reportId) {
        return jdbcTemplate.query(SELECTION+"where report_id = ?"+ORDER_BY,new Object[]{reportId},EXTRACTOR).stream().findFirst();
    }

    @Override
    public List<ReportContent> getAll(long page, long pageSize) {
        return jdbcTemplate.query(SELECTION+ORDER_BY + " LIMIT ? OFFSET ? ", new Object[]{pageSize, (page-1)*pageSize},EXTRACTOR);
    }

    @Override
    public List<ReportContent> getByFramework(long frameworkId) {
        return jdbcTemplate.query(SELECTION+"where f.framework_id = ?"+ORDER_BY,new Object[]{frameworkId},EXTRACTOR);
    }

    @Override
    public Optional<ReportContent> getByContent(long contentId) {
        String value = SELECTION+"where cr.content_id = ?"+ORDER_BY;
        return jdbcTemplate.query(value,new Object[]{contentId},EXTRACTOR).stream().findFirst();
    }

    @Override
    public void add(long contentId, long userId, String description) {
        final Map<String, Object> args = new HashMap<>();
        args.put("content_id", contentId); // la key es el nombre de la columna
        args.put("user_id",userId);
        args.put("description",description);
        jdbcInsert.executeAndReturnKey(args);
    }

    @Override
    public void delete(long reportId) {
         jdbcTemplate.update("DELETE FROM content_report WHERE report_id = ?", new Object[]{reportId});

    }

    @Override
    public void deleteByContent(long contentId) {
        jdbcTemplate.update("DELETE FROM content_report WHERE content_id = ?", new Object[]{contentId});

    }

    @Override
    public List<ReportContent> getByFrameworks(List<Long> frameworksIds, long page, long pageSize) {
        Map<String, List<Long>> params = new HashMap<>();
        params.put("framework_id", new ArrayList<>(frameworksIds));
        String query;
        query = SELECTION + " WHERE f.framework_id IN (:framework_id) " + ORDER_BY + " LIMIT " + pageSize + " OFFSET " + (page-1)*pageSize;

        return namedJdbcTemplate.query(query, params, EXTRACTOR);
    }
}
