package com.epam.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.epam.config.TextProperties;
import com.epam.service.DictionaryService;

@Component
public class DictionaryLifeCycleListener {

    @Autowired
    private DictionaryService service;

    @Autowired
    private TextProperties properties;

    @EventListener(ContextRefreshedEvent.class)
    public void initializeDictionary() {
        service.initDictionary(properties.getFilePaths());
    }

    @EventListener(ContextClosedEvent.class)
    public void destroyDictionary() {
        service.destroyDictionary();
    }
}
