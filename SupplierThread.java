package Project2_6680081;

import java.util.*;
import java.io.*;

/*  
    for (int i = 0; i < supplierNum; i++) {
        Thread supplier = new Thread(new SupplierThread(
            warehouses, supplierMin, supplierMax, days, coordinator
        ), "Supplier_" + (i + 1));
        allThreads.add(supplier);
        supplier.start();
    }
*/

public class SupplierThread extends Thread {
    private final List<Warehouse> warehouses; 
    private final int supplierMin;
    private final int supplierMax;
    private final int totalDays;
    private final SimulationCoordinator coordinator;
    private final Random random;

    
    public SupplierThread(List<Warehouse> warehouses, int supplierMin, int supplierMax, int days, SimulationCoordinator coordinator) { 
        this.warehouses = warehouses;
        this.supplierMin = supplierMin;
        this.supplierMax = supplierMax;
        this.totalDays = days;
        this.coordinator = coordinator;
        this.random = new Random();
    }

    @Override
    public void run() {
        if (coordinator == null) {
            throw new IllegalArgumentException("Coordinator must not be null");
        }

        for (int day = 0; day < totalDays; day++) {
            try {
                coordinator.dailyStartBarrier.await();
                        
                int amount = supplierMin + random.nextInt(supplierMax - supplierMin + 1);
                int warehouseNumber = random.nextInt(warehouses.size());
                warehouses.get(warehouseNumber).put(amount);
                    System.out.printf("%s  >>  put %d materials    Warehouse_%d balance = %-5d\n",
                    Thread.currentThread().getName(),
                    amount,
                    warehouseNumber,
                    warehouses.get(warehouseNumber).getMaterialCount());
                                    
                coordinator.supplierDoneBarrier.await();
                coordinator.factoryDoneBarrier.await();

            } catch (Exception e) {
                System.out.println(getName() + ">> Error type: " + e.getMessage());
            }
        }
    }
}
