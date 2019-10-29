package com.cscie97.store.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import com.cscie97.store.model.Aisle;
import com.cscie97.store.model.Appliance;
import com.cscie97.store.model.Basket;
import com.cscie97.store.model.Customer;
import com.cscie97.store.model.Inventory;
import com.cscie97.store.model.Observer;
import com.cscie97.store.model.Sensor;
import com.cscie97.store.model.Store;
import com.cscie97.store.model.StoreModelService;
import com.cscie97.store.model.Subject;

/* *
 * Controller implements the Observer interface (i.e., the observer in the observer pattern).
 */
public class Controller implements Observer
{
    /* Variables */
    
    private StoreModelService modeler;
    private com.cscie97.store.ledger.CommandProcessor ledgerCp;
    // TODO: Log rule executions and resulting actions
    private String logger;
    
    /* Constructor */
    
    public Controller(Subject modeler, com.cscie97.store.ledger.CommandProcessor ledgerCp)
    {       
        // Register Controller with Model Service
        modeler.registerObserver(this);
        
        this.modeler = (StoreModelService) modeler;
        this.ledgerCp = ledgerCp;
    }

    /* API Method(s) */
    
    @Override
    public void update(UpdateEvent event)
    {
        // TODO: In progress
        
        // Delimit event string on whitespace and add each value to an array
        String[] eventStrArr = event.getPerceivedEvent();
        
        if ((eventStrArr.length == 3) && eventStrArr[0].equals("emergency"))
        {      
            // TODO: Validate location? (Unnecessary? Or do in Model Service?)            
            
            // TODO: Checking necessary here? -- Check for emergency types
            if (eventStrArr[1].equals("fire") || eventStrArr[1].equals("flood") || eventStrArr[1].equals("earthquake")
                    || eventStrArr[1].equals("armed_intruder"))
            {
                // Create new Emergency               
                Command emergency = new Emergency(event.getSourceDevice(), eventStrArr[1], eventStrArr[2]);
                
                // Run the Command's execute method
                emergency.execute();
            }
            
            else
            {
                // TODO (???): Add an Exception
                
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
                // TODO (???): Add an Exception
                
                System.out.println("\nEvent is not recognized.");
                return;
            }*/
            
            Command basketItems = new BasketItems(event.getSourceDevice(), eventStrArr[1], eventStrArr[2], eventStrArr[3], eventStrArr[4], eventStrArr[5]);
            basketItems.execute();
        }
        
        else if ((eventStrArr.length == 3) && eventStrArr[0].equals("clean"))
        {
            Command clean = new Clean(event.getSourceDevice(), eventStrArr[1], eventStrArr[2]);
            clean.execute();
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("broken_glass"))
        {
            Command brokenGlass = new BrokenGlass(event.getSourceDevice(), eventStrArr[1]);
            brokenGlass.execute();
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("missing_person"))
        {
            Command missingPerson = new MissingPerson(event.getSourceDevice(), eventStrArr[1]);
            missingPerson.execute();
        }
        
        else if ((eventStrArr.length == 4) && eventStrArr[0].equals("customer_seen"))
        {
            Command customerSeen = new CustomerSeen(event.getSourceDevice(), eventStrArr[1], eventStrArr[2], eventStrArr[3]);
            customerSeen.execute();
        }
        
        else if ((eventStrArr.length == 5) && eventStrArr[0].equals("fetch_product"))
        {
            Command fetchProduct = new FetchProduct(event.getSourceDevice(), eventStrArr[1], Integer.parseInt(eventStrArr[2]), eventStrArr[3], eventStrArr[4]);
            fetchProduct.execute();
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("account_balance"))
        {
            Command accountBalance = new AccountBalance(event.getSourceDevice(), eventStrArr[1]);
            accountBalance.execute();
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("car_assist"))
        {
            Command carAssist = new CarAssist(event.getSourceDevice(), eventStrArr[1]);
            carAssist.execute();
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("checkout"))
        {
            Command checkout = new Checkout(event.getSourceDevice(), eventStrArr[1]);
            checkout.execute();
        }
        
        else if ((eventStrArr.length == 2) && eventStrArr[0].equals("enter_store"))
        {
            Command enterStore = new EnterStore(event.getSourceDevice(), eventStrArr[1]);
            enterStore.execute();
        }
        
        else
        {
            // TODO (???): Add an Exception
            
            System.out.println("\nEvent is not recognized.");
            return;
        }
    }
    
