package com.cscie97.store.controller;

import com.cscie97.store.model.Sensor;

public abstract class Command
{
    /* Variables */
    
    protected Sensor sourceDevice;
    protected String[] eventStrArr;
    
    /* Constructor */
    
    public Command(Sensor sourceDevice, String[] eventStrArr)
    {
        this.sourceDevice = sourceDevice;
        this.eventStrArr = eventStrArr;        
    }
    
    /* Methods */
    
    public abstract void execute();      
}
