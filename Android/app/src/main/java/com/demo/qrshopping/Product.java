package com.demo.qrshopping;

import java.io.Serializable;

/**
 * Created by manish on 26/04/18.
 */

public class Product implements Serializable {

    String price;
    int id;
    String name;
    String quantity;

    Product(String name,String price,String quantity){
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

}
