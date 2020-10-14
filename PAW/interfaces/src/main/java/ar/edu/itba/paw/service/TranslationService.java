package ar.edu.itba.paw.service;

import java.util.Map;

public interface TranslationService {

    String getCategory(String categoryName);
    Map<String, String> getAllCategories();
    String getType(String typeName);
    Map<String, String> getAllTypes();
}
