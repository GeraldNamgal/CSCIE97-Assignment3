/* *
 * Gerald Arocena
 * CSCI E-97
 * Professor: Eric Gieseke
 * Assignment 2
 */

package com.cscie97.store.model;

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
        
    /* Constructor */
    
    /* *
     * Creates a new Appliance
     * @param type Type of appliance (e.g., speaker, robot, turnstile; enum of valid appliances shown above)
     * @param location The location of the appliance (e.g., aisle 2 in store 1)
     */
    public Appliance(String id, String name, String type, String location)
    {
        super(id, name, type, location);         
    }
    
    /* API Methods */
    
    /* *
     * TODO: Receives appliance commands
     */
    public void command(String command)
    {
        
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
}