    /* Nested Classes */
    
    public class Emergency extends Command
    {       
        /* Variables */
        
        private String eventType;
        private String aisleNumber;
        
        /* Constructor */
        
        public Emergency(Sensor sourceDevice, String eventType, String aisleNumber)
        {
            super(sourceDevice);
            
            this.eventType = eventType;
            this.aisleNumber = aisleNumber;
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
                        String announcement = "There is a " + eventType + " in " + store.getAisles().get(aisleNumber).getName()
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
                
                if (appliance.getRobot().addressEmergency(eventType, store.getAisles().get(aisleNumber).getNumber()))                
                    System.out.println(appliance.getName() + ": Addressing " + eventType + " in " + store.getAisles().get(aisleNumber).getName() + " aisle");
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
        
        private String customerId;
        private String addOrRemove;
        private String productId;
        private String number;
        private String aisleShelfLoc;
        
        public BasketItems(Sensor sourceDevice, String customerId, String addOrRemove, String productId, String number, String aisleShelfLoc)
        {
            super(sourceDevice);
            
            this.customerId = customerId;
            this.addOrRemove = addOrRemove;
            this.productId = productId;
            this.number = number;
            this.aisleShelfLoc = aisleShelfLoc;
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
                if (inventoryEntry.getValue().getLocation().equals(store.getId() + ":" + aisleShelfLoc)
                        && inventoryEntry.getValue().getProductId().equals(productId))
                {
                    inventory = inventoryEntry.getValue();
                    break;
                }
            }
            
            // Get customer's (virtual) basket
            modeler.getCustomerBasket(customerId, null);
            
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
            
            System.out.println();
            
            // If event was customer added (rather than removed) a basket item
            if (addOrRemove.equals("add") && !inventory.equals(null))
            {                            
                // Update customer's virtual basket items by adding item to it
                System.out.println("Controller Service: Adding " + Integer.parseInt(number) + " of " + productId + " to customer "
                        + customerId + "'s virtual basket");
                modeler.addBasketItem(customerId, productId, Integer.parseInt(number), null);                
                
                // Update inventory by decrementing product count on the shelf
                System.out.println("Controller Service: Updating inventory " + inventory.getInventoryId() + "'s count by " + Integer.parseInt(number) * (-1));                
                modeler.updateInventory(inventory.getInventoryId(), (Integer.parseInt(number) * (-1)), null);
                             
                // If store has robots then command one to restock what customer removed
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
                            if (inventoryEntry.getValue().getProductId().equals(productId)
                                    && storeroomAisles.contains(inventoryEntry.getValue().getLocation().split(":")[1])
                                    && (inventoryEntry.getValue().getCount() > 0))
                            {                        
                                storeroomInvIds.add(inventoryEntry.getValue().getInventoryId());
                            }
                        }                       
                                               
                        // If product was found in storeroom
                        if (storeroomInvIds.size() > 0)
                        {
                            // Have a robot restock; Update floor and storeroom inventories
                            Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));    
                            
                            // Call robot appliance's restock method (returns a boolean)
                            if (appliance.getRobot().restock(productId, (inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[1]
                                    + inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[2]), aisleShelfLoc))
                            {
                                System.out.println(appliance.getName() + ": Restocking " + productId + " from "
                                        + (inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[1] + ":"
                                                + inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[2]) + " (storeroom) "
                                        + "to " + aisleShelfLoc);                    
                                
                                // If storeroom product supply is less than update amount then get all of the supply for restock
                                Integer updateAmount = Integer.parseInt(number);
                                if (inventories.get(storeroomInvIds.get(0)).getCount() < updateAmount)
                                    updateAmount = inventories.get(storeroomInvIds.get(0)).getCount();
                                
                                // Update storeroom location's inventory
                                System.out.println("Controller Service: Updating inventory " + inventories.get(storeroomInvIds.get(0)).getInventoryId()
                                        + "'s (storeroom) count by " + (updateAmount * (-1)));
                                modeler.updateInventory(inventories.get(storeroomInvIds.get(0)).getInventoryId(), (updateAmount * (-1)), null);                                                
    
                                // Update floor location's inventory
                                System.out.println("Controller Service: Updating inventory " + inventory.getInventoryId() + "'s count by " + updateAmount);
                                modeler.updateInventory(inventory.getInventoryId(), updateAmount, null);                                                
                            }
                        }
                        
