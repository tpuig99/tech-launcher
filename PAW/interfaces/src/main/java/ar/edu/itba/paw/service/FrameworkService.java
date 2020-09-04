package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.Framework;
import ar.edu.itba.paw.models.FrameworkCategories;

import java.util.List;

public interface FrameworkService {
    Framework findById(long id);
    List<Framework> getFrameworks(FrameworkCategories category);
}
