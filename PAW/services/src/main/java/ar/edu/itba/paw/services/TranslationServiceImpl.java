package ar.edu.itba.paw.services;

import ar.edu.itba.paw.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationServiceImpl implements TranslationService {

    @Autowired
    MessageSource messageSource;


    public String getCategory(String categoryName) {
        return getAllCategories().get(categoryName);
    }

    public Map<String, String> getAllCategories() {
        Map<String, String> toReturn = new HashMap<>();
        toReturn.put("Back-End Development",messageSource.getMessage("category.back_end",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Big Data",messageSource.getMessage("category.big_data",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Business",messageSource.getMessage("category.business",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Artificial Intelligence",messageSource.getMessage("category.artificial_intelligence",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Networking",messageSource.getMessage("category.networking",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Security",messageSource.getMessage("category.security",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Front-End Development",messageSource.getMessage("category.front_end",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Platforms",messageSource.getMessage("category.platforms",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Gaming",messageSource.getMessage("category.gaming",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Editors",messageSource.getMessage("category.editors",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Development Environment",messageSource.getMessage("category.development",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Databases",messageSource.getMessage("category.databases",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Media",messageSource.getMessage("category.media",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Functional Programming",messageSource.getMessage("category.functional",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Imperative Programming",messageSource.getMessage("category.imperative",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Object Oriented Programming",messageSource.getMessage("category.object_oriented",new Object[]{}, LocaleContextHolder.getLocale()));
        return toReturn;
    }

    public String getType(String typeName) {
        return getAllTypes().get(typeName);
    }

    public Map<String, String> getAllTypes() {
        Map<String, String> toReturn = new HashMap<>();
        toReturn.put("Online Platform",messageSource.getMessage("type.online_platform",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Framework",messageSource.getMessage("type.framework",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Service",messageSource.getMessage("type.service",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Database System",messageSource.getMessage("type.database_system",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Programming Language",messageSource.getMessage("type.programming_language",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Operating System",messageSource.getMessage("type.operating_system",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Runtime Platform",messageSource.getMessage("type.runtime_platform",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Libraries",messageSource.getMessage("type.libraries",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Engine",messageSource.getMessage("type.engine",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Shell",messageSource.getMessage("type.shell",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Terminal",messageSource.getMessage("type.terminal",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Application",messageSource.getMessage("type.application",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Text Editor",messageSource.getMessage("type.text_editor",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("CSS Modifier",messageSource.getMessage("type.css_modifier",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("API",messageSource.getMessage("type.api",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("Toolkit",messageSource.getMessage("type.toolkit",new Object[]{}, LocaleContextHolder.getLocale()));
        toReturn.put("IDE",messageSource.getMessage("type.ide",new Object[]{}, LocaleContextHolder.getLocale()));
        return toReturn;
    }
}
