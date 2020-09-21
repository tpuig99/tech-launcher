package ar.edu.itba.paw.service;

import ar.edu.itba.paw.models.*;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public interface FrameworkService {
    Optional<Framework> findById(long frameworkId);
    List<Framework> getByCategory(FrameworkCategories category);
    List<Framework> getAll();
    List<Framework> getCompetitors(Framework framework);
    List<Framework> getByNameOrCategory(String toSearch);
}
