package com.cscie97.store.controller;

import com.cscie97.store.model.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Controller implements PropertyChangeListener
{
    public Controller(StoreModelService modeler)
    {
        modeler.addChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        
    }
}