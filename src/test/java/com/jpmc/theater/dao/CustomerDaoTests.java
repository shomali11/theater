package com.jpmc.theater.dao;

import com.jpmc.theater.models.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerDaoTests {

    private static final CustomerDao customerDao = new CustomerDao();

    @BeforeAll
    static void setup() {
        final Customer customer = Customer.builder().name("Raed Shomali").id("1").build();
        customerDao.addCustomer(customer);
    }

    @Test
    void testInvalidAdd() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerDao.addCustomer(Customer.builder().name("Raed Shomali").id("1").build());
        });
    }

    @Test
    void testValidGet() {
        final Customer customer = customerDao.getCustomer("1");
        assertEquals("1", customer.getId());
        assertEquals("Raed Shomali", customer.getName());
    }

    @Test
    void testInvalidGet() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerDao.getCustomer("unknown");
        });
    }
}
