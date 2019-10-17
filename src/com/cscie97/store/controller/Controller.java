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
    public void update(UpdateEvent perceivedEvent)
    {
        // TODO
        
        // Delimit event string on whitespace and add each value to an array
        String[] splitEventArr = perceivedEvent.getPerceivedEvent().split("\\s+");
        
        if ((splitEventArr.length == 3) && splitEventArr[0].equals("emergency"))
        {           
            // TODO
            
            // Check that aisle is valid
            
            // Check for emergency types
            if (splitEventArr[1].equals("fire") || splitEventArr[1].equals("flood") || splitEventArr[1].equals("earthquake")
                    || splitEventArr[1].equals("armed_intruder"))
            {
                // Create new Emergency (extends Command)
                Emergency emergency = new Emergency(perceivedEvent.getSourceDevice());
            }
            
            else
            {
                System.out.println("\nEvent is inactionable.");                
                return;
            }
        }
        
        else if ((splitEventArr.length == 6) && splitEventArr[0].equals("basket_items") && (splitEventArr[2].equals("adds") || splitEventArr[2].equals("removes")))
        {
            // Check if integer input is valid
            Boolean validInts = true;
            try
            {
                Integer.parseInt(splitEventArr[4]);                
            }

            catch (NumberFormatException exception)
            {
                validInts = false;
            }
            
            if (validInts == false)
            {
                System.out.println("\nEvent is inactionable.");
                return;
            }
        }
        
        else if ((splitEventArr.length == 3) && splitEventArr[0].equals("clean"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("broken_glass"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("missing_person"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("customer_seen"))
        {
            
        }
        
        else if ((splitEventArr.length == 4) && splitEventArr[0].equals("fetch_product"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("account_balance"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("car_assist"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("checkout"))
        {
            
        }
        
        else if ((splitEventArr.length == 2) && splitEventArr[0].equals("enter_store"))
        {
            
        }
        
        else
        {
            System.out.println("\nEvent is inactionable.");
            return;
        }
    }
}