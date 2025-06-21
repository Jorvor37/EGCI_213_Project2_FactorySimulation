package Project2_6680684;

//import java.io.*;
import java.util.*;

/**
 *
 * @author Phattarada Limsuchaiwat, 6680684
 */

class Warehouse {
    private final String name;
    private int balance = 0;
    
    public Warehouse (String name)               { this.name = name; }
    
    public synchronized void put (int amount)    { balance += amount; }
    
    public synchronized int get (int amount) {
        int taken = Math.min (amount, balance);
        balance -= taken;
        return taken;
    }
    
    public synchronized int getBalance()         { return balance; } 
    
    public String getName()                      { return name; } 
}

class Freight {
    private final String name;
    private final int max;
    private int loaded = 0;
        
    public Freight (String name, int maxCapacity) {
        this.name = name;
        this.max = maxCapacity;
    }
    
    public synchronized int ship (int amount) {
        int room = max - loaded;
        int toShip = Math.min(room, amount);
        loaded += toShip;
        return toShip;
    }
    
    public synchronized void reset ()             { loaded = 0; }
    
    public synchronized int getRemaining ()       { return max - loaded; }
    
    public String getName()                       { return name; }
}
public class Jan_1 {

    public static void main(String[] args) {
        String configPath = "config_1.txt";
        
        ConfigReader config = new ConfigReader(configPath);
        
        int days = config.getDays();
        int warehouseNum = config.getWarehouseNum();
        int freightNum = config.getFreightNum();
        int freightCap = config.getFreightCapacity();
        
        List<Warehouse> warehouse = new ArrayList<>();
        for (int i = 0; i < warehouseNum; i++) 
        {
            warehouse.add(new Warehouse("Warehouse_" + i));
        }
        
        List<Freight> freight = new ArrayList<>();
        for (int i = 0; i < freightNum; i++)
        {
            freight.add(new Freight("Freight_" + (i + 1), freightCap));
        }
        
        //Day-by-day simulation
        Random rand = new Random();
        
        for (int day = 1; day <= days; day++)
        {
            System.out.printf("%n%20s >> Day %d %n", Thread.currentThread().getName(), day);
            
            for (Warehouse w : warehouse)
            {
                System.out.printf ("%20s >> %-16s = %d\n", Thread.currentThread().getName(), w.getName() + " balance", w.getBalance());
            }
            
            for (Freight f : freight) 
            {
                System.out.printf ("%20s >> %-16s = %d\n", Thread.currentThread().getName(), f.getName() + " capacity", f.getRemaining());
            }
            
            // for suppliers and factories parts
            
            for (Freight f : freight)
            {
                f.reset();
            }
        }
    }  
}
