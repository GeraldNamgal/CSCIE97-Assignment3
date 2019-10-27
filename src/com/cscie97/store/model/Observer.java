package com.cscie97.store.model;

import com.cscie97.store.controller.UpdateEvent;

// Referenced https://www.vogella.com/tutorials/DesignPatternObserver/article.html
public interface Observer
{
    void update(UpdateEvent event);
}
