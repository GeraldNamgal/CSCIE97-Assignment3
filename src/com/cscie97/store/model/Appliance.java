/* *
 * Gerald Arocena
 * CSCI E-97
 * Professor: Eric Gieseke
 * Assignment 3
 */

package com.cscie97.store.model;

/* *
 * Appliance class that represents an appliance in a store. Extends Sensor class
 */
public class Appliance extends Sensor
{
    /* Variables */  

    /* *
     * Types of valid appliances
     */
    enum Type 
    { 
        speaker, robot, turnstile; 
    }
    
    private Turnstile turnstile;
    private Speaker speaker;
    private Robot robot;
        
    /* Constructor */
    
    /* *
     * Creates a new Appliance
     * @param type Type of appliance (e.g., speaker, robot, turnstile; enum of valid appliances shown above)
     * @param location The location of the appliance (e.g., aisle 2 in store 1)
     */
    public Appliance(String id, String name, String type, String location)
    {
        super(id, name, type, location);
        
        if (type.equals("turnstile"))
        {
            turnstile = new Turnstile();
        }
        
        if (type.equals("robot"))
        {
            robot = new Robot();
        }
        
        if (type.equals("speaker"))
        {
            speaker = new Speaker();
        }
    }
    
    /* API Methods */
    
    /* *
     * Receives appliance commands
     */
    public void command(String command)
    {
        // TODO: Necessary or delete? Maybe if have time
    }
    
    /* Nested Classes */   
    
    public class Turnstile
    {       
        private boolean open = false;
        
        public boolean isOpen()
        {
            return open;
        }

        public boolean setOpen(boolean trueOrFalse)
        {
            open = trueOrFalse;
            return open;
        }       
    }
    
    public class Speaker
    {
        public boolean announce(String expression)
        {
            boolean announcing = true;            
            return announcing;
        }
    }
    
    public class Robot
    {
        // TODO
        
        public boolean addressEmergency(String emergency, String aisle)
        {
            boolean addressingEmergency = true;
            return addressingEmergency;
        }
        
        public boolean assstLeavingCstmrs(String store)
        {
            boolean assstingLeavingCstmrs = true;
            return assstingLeavingCstmrs;
        }
        
        public boolean restock(String product, String fromLocation, String toLocation)
        {
            boolean restocking = true;            
            return restocking;
        }
        
        public boolean clean(String product, String aisle)
        {
            // TODO
            
            boolean cleaning = true;
            return cleaning;
        }
    }
    
    /* Utility Methods */
 
    /* *
     * Checks a string if it's a Type enum
     */
    public static boolean containsTypeEnum(String testString)
    {
        for (Type type : Type.values())
        {
            if (type.name().equals(testString))
            {
                return true;
            }
        }

        return false;
    }

    /* Getters and Setters */
    
    public Turnstile getTurnstile()
    {
        return turnstile;
    }    

    public Speaker getSpeaker()
    {
        return speaker;
    }    

    public Robot getRobot()
    {
        return robot;
    }           
}
