package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PostTag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostTagDaoImpl implements PostTagDao{
    @Override
    public List<PostTag> getByPost(long postId) {
        return null;
    }

    @Override
    public PostTag insert(String tagName, long postId) {
        return null;
    }

    @Override
    public void delete(long tagId) {

    }
}
