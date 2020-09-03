package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Framework;

import java.util.List;

public interface FrameworkDao {
    Framework findById(long id);
    List<Framework> getFrameworks(String category);
}
