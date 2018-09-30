package com.example.macie.mobilecheckout.program_logic;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements lazy instantiation design pattern for singleton classes.
 * Preserves list of Product objects inside LinkedList. Allows for additional and removal
 * of the list content.
 */

public class VirtualBasket {
    private List<Product> productList;
    private static VirtualBasket instance;

    /**
     * Constructor for VirtualBasket. Initiates declared List.
     */
    private VirtualBasket() {
        productList = new LinkedList<>();
    }

    /**
     *
     * @return Instance of VirtualBasket
     */
    public static VirtualBasket getInstance() {
        if(instance == null) {
            instance = new VirtualBasket();
        }
        return instance;
    }

    /**
     * Adds Product into productList.
     * @param product
     */
    public void add(Product product){
        productList.add(product);
    }

    /**
     * Removes Product from productList.
     * @param product
     */
    public void remove(Product product){
        productList.remove(product);
    }

    /**
     *
     * @return List of Product objects.
     */
    public List<Product> getProductList(){
        return productList;
    }

    /**
     *
     * @param index
     * @return Product from the list.
     */
    public Product findByIndex(int index) {
        return productList.get(index);
    }

}
