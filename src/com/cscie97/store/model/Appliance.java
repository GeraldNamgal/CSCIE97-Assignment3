/* *
 * Gerald Arocena
 * CSCI E-97
 * Professor: Eric Gieseke
 * Assignment 2
 */

package com.cscie97.store.model;

import java.util.ArrayList;

/* *
 * Appliance class that represents an appliance in a store. Extends Sensor class
 */
public class Appliance extends Sensor
{
    /* API Variables */  

    /* *
     * Types of valid appliances
     */
    enum Type 
    { 
        speaker, robot, turnstile; 
    }
    
    /* My Variables */
    
    ArrayList<String> commands;
        
    /* Constructor */
    
    /* *
     * Creates a new Appliance
     * @param type Type of appliance (e.g., speaker, robot, turnstile; enum of valid appliances shown above)
     * @param location The location of the appliance (e.g., aisle 2 in store 1)
     */
    public Appliance(String id, String name, String type, String location)
    {
        super(id, name, type, location);   
        
        commands = new ArrayList<String>();
    }
    
    /* API Methods */
    
    /* *
     * Sends appliance commands. Outputs to stdout the received command
     */
    public void command(String command)
    {
        System.out.println("\nCommand \"" + command + "\" was received by device " + this.getId() + "!");
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

    public ArrayList<String> getCommands()
    {
        return commands;
    }       
}
