package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.PostTag;

import java.util.List;

public interface PostTagDao {

    List<PostTag> getByPost(long postId);

    PostTag insert(String tagName, long postId);
    void delete(long tagId);

}
