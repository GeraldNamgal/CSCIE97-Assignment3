package com.cscie97.store.controller;

import com.cscie97.store.model.Sensor;

public abstract class Command
{
    /* Variables */
    
    protected Sensor sourceDevice;    
    
    /* Constructor */
    
    public Command(Sensor sourceDevice)
    {
        this.sourceDevice = sourceDevice;               
    }
    
    /* Methods */
    
    public abstract void execute();      
}
