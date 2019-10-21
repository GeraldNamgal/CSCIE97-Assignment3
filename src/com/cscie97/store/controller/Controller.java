package com.cscie97.store.controller;

import java.util.ArrayList;
import java.util.Map.Entry;

import com.cscie97.store.model.Appliance;
import com.cscie97.store.model.Sensor;
import com.cscie97.store.model.Store;
import com.cscie97.store.model.StoreModelService;

/* *
 * Controller implements the Observer interface (i.e., the observer in the observer pattern).
 */
public class Controller implements Observer
{
    /* Variables */
    
    private StoreModelService modeler;
    // TODO: Log rule executions and resulting actions
    private String logger;
    
    /* Constructor */
    
    public Controller(Subject modeler)
    {       
        // Register Controller with Model Service
        modeler.registerObserver(this);
        
        this.modeler = (StoreModelService) modeler;
    }

    /* API Method(s) */
    
    @Override
    public void update(UpdateEvent event)
    {
        // TODO: In progress
        
        // Delimit event string on whitespace and add each value to an array
        String[] eventStrArr = event.getPerceivedEvent().split("\\s+");
        
        if ((eventStrArr.length == 3) && eventStrArr[0].equals("emergency"))
        {           
            // TODO: In progress
            
            // TODO: Validate location? (Unnecessary? Or do in Model Service?)            
            
            // Check for emergency types
            if (eventStrArr[1].equals("fire") || eventStrArr[1].equals("flood") || eventStrArr[1].equals("earthquake")
                    || eventStrArr[1].equals("armed_intruder"))
            {
                // TODO: Create new Emergency               
                Command emergency = new Emergency(event.getSourceDevice(), eventStrArr);
                
                // Run the Command's execute method
                emergency.execute();
            }
            
            else
            {
                System.out.println("\nEvent is not recognized.");                
                return;
            }
        }
        
        else if ((eventStrArr.length == 6) && eventStrArr[0].equals("basket_items") && (eventStrArr[2].equals("adds") || eventStrArr[2].equals("removes")))
        {
            // Check if integer input is valid
            Boolean validInts = true;
            try
            {
                Integer.parseInt(eventStrArr[4]);                
            }

            catch (NumberFormatException exception)
            {
                validInts = false;
            }
            
            if (validInts == false)
            {
                System.out.println("\nEvent is not recognized.");
                return;
            }
        }
        
        else if ((eventStrArr.length == 3) && eventStrArr[0].equals("clean"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("broken_glass"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("missing_person"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("customer_seen"))
        {
            
        }
        
        else if ((eventStrArr.length == 4) && eventStrArr[0].equals("fetch_product"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("account_balance"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("car_assist"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("checkout"))
        {
            
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("enter_store"))
        {
            
        }
        
        else
        {
            System.out.println("\nEvent is not recognized.");
            return;
        }
    }
    
    /* Nested Classes */
    
    public class Emergency extends Command
    {       
        /* Constructor */
        
        public Emergency(Sensor sourceDevice, String[] eventStrArr)
        {
            super(sourceDevice, eventStrArr);
        }

        /* Method(s) */
        
        @Override
        public void execute()
        {
            // TODO
            
            // Get the source store
            Store store = modeler.getStore(sourceDevice.getLocation().split(":")[0], null);
            
            // Initialize array for getting store's device map's robot type appliance keys
            ArrayList<String> robotKeys = new ArrayList<String>();
            
            System.out.println();
            
            // Iterate through devices and perform type-specific actions
            Sensor devicePointer;
            for (Entry<String, Sensor> deviceEntry : store.getDevices().entrySet())
            {
                devicePointer = deviceEntry.getValue();
                
                // Check if device is an appliance              
                if (Appliance.containsTypeEnum(devicePointer.getType()))
                {
                    Appliance appliance = (Appliance) devicePointer;
                
                    // If device is a turnstile
                    if (appliance.getType().equals("turnstile"))
                    {                    
                        // Open turnstile
                        appliance.getTurnstile().setOpen(true);                        
                    }
                    
                    // If device is a speaker
                    if (appliance.getType().equals("speaker"))
                    {
                        // Announce emergency
                        appliance.getSpeaker().announce("\"There is a " + eventStrArr[1] + " in " + store.getAisles().get(eventStrArr[2]).getName()
                                + " aisle. Please leave store immediately!\"");
                    }
                    
                    // If device is a robot
                    if (appliance.getType().equals("robot"))
                    {
                        // Add hash map's robot key to array
                        robotKeys.add(deviceEntry.getKey());                       
                    }
                }
            }
            
            // If store has a robot
            if (robotKeys.size() > 0)
            {
                Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));                
                appliance.getRobot().addressEmergency(eventStrArr[1], store.getAisles().get(eventStrArr[2]).getName());
            }
            
            // If store has more than one robot
            if (robotKeys.size() > 1)
            {
                Appliance appliance; 
                for (int i = 1; i < robotKeys.size(); i++)
                {
                    appliance = (Appliance) store.getDevices().get(robotKeys.get(i));
                    appliance.getRobot().assstLeavingCstmrs(store.getName());
                }
            }
        }
    }
}