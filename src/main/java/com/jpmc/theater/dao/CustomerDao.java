package com.jpmc.theater.dao;

import com.google.inject.Singleton;
import com.jpmc.theater.models.Customer;

import java.util.Map;
import java.util.TreeMap;

@Singleton
public class CustomerDao {

    // This DAO (Data Access Object) Layer pretends to be interacting with a database
    // Our map here is going to represent a customer database table/store
    private final Map<String, Customer> customerMap = new TreeMap<>();

    // Here we are adding a customer to our store using the ID as a primary key
    public void addCustomer(final Customer customer) {
        final String id = customer.getId();
        if (customerMap.containsKey(id)) {
            throw new IllegalArgumentException("a customer already exists for given id " + id);
        }
        customerMap.put(customer.getId(), customer);
    }

    // Here we are retrieving a customer from our store using the ID
    public Customer getCustomer(final String id) {
        if (!customerMap.containsKey(id)) {
            throw new IllegalArgumentException("not able to find any customer for given id " + id);
        }
        return customerMap.get(id);
    }
}
