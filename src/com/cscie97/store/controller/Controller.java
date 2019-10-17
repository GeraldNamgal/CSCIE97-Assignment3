package com.cscie97.store.controller;

import com.cscie97.store.model.StoreModelService;

/* *
 * Controller implements the Observer interface (i.e., the observer in the observer pattern).
 */
public class Controller implements Observer
{
    /* Constructor */
    
    public Controller(StoreModelService modeler)
    {
        // Register Controller with Model Service
        modeler.registerObserver(this);
    }

    /* API Method(s) */
    
    @Override
    public void update(UpdateEvent event)
    {
        // TODO
        
        // TODO: Debugging -- Print event to stdout
        
    }
}