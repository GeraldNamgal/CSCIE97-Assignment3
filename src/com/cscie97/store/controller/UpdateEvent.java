package com.cscie97.store.controller;

import com.cscie97.store.model.Sensor;

// Referenced https://www.vogella.com/tutorials/DesignPatternObserver/article.html
public class UpdateEvent
{
    Sensor sourceDevice;
    String perceivedEvent;
    
    public UpdateEvent(Sensor sourceDevice, String eventToSend)
    {
        this.sourceDevice = sourceDevice;
        this.perceivedEvent = eventToSend;
    }

    /* Getters and Setters */
    
    public Sensor getSourceDevice()
    {
        return sourceDevice;
    }   

    public String getPerceivedEvent()
    {
        return perceivedEvent;
    }     
}