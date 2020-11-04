package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.PostTag;

import java.util.List;
import java.util.Optional;

public interface PostTagService {

    List<PostTag> getByPost(long postId);

    Optional<PostTag> insert(String tagName, long postId);
    void delete(long tagId);


}
