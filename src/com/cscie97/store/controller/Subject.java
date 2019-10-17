package com.cscie97.store.controller;

import com.cscie97.store.model.Sensor;

// Referenced https://www.vogella.com/tutorials/DesignPatternObserver/article.html
public interface Subject
{
    void registerObserver(Observer newObserver);
    void deregisterObserver(Observer observerToRemove);
    void notifyObservers(Sensor sourceDevice, String eventToSend);
}
