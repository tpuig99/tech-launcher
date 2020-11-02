package ar.edu.itba.paw.services;

import ar.edu.itba.paw.models.PostTag;
import ar.edu.itba.paw.persistence.PostTagDao;
import ar.edu.itba.paw.service.PostTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostTagServiceImpl implements PostTagService {

    @Autowired
    PostTagDao postTagDao;

    @Override
    public List<PostTag> getByPost(long postId) {
        return postTagDao.getByPost(postId);
    }

    @Override
    public PostTag insert(String tagName, long postId) {
        return postTagDao.insert(tagName, postId);
    }

    @Override
    public void delete(long tagId) {
        postTagDao.delete(tagId);

    }
}
