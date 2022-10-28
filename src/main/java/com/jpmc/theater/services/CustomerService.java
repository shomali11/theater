package com.jpmc.theater.services;

import com.google.inject.Singleton;
import com.jpmc.theater.dao.CustomerDao;
import com.jpmc.theater.models.Customer;

import javax.inject.Inject;

@Singleton
public class CustomerService {

    private final CustomerDao customerDao;

    @Inject
    public CustomerService(final CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public void addCustomer(final Customer customer) {
        customerDao.addCustomer(customer);
    }

    public Customer getCustomer(final String id) {
        return customerDao.getCustomer(id);
    }
}
