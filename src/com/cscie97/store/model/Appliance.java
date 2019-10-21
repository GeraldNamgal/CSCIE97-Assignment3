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

        public void setOpen(boolean trueOrFalse)
        {
            open = trueOrFalse;
            System.out.println(getName() + ": open == " + isOpen());
        }       
    }
    
    public class Speaker
    {
        // TODO
        
        public void announce(String expression)
        {
            System.out.println(getName() + ": " + expression);
        }
    }
    
    public class Robot
    {
        // TODO
        
        public void addressEmergency(String emergency, String aisleName)
        {
            System.out.println(getName() + ": Addressing " + emergency + " in " + aisleName + " aisle");
        }
        
        public void assstLeavingCstmrs(String storeName)
        {
            System.out.println(getName() + ": Assisting customers leaving " + storeName);
        }
        
        public boolean restock(String location, Product product)
        {
            // TODO
            
            return true;
        }
        
        public void clean()
        {
            // TODO
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
