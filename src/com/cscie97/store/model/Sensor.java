/* *
 * Gerald Arocena
 * CSCI E-97
 * Professor: Eric Gieseke
 * Assignment 2
 */

package com.cscie97.store.model;

/* *
 * Sensor class that represents a sensor that's in a store
 */
public class Sensor
{
    /* API Variables */
    
    /* *
     * Types of valid sensors
     */
    enum Type 
    { 
        microphone, camera; 
    }
    
    private String id;
    private String name;
    private String type;
    private String location; 
    
    /* Constructor */
    
    /* *
     * Creates a new Sensor
     * @param type Type of sensor (enum of valid sensors shown above)
     * @param location Location of sensor (e.g., store1:aisle1)
     */
    public Sensor(String id, String name, String type, String location)
    {
        this.id = id;
        this.name = name;
        this.type = type;
        this.location = location;        
    }
    
    /* API Methods */
    
    /* *
     * TODO: Receives events and notifies observers
     */
    public String event(String perceivedEvent)
    {
        String eventToSend = perceivedEvent;
        
        return eventToSend;
    }
    
    /* Utility Methods */
 
    /* *
     *  Checks a string if it's a Type enum
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
    
    /* Getters and Setter */

    public String getId()
    {
        return id;
    }    

    public String getName()
    {
        return name;
    }   

    public String getType()
    {
        return type;
    }  

    public String getLocation()
    {
        return location;
    }     
}