                        // Else there's no product to restock with
                        else
                        {
                            // TODO (???): Add an Exception
                            
                            return;
                        }
                        
                    }
                        
                    // Else there are no storeroom aisles; can't restock
                    else
                    {
                        // TODO (???): Add an Exception
                        
                        return;
                    }
                }
                
                // TODO (Exception?): Else there are no robots found that can restock
                else
                {
                    // TODO (???): Add an Exception
                    
                    return;
                }
            }
            
            // Else if event was customer removed (rather than added) a basket item
            else if (addOrRemove.equals("remove") && !inventory.equals(null))
            {
                // Update basket items by removing item from it
                System.out.println("Controller Service: Removing " + Integer.parseInt(number) + " of " + productId + " from customer " + customerId + "'s virtual basket");                
                modeler.removeBasketItem(customerId, productId, Integer.parseInt(number), null);
                
                // If there's room on the shelf to put back items, add them back to shelf
                if ((inventory.getCapacity() - inventory.getCount()) >= Integer.parseInt(number))
                    modeler.updateInventory(inventory.getInventoryId(), Integer.parseInt(number), null);
                
                // Else if there's no room on the floor shelf, put back on storeroom shelf
                else
                {
                    // Put all the storeroom aisle numbers in a hashset (for locating product storage in storeroom)
                    HashSet<String> storeroomAisles = new HashSet<String>();
                    
                    for (Entry<String, Aisle> aisleEntry : store.getAisles().entrySet())
                    {
                        if (aisleEntry.getValue().getLocation().toString().equals("storeroom"))
                            storeroomAisles.add(aisleEntry.getValue().getNumber());
                    }
                    
                    // If there were any storeroom aisles
                    if (storeroomAisles.size() > 0)
                    {
                        // Collect storeroom-only inventories that have the given productId and have shelf room
                        ArrayList<String> storeroomInvIds = new ArrayList<String>();
                     
                        for (Entry<String, Inventory> inventoryEntry : inventories.entrySet())
                        {
                            if (inventoryEntry.getValue().getProductId().equals(productId)
                                    && storeroomAisles.contains(inventoryEntry.getValue().getLocation().split(":")[1])
                                    && ((inventoryEntry.getValue().getCapacity() - inventoryEntry.getValue().getCount()) >= Integer.parseInt(number)))
                            {                        
                                storeroomInvIds.add(inventoryEntry.getValue().getInventoryId());
                            }
                        }                       
                                               
                        // If product storage was found in storeroom
                        if (storeroomInvIds.size() > 0)
                        {
                            // Have a robot restock; Update floor and storeroom inventories
                            Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));    
                            
                            // Call robot appliance's restock method (returns a boolean)
                            if (appliance.getRobot().restock(productId, (inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[1]
                                    + inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[2]), aisleShelfLoc))
                            {
                                System.out.println(appliance.getName() + ": No room on " + aisleShelfLoc + "; restocking "
                                        + productId + " to " + (inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[1] + ":"
                                                + inventories.get(storeroomInvIds.get(0)).getLocation().split(":")[2]) + " (storeroom)");
                                
                                // Update storeroom location's inventory
                                System.out.println("Controller Service: Updating inventory " + inventories.get(storeroomInvIds.get(0)).getInventoryId()
                                        + "'s (storeroom) count by " + Integer.parseInt(number));
                                modeler.updateInventory(inventories.get(storeroomInvIds.get(0)).getInventoryId(), Integer.parseInt(number), null);                                                                        
                            }
                        }
                        
                        // Else there's no storage to put items back on
                        else
                        {
                            // TODO (???): Add an Exception
                            
                            return;
                        }
                        
                    }
                        
                    // Else there are no storeroom aisles; can't restock
                    else
                    {
                        // TODO (???): Add an Exception
                        
                        return;
                    }
                }                
            }                
        }
    }
    
    public class Clean extends Command
    {       
        /* Variables */
        
        private String productId;
        private String aisleNumber;
        
        /* Constructor */
        
        public Clean(Sensor sourceDevice, String productId, String aisleNumber)
        {
            super(sourceDevice);
            
            this.productId = productId;
            this.aisleNumber = aisleNumber;
        }

        /* Method(s) */
        
        @Override
        public void execute()
        {
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);                
            
            // Initialize array for getting store's robot appliance map keys
            ArrayList<String> robotKeys = new ArrayList<String>();           
            
            // Iterate through devices to find robots
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
                        // Add robot key to array
                        robotKeys.add(deviceEntry.getKey());                       
                    }
                }
            }
            
            System.out.println();
            
            // If store has a robot, tell it to clean up the product
            if (robotKeys.size() > 0)
            {
                Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));                
                
                if (appliance.getRobot().clean(productId, aisleNumber))
                {                    
                    System.out.println(appliance.getName() + ": Cleaning " + modeler.getProducts().get(productId).getName()+ " in "
                            + store.getAisles().get(aisleNumber).getName() + " aisle");
                }
            }
            
            // Else if store doesn't have a robot; cancel actions
            else
            {
                // TODO (???): Throw an Exception
                
                return;
            }
        }
    }
    
    public class BrokenGlass extends Command
    {
        /* Variables */
        
        private String aisleNumber;
        
        /* Constructor */
        
        public BrokenGlass(Sensor sourceDevice, String aisleNumber)
        {
            super(sourceDevice);
            
            this.aisleNumber = aisleNumber;
        }

        @Override
        public void execute()
        {
            // TODO
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);                
            
            // Initialize array for getting store's robot appliance map keys
            ArrayList<String> robotKeys = new ArrayList<String>();           
            
            // Iterate through devices to find robots
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
                        // Add robot key to array
                        robotKeys.add(deviceEntry.getKey());                       
                    }
                }
            }
            
            System.out.println();
            
            // If store has a robot, tell it to clean up the broke glass
            if (robotKeys.size() > 0)
            {
                Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));                
                
                if (appliance.getRobot().brokenGlass(aisleNumber))
                {                    
                    System.out.println(appliance.getName() + ": Cleaning broken glass in " + store.getAisles().get(aisleNumber).getName() + " aisle");
                }
            }
            
            // Else if store doesn't have a robot; cancel actions
            else
            {
                // TODO (???): Throw an Exception
                
                return;
            }
        }        
    }
    
    public class MissingPerson extends Command
    {
        /* Variables */
        
        private String customerId;

        /* Constructor */
        
        public MissingPerson(Sensor sourceDevice, String customerId)
        {
            super(sourceDevice);
            
            this.customerId = customerId;
        }

        @Override
        public void execute()
        {
            // TODO Auto-generated method stub
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
            // Initialize string for getting store's speaker that's nearest customer
            String speakerKey = null;           
            
            // Iterate through devices to find a speaker close to microphone/customer that triggered event
            Sensor devicePointer;
            for (Entry<String, Sensor> deviceEntry : store.getDevices().entrySet())
            {
                devicePointer = deviceEntry.getValue();
                
                // If device is a speaker and in same aisle as microphone, get its key and break out of loop      
                if (devicePointer.getType().equals("speaker") && devicePointer.getLocation().split(":")[1].equals(sourceDevice.getLocation().split(":")[1]))
                {
                    speakerKey = deviceEntry.getKey();
                    break;
                }
            }
            
            System.out.println();
            
            // If store speaker was found near microphone that triggered event, command it to announce message
            if (speakerKey != null)
            {
                Appliance appliance = (Appliance) store.getDevices().get(speakerKey);               
                
                String announcement = "Customer " + customerId + " is in " + store.getCustomers().get(customerId).getLocation().split(":")[1] + " aisle";
                
                if (appliance.getSpeaker().announce(announcement))
                {                    
                    System.out.println(appliance.getName() + ": \"" + announcement + "\"");
                }               
            }
            
            // Else if store didn't have a speaker, command a robot to tell customer or something
            else
            {
                // TODO (???): Throw an Exception
            }                
        }        
    }
    
    public class CustomerSeen extends Command
    {
        /* Variables */
        
        private String customerId;
        private String aisleId;
        private String dateTime;
        
        /* Constructor */
        
        public CustomerSeen(Sensor sourceDevice, String customerId, String aisleId, String dateTime)
        {
            super(sourceDevice);
            
            this.customerId = customerId;
            this.aisleId = aisleId;
            this.dateTime = dateTime;
        }

        @Override
        public void execute()
        {
            // TODO
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
            System.out.println();
            
            // Update customer's location
            System.out.println("Controller Service: Updating customer " + customerId + "'s location");
            modeler.updateCustomer(customerId, store.getId() + ":" + aisleId, dateTime, null);            
        }        
    }
    
    public class FetchProduct extends Command
    {
        /* Variables */
        
        private String productId;
        private Integer number;
        private String aisleShelfLoc;
        private String customerId;
        
        /* Constructor */
        
        public FetchProduct(Sensor sourceDevice, String productId, Integer number, String aisleShelfLoc, String customerId)
        {
            super(sourceDevice);
            
            this.productId = productId;
            this.number = number;
            this.aisleShelfLoc = aisleShelfLoc;
            this.customerId = customerId;
        }

        @Override
        public void execute()
        {
            // TODO
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);                
            
            // Initialize array for getting store's robot appliance map keys
            ArrayList<String> robotKeys = new ArrayList<String>();           
            
            // Iterate through devices to find robots
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
                        // Add robot key to array
                        robotKeys.add(deviceEntry.getKey());                       
                    }
                }
            }
            
            System.out.println();
            
            // If store has a robot, tell it to fetch the product
            if (robotKeys.size() > 0)
            {
                Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));
                
                // Get customer's location
                String customerAisleLoc = modeler.getCustomers().get(customerId).getLocation().split(":")[1];
                
                if (appliance.getRobot().fetchProduct(productId, number, aisleShelfLoc, customerId, customerAisleLoc))
                {                    
                    System.out.println(appliance.getName() + ": Fetching " + number + " of product " + productId + " from " + aisleShelfLoc
                            + " for customer " + customerId + " in " + customerAisleLoc + " aisle");                    
               
                    // Get inventory using aisleShelfLoc and productId (to update inventory)
                    LinkedHashMap<String, Inventory> inventories = store.getInventories();
                    Inventory inventory = null;
                    
                    for (Entry<String, Inventory> inventoryEntry : inventories.entrySet())
                    {
                        if (inventoryEntry.getValue().getLocation().equals(store.getId() + ":" + aisleShelfLoc)
                                && inventoryEntry.getValue().getProductId().equals(productId))
                        {
                            inventory = inventoryEntry.getValue();
                            break;
                        }
                    }
                    
                    if (!inventory.equals(null))
                    {
                        // Update inventory by decrementing product count on the shelf
                        System.out.println("Controller Service: Updating inventory " + inventory.getInventoryId() + "'s count by " + (number * (-1)));
                        modeler.updateInventory(inventory.getInventoryId(), (number * (-1)), null);                                                
                    }
                    
                    // Else inventory wasn't found; can't update it
                    else
                    {
                        // TODO (???): Throw exception
                        
                        return;
                    }              
                    
                    // Get customer's (virtual) basket (to update their basket items)
                    modeler.getCustomerBasket(customerId, null);
                    
                    // Update customer's virtual basket items by adding item(s) to it
                    System.out.println("Controller Service: Adding " + number + " of " + productId + " to customer "
                            + customerId + "'s virtual basket");
                    modeler.addBasketItem(customerId, productId, number, null);                                           
                }
            }
            
            // Else if store doesn't have a robot; cancel actions
            else
            {
                // TODO (???): Throw an Exception
                
                return;
            }
        }        
    }
    
    public class AccountBalance extends Command
    {
        /* Variables */
        
        private String customerId;

        /* Constructor */
        
        public AccountBalance(Sensor sourceDevice, String customerId)
        {
            super(sourceDevice);

            this.customerId = customerId;
        }

        @Override
        public void execute()
        {
            // TODO
            
            System.out.println();
            
            System.out.println("Controller Service: Computing the value of items in customer " + customerId + "'s basket");
            
            // Calculate the total value of the customer's basket items
            Basket basket = modeler.getCustomerBasket(customerId, null);
            LinkedHashMap<String, Integer> basketItems = basket.getBasketItems();
            Integer itemsTotValue = 0;
            for (Entry<String, Integer> integerEntry : basketItems.entrySet())
            {
                itemsTotValue += (integerEntry.getValue() * (modeler.getProducts().get(integerEntry.getKey()).getUnitPrice()));
            }            
            
            System.out.println("Controller Service: Checking customer " + customerId + "'s account balance");           
            
            // Get the customer's account balance            
            String customerBalance = ledgerCp.getAccountBalance(customerId);
            
            // Output aesthetics
            if (customerBalance == null)
                System.out.println();
            
            // Assemble expression string for a speaker near the customer to recite to customer            
            String moreLessOrEqual = null;
            
            if (customerBalance != null)
            {
                if (Integer.parseInt(customerBalance) > itemsTotValue)
                    moreLessOrEqual = "less than";
                else if (Integer.parseInt(customerBalance) < itemsTotValue)
                    moreLessOrEqual = "more than";
                else
                    moreLessOrEqual = "equal to";
            }
            
            String announcement = null;
            
            if (customerBalance == null)
            {
                announcement = "Total value of basket items is " + itemsTotValue + " Units. Your account balance is unavailable at the moment";
            }
            
            else
            {
                announcement = "Total value of basket items is " + itemsTotValue + " Units which is " + moreLessOrEqual + " your account balance "
                        + "of " + customerBalance;
            }
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
            // Initialize string for getting store's speaker that's nearest customer
            String speakerKey = null;           
            
            // Iterate through devices to find a speaker close to microphone/customer that triggered event
            Sensor devicePointer;
            for (Entry<String, Sensor> deviceEntry : store.getDevices().entrySet())
            {
                devicePointer = deviceEntry.getValue();
                
                // If device is a speaker and in same aisle as microphone, get its key and break out of loop      
                if (devicePointer.getType().equals("speaker") && devicePointer.getLocation().split(":")[1].equals(sourceDevice.getLocation().split(":")[1]))
                {
                    speakerKey = deviceEntry.getKey();
                    break;
                }
            }         
            
            // If store speaker was found near microphone that triggered event, command it to announce message
            if (speakerKey != null)
            {
                Appliance appliance = (Appliance) store.getDevices().get(speakerKey);       
                
                if (appliance.getSpeaker().announce(announcement))
                {                    
                    System.out.println(appliance.getName() + ": \"" + announcement + "\"");
                }               
            }
            
            // Else if store didn't have a speaker, command a robot to tell customer or something
            else
            {
                // TODO (???): Throw an Exception
            }              
        }        
    }
    
    public class CarAssist extends Command
    {
        /* Variables */
        
        private String customerId;
        
        /* Constructor */
        
        public CarAssist(Sensor sourceDevice, String customerId)
        {
            super(sourceDevice);
            
            this.customerId = customerId;
        }

        @Override
        public void execute()
        {
            // TODO
            
            // Get customer's basket
            Basket basket = modeler.getCustomerBasket(customerId, null);
            
            // If basket returned null, return (Modeler's getCustomerBasket will thrown an exception)
            if (basket == null)            
                return;         
            
            // Confirm that basket items' weight is actually exceeding 10 lbs.
            LinkedHashMap<String, Integer> basketItems = basket.getBasketItems();
            Integer itemsTotWeight = 0;
            for (Entry<String, Integer> integerEntry : basketItems.entrySet())
            {
                itemsTotWeight += (integerEntry.getValue() * Integer.parseInt(modeler.getProducts().get(integerEntry.getKey()).getSize().split(" ")[0]));
            }
            
            // TODO: Throw exception instead?
            if (itemsTotWeight <= 10)
            {                
                System.out.println("Total weight of basket does not exceed 10 lbs; car assist not needed");
                return;
            }
            
            else
            {
             // Get the source store
                LinkedHashMap<String, Store> stores = modeler.getStores();
                Store store = stores.get(sourceDevice.getLocation().split(":")[0]);                
                
                // Initialize array for getting store's robot appliance map keys
                ArrayList<String> robotKeys = new ArrayList<String>();           
                
                // Iterate through devices to find robots
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
                            // Add robot key to array
                            robotKeys.add(deviceEntry.getKey());                       
                        }
                    }
                }
                
                // If store has a robot, tell it to fetch the product
                if (robotKeys.size() > 0)
                {
                    Appliance appliance = (Appliance) store.getDevices().get(robotKeys.get(0));
                    
                    // Get customer's location
                    String customerLocation = store.getDevices().get(sourceDevice.getId()).getName();
                    
                    if (appliance.getRobot().carAssist(customerId, sourceDevice.getLocation()))
                    {                        
                        System.out.println(appliance.getName() + ": Assisting customer " + customerId + " at " + customerLocation + " to car");                      
                    }
                }
                
                // Else if store doesn't have a robot; cancel actions
                else
                {
                    // TODO (???): Throw an Exception
                    
                    return;
                }
            }
        }       
    }
    
    public class Checkout extends Command
    {
        /* Variables */
        
        private String customerId;
        
        /* Constructor */
        
        public Checkout(Sensor sourceDevice, String customerId)
        {
            super(sourceDevice);
            
            this.customerId = customerId;
        }

        @Override
        public void execute()
        {
            // TODO 
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
            System.out.println();   
           
            // Identify customer and have turnstile speaker acknowledge them
            System.out.println("Controller Service: Identifying customer " + customerId);
            Customer customer = store.getCustomers().get(customerId);            
            String expression = null;
            if (customer != null)
                expression = "Hello, customer " + customer.getFirstName();
            else
                expression = "Hello";
            Appliance appliance = (Appliance) sourceDevice;
            if (appliance.getTurnstile().speak(expression))
                System.out.println(appliance.getName() + ": \"" + expression + "\"");           
            
            // Initialize variables for customer's basket items (for registered customers)
            Integer itemsTotValue = 0;
            Basket basket = null;
            LinkedHashMap<String, Integer> basketItems = null; 
            
            // If customer is registered
            if ((customer != null) && customer.getType().toString().equals("registered"))
            {
                // Get the customer's basket
                basket = modeler.getCustomerBasket(customerId, null);
                basketItems = basket.getBasketItems();
                
                // Calculate the cost of their basket items
                System.out.println("Controller Service: Computing the value of items in customer " + customerId + "'s basket");              
                               
                for (Entry<String, Integer> integerEntry : basketItems.entrySet())
                {
                    itemsTotValue += (integerEntry.getValue() * (modeler.getProducts().get(integerEntry.getKey()).getUnitPrice()));
                }       
            }          
            
            // If customer has a non-empty basket
            if ((basket != null) && !basket.getBasketItems().isEmpty())
            {
                // Start Transaction
                System.out.println("Controller Service: Starting transaction for " + customerId + "'s checkout");
                String txnId = ledgerCp.processTransaction("0", itemsTotValue, 10, "checkout for " + customerId, customerId, store.getId());
                System.out.println("Controller Service: Transaction submitted and processed for " + customerId + "'s checkout");
                
                // If transaction came back null, have turnstile speaker talk to customer
                if (txnId == null)
                {
                    expression = "Transaction failed, " + store.getCustomers().get(customerId).getFirstName() + ". Please return items to store. Thank you!";                
                    if (appliance.getTurnstile().speak(expression))
                        System.out.println(appliance.getName() + ": \"" + expression + "\"");
                    
                    return;
                }
                
                // If payer does not have enough funds, have turnstile speaker talk to customer
                if (txnId != null && txnId.equals("payer has insufficient funds"))
                {
                    expression = "You do not have enough funds in your account for checkout, " + store.getCustomers().get(customerId).getFirstName()
                            + ". Please return some or all items to store before checking out again. Thank you!";
                    
                    if (appliance.getTurnstile().speak(expression))
                        System.out.println(appliance.getName() + ": \"" + expression + "\"");
                    
                    return;
                }
                    
                // If payer account was not found, have turnstile speaker talk to customer
                if (txnId != null && txnId.equals("payer account not found"))
                {
                    expression = "Your Blockchain account was not found, " + store.getCustomers().get(customerId).getFirstName()
                            + ". Please return items to store.";
                    
                    if (appliance.getTurnstile().speak(expression))
                        System.out.println(appliance.getName() + ": \"" + expression + "\"");
                    
                    return;
                }           
                     
                // TODO: If txn processed successfully
                if (txnId != null)
                {               
                    // Get weight of basket items                     
                    Integer itemsTotWeight = 0;
                    for (Entry<String, Integer> integerEntry : basketItems.entrySet())
                    {
                        itemsTotWeight += (integerEntry.getValue() * Integer.parseInt(modeler.getProducts().get(integerEntry.getKey()).getSize().split(" ")[0]));
                    }
                    
                    // If weight of items is more than 10 lbs, command a robot to assist customer to car
                    if (itemsTotWeight > 10)
                    {
                        expression = "Your basket items weigh more than 10 pounds. Please wait for a robot to assist you to your car";
                        if (appliance.getTurnstile().speak(expression))
                            System.out.println(appliance.getName() + ": \"" + expression + "\"");
                        
                        System.out.println(appliance.getName() + ": Calling for a robot to assist customer " + customerId + " to their car");                    
                        Command carAssist = new CarAssist(sourceDevice, customerId);
                        carAssist.execute();                    
                    }
                }
            }
                
            // Tell customer they can pass
            if (customer == null)
                expression = "You may pass through turnstile";                
            else    
                expression = "Customer " + store.getCustomers().get(customerId).getFirstName() + ", you may pass through turnstile";
            if (appliance.getTurnstile().speak(expression))
                System.out.println(appliance.getName() + ": \"" + expression + "\"");          
            
            // Open turnstile
            if (customer == null)
                expression = ": Person passed through turnstile";
            else
                expression = ": Customer " + customerId + " passed through turnstile";
            if (appliance.getTurnstile().letPersonPass())
                System.out.println(appliance.getName() + expression);
            
            // Tell customer goodbye
            if (customer == null)
                expression = "Goodbye!";
            else
                expression = "Goodbye, customer " + store.getCustomers().get(customerId).getFirstName() + ". Thanks for shopping at "
                        + store.getName() + "!";
            if (appliance.getTurnstile().speak(expression))
                System.out.println(appliance.getName() + ": \"" + expression + "\"");
            
            // If customer was a registered customer, clear their basket
            if ((customer != null) && customer.getType().toString().equals("registered"))
            {
                System.out.println("Controller Service: Clearing customer " + customerId + "'s basket");
                modeler.clearBasket(customerId, null);
            }
            
            // Update customer's location
            if (customer != null)
            {
                System.out.println("Controller Service: Updating customer " + customerId + "'s location");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss");
                LocalDateTime currentDateTime = LocalDateTime.now();
                modeler.updateCustomer(customerId, "null", dtf.format(currentDateTime), null);
            }
                       
            // TODO: Print how much store profited?
            
        }        
    }
    
    public class EnterStore extends Command
    {
        /* Variables */
        
        private String customerId;
        
        /* Constructor */
        
        public EnterStore(Sensor sourceDevice, String customerId)
        {
            super(sourceDevice);
             
            this.customerId = customerId;
        }

        @Override
        public void execute()
        {
            // TODO
            
            // Get the source store
            LinkedHashMap<String, Store> stores = modeler.getStores();
            Store store = stores.get(sourceDevice.getLocation().split(":")[0]);
            
            // Access source turnstile
            Appliance appliance = (Appliance) sourceDevice;
            
            // Initialize string for turnstile speaker
            String expression;
            
            // Output aesthetics
            System.out.println();
            
            // Identify customer
            System.out.println("Controller Service: Identifying customer " + customerId);
            Customer customer = modeler.getCustomers().get(customerId);
            
            // If customer is not registered or not a guest
            if (customer == null)
            {
                expression = "You do not have a registered account with " + store.getName() + ". Please register one and come back later";
                if (appliance.getTurnstile().speak(expression))
                    System.out.println(appliance.getName() + ": \"" + expression + "\"");
                
                return;
            }
            
            // Initialize customer balance string
            String customerBalance = null;
            
            // If customer is registered, get their account balance
            if (customer.getType().toString().equals("registered"))
            {
                System.out.println("Controller Service: Checking customer " + customerId + "'s account for positive balance");                 
                customerBalance = ledgerCp.getAccountBalance(customerId);                  
                
                // If account balance returned null
                if (customerBalance == null)
                {
                    System.out.println();
                    expression = "Your account information is unavailable. Please come back at another time";
                    if (appliance.getTurnstile().speak(expression))
                        System.out.println(appliance.getName() + ": \"" + expression + "\"");
                    
                    return;
                }
            }           
            
            // If customer has no funds in their account
            if ((customerBalance != null) && (Integer.parseInt(customerBalance) <= 0))
            {
                expression = "You have no funds in your account. Please come back when you do";
                if (appliance.getTurnstile().speak(expression))
                    System.out.println(appliance.getName() + ": \"" + expression + "\"");
                
                return;
            }          
            
            // Tell customer they can pass through turnstile and greet them
            expression = "Hello, customer " + modeler.getCustomers().get(customerId).getFirstName() + ", you may pass through the turnstile. "
                    + "Welcome to " + store.getName() + "!";
            if (appliance.getTurnstile().speak(expression))
                System.out.println(appliance.getName() + ": \"" + expression + "\"");          
            
            // Open turnstile
            if (appliance.getTurnstile().letPersonPass())
                System.out.println(appliance.getName() + ": Customer " + customerId + " passed through turnstile");
            
            // Update customer's location
            System.out.println("Controller Service: Updating customer " + customerId + "'s location");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss");
            LocalDateTime currentDateTime = LocalDateTime.now();
            modeler.updateCustomer(customerId, store.getId() + ":null", dtf.format(currentDateTime), null);
            
            // If customer is registered, assign them a virtual basket
            if ((customerBalance != null) && (Integer.parseInt(customerBalance) > 0))
            {             
                System.out.println("Controller Service: Getting customer " + customerId + "'s virtual basket");
                modeler.getCustomerBasket(customerId, null);
            }
        }        
    }
}