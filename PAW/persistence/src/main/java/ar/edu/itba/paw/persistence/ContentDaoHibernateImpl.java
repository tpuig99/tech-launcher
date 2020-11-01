package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Content;
import ar.edu.itba.paw.models.ContentTypes;
import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ContentDaoHibernateImpl implements  ContentDao{
    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<Content> getById(long contentId) {
        return Optional.ofNullable(em.find(Content.class, contentId));
    }

    @Override
    public List<Content> getContentByFrameworkAndType(long frameworkId, ContentTypes type, long page, long pageSize) {
        final TypedQuery<Content> query = em.createQuery("FROM Content c WHERE c.framework.id = :frameworkId and c.type = :type", Content.class);
        query.setParameter("frameworkId",frameworkId);
        query.setParameter("type",type);
        query.setMaxResults((int) pageSize);
        query.setFirstResult((int) ((page-1)*pageSize));
        return query.getResultList();
    }

    @Override
    public List<Content> getContentByUser(long userId, long page, long pagesize) {
        final TypedQuery<Content> query = em.createQuery("FROM Content c WHERE c.user.id = :userId", Content.class);
        query.setParameter("userId",userId);
        query.setMaxResults((int) pagesize);
        query.setFirstResult((int) ((page-1)*pagesize));
        return query.getResultList();
    }

    @Override
    public Optional<Long> getContentCountByUser(long userId) {
        final TypedQuery<Long> query = em.createQuery("SELECT count(c.contentId) FROM Content c WHERE c.user.id = :userId", Long.class );
        query.setParameter("userId",userId);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public List<Content> getContentByFrameworkAndTypeAndTitle(long frameworkId, ContentTypes type, String title) {
        final TypedQuery<Content> query = em.createQuery("FROM Content c WHERE c.framework.id = :frameworkId and c.type = :type and c.title = :title", Content.class);
        query.setParameter("frameworkId", frameworkId);
        query.setParameter("type", type);
        query.setParameter("title", title);
        return query.getResultList();
    }

    @Override
    public Content insertContent(long frameworkId, long userId, String title, String link, ContentTypes type) {
        Content content = new Content(em.getReference(Framework.class,frameworkId),em.getReference(User.class,userId),title,link,type);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        content.setTimestamp(ts);
        em.persist(content);
        return content;
    }

    @Override
    public int deleteContent(long contentId) {
        em.remove(em.find(Content.class, contentId));
        return 1;
    }

    @Override
    public Optional<Content> changeContent(long contentId, String title, String link, ContentTypes type) {
        Content content = em.find(Content.class, contentId);
        content.setTitle(title);
        content.setLink(link);
        content.setType(type);
        return Optional.empty();
    }
}
