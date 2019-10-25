package com.cscie97.store.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.cscie97.store.model.Aisle;
import com.cscie97.store.model.Appliance;
import com.cscie97.store.model.Inventory;
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
            
            // TODO: Checking necessary here? -- Check for emergency types
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
        
        else if ((eventStrArr.length == 6) && eventStrArr[0].equals("basket_items") && (eventStrArr[2].equals("add") || eventStrArr[2].equals("remove")))
        {
            /* TODO: Checking necessary here?
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
            }*/
            
            Command basketItems = new BasketItems(event.getSourceDevice(), eventStrArr);
            basketItems.execute();
        }
        
        else if ((eventStrArr.length == 3) && eventStrArr[0].equals("clean"))
        {
            Command clean = new Clean(event.getSourceDevice(), eventStrArr);
            clean.execute();
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
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
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
                        System.out.println(appliance.getName() + ": open = " + appliance.getTurnstile().setOpen(true));
                    }
                    
                    // If device is a speaker
                    if (appliance.getType().equals("speaker"))
                    {
                        // Announce emergency
                        String announcement = "There is a " + eventStrArr[1] + " in " + store.getAisles().get(eventStrArr[2]).getName()
                                + " aisle. Please leave store immediately!";
                        
                        if (appliance.getSpeaker().announce(announcement))
                            System.out.println(appliance.getName() + ": \"" + announcement + "\"");
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
                
                if (appliance.getRobot().addressEmergency(eventStrArr[1], store.getAisles().get(eventStrArr[2]).getNumber()))                
                    System.out.println(appliance.getName() + ": Addressing " + eventStrArr[1] + " in " + store.getAisles().get(eventStrArr[2]).getName() + " aisle");
            }
            
            // If store has more than one robot
            if (robotKeys.size() > 1)
            {
                Appliance appliance; 
                for (int i = 1; i < robotKeys.size(); i++)
                {
                    appliance = (Appliance) store.getDevices().get(robotKeys.get(i));
                    
                    if (appliance.getRobot().assstLeavingCstmrs(store.getId()))                    
                        System.out.println(appliance.getName() + ": Assisting customers leaving " + store.getName());
                }
            }
        }
    }
    
    public class BasketItems extends Command
    {       
        /* Constructor */
        
        public BasketItems(Sensor sourceDevice, String[] eventStrArr)
        {
            super(sourceDevice, eventStrArr);
        }

        /* Method(s) */
        
        @Override
        public void execute()
        {
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
            // Get inventory using event's location and product info
            LinkedHashMap<String, Inventory> inventories = store.getInventories();
            Inventory inventory = null;
            
            for (Entry<String, Inventory> inventoryEntry : inventories.entrySet())
            {
                if (inventoryEntry.getValue().getLocation().equals(store.getId() + ":" + eventStrArr[5])
                        && inventoryEntry.getValue().getProductId().equals(eventStrArr[3]))
                {
                    inventory = inventoryEntry.getValue();
                    break;
                }
            }
            
            // Get customer's (virtual) basket
            modeler.getCustomerBasket(eventStrArr[1], null);
            
            System.out.println();
            
            // If customer added (rather than removed) a basket item
            if (eventStrArr[2].equals("add") && !inventory.equals(null))
            {                            
                // Update customer's virtual basket items by adding item to it
                modeler.addBasketItem(eventStrArr[1], eventStrArr[3], Integer.parseInt(eventStrArr[4]), null);
                System.out.println("Controller Service: Adding " + Integer.parseInt(eventStrArr[4]) + " of " + eventStrArr[3] + " to customer "
                        + eventStrArr[1] + "'s virtual basket");
                
                // Update inventory by decrementing product count on the shelf
                modeler.updateInventory(inventory.getInventoryId(), (Integer.parseInt(eventStrArr[4]) * (-1)), null);
                System.out.println("Controller Service: Updating inventory " + inventory.getInventoryId() + "'s count by " + Integer.parseInt(eventStrArr[4]) * (-1));                
                
                // Initialize array for getting store's robot Appliance map keys (for restocking)
                ArrayList<String> robotKeys = new ArrayList<String>();           
                
                // Iterate through all devices to find the robots (if any)
                Sensor devicePointer;
                
                for (Entry<String, Sensor> deviceEntry : store.getDevices().entrySet())
                {
                    devicePointer = deviceEntry.getValue();
                    
                    // If device is an appliance              
                    if (Appliance.containsTypeEnum(devicePointer.getType()))
                    {
                        // Cast device to an appliance
                        Appliance appliance = (Appliance) devicePointer;             
                        
                        // If device is a robot
                        if (appliance.getType().equals("robot"))
                        {
                            // Add robot device's map key to array
                            robotKeys.add(deviceEntry.getKey());                       
                        }
                    }
                }
                
                // If store has robots then command one to restock if possible
                if (robotKeys.size() > 0)
                {                
                    // Put all the storeroom aisle numbers in a hashset (for locating product in storeroom)
                    HashSet<String> storeroomAisles = new HashSet<String>();
                    
                    for (Entry<String, Aisle> aisleEntry : store.getAisles().entrySet())
                    {
                        if (aisleEntry.getValue().getLocation().toString().equals("storeroom"))
                            storeroomAisles.add(aisleEntry.getValue().getNumber());
                    }
                    
                    // If there were any storeroom aisles
                    if (storeroomAisles.size() > 0)
                    {
                        // Collect storeroom-only inventories that have the given productId and has supply
                        ArrayList<String> storeroomInvIds = new ArrayList<String>();
                     
                        for (Entry<String, Inventory> inventoryEntry : inventories.entrySet())
                        {
                            if (inventoryEntry.getValue().getProductId().equals(eventStrArr[3])
                                    && storeroomAisles.contains(inventoryEntry.getValue().getLocation().split(":")[1])
                                    && (inventoryEntry.getValue().getCount() > 0))
                            {                        
                                storeroomInvIds.add(inventoryEntry.getValue().getInventoryId());
                            }
                        }                       
                                               
                        // If product was found in storeroom
                        if (storeroomInvIds.size() > 0)
                        {
                            // TODO
                            
                            // Have a robot restock; Update floor and storeroom inventories
                            Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));    
                            
                            // Call robot appliance's restock method (returns a boolean)
                            if (appliance.getRobot().restock(eventStrArr[3], (inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[1]
                                    + inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[2]), eventStrArr[5]))
                            {
                                System.out.println(appliance.getName() + ": Restocking " + eventStrArr[3] + " from "
                                        + (inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[1] + ":"
                                                + inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[2]) + " (storeroom) "
                                        + "to " + eventStrArr[5] + " (floor)");                    
                                
                                // TODO: Make sure that if less than update amount then get all of the supply -- Update storeroom location's inventory
                                modeler.updateInventory(inventories.get(storeroomInvIds.get(0)).getInventoryId(), (Integer.parseInt(eventStrArr[4]) * (-1)), null);
                                System.out.println("Controller Service: Updating inventory " + inventories.get(storeroomInvIds.get(0)).getInventoryId()
                                        + "'s count by " + Integer.parseInt(eventStrArr[4]) * (-1));                
    
                                // TODO: Make sure update amount matches with above (after do less than update amount stuff) -- Update floor location's inventory
                                modeler.updateInventory(inventory.getInventoryId(), Integer.parseInt(eventStrArr[4]), null);
                                System.out.println("Controller Service: Updating inventory " + inventory.getInventoryId() + "'s count by " + Integer.parseInt(eventStrArr[4]));                
                            }
                        }
                        
                        // Else there's no product to restock with
                        else
                        {
                            // TODO
                        }
                        
                    }
                        
                    // Else there are no storeroom aisles; can't restock
                    else
                    {
                        // TODO
                    }
                }
                
                // TODO (Exception?): Else there are no robots found that can restock
                else
                {
                    
                }
            }
            
            // Else If customer removed (rather than added) a basket item
            else if (eventStrArr[2].equals("remove") && !inventory.equals(null))
            {
                // Update basket items by removing item from it
                modeler.removeBasketItem(eventStrArr[1], eventStrArr[3], Integer.parseInt(eventStrArr[4]), null);
                System.out.println("Controller Service: Removing " + Integer.parseInt(eventStrArr[4]) + " of " + eventStrArr[3] + " from customer " + eventStrArr[1] + "'s virtual basket");
            }                
        }
    }
    
    public class Clean extends Command
    {       
        /* Constructor */
        
        public Clean(Sensor sourceDevice, String[] eventStrArr)
        {
            super(sourceDevice, eventStrArr);
        }

        /* Method(s) */
        
        @Override
        public void execute()
        {
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);                
            
            // Initialize array for getting store's device map's robot type appliance keys
            ArrayList<String> robotKeys = new ArrayList<String>();           
            
            // Iterate through devices and find robots
            Sensor devicePointer;
            for (Entry<String, Sensor> deviceEntry : store.getDevices().entrySet())
            {
                devicePointer = deviceEntry.getValue();
                
                // Check if device is an appliance              
                if (Appliance.containsTypeEnum(devicePointer.getType()))
                {
                    Appliance appliance = (Appliance) devicePointer;             
                    
                    // If device is a robot
                    if (appliance.getType().equals("robot"))
                    {
                        // Add hash map's robot key to array
                        robotKeys.add(deviceEntry.getKey());                       
                    }
                }
            }
            
            // If store has a robot, tell it to clean up the product
            if (robotKeys.size() > 0)
            {
                Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));                
                
                if (appliance.getRobot().clean(eventStrArr[1], eventStrArr[2]))
                {
                    System.out.println();
                    System.out.println(appliance.getName() + ": Cleaning " + modeler.getProducts().get(eventStrArr[1]).getName()+ " in "
                            + store.getAisles().get(eventStrArr[2]).getName() + " aisle");
                }
            }     
        }
    }
}