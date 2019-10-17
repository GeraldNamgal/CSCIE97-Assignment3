package com.cscie97.store.controller;

// Referenced https://www.vogella.com/tutorials/DesignPatternObserver/article.html
public interface Observer
{
    void update(UpdateEvent event);
}
