package com.jpmc.theater.services;

import com.jpmc.theater.dao.CustomerDao;
import com.jpmc.theater.models.Customer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomerServiceTests {

    private static final CustomerDao customerDao = new CustomerDao();
    private static final CustomerService customerService = new CustomerService(customerDao);

    @BeforeAll
    static void setup() {
        final Customer customer = Customer.builder().name("Raed Shomali").id("1").build();
        customerService.addCustomer(customer);
    }

    @Test
    void testValidGet() {
        final Customer customer = customerService.getCustomer("1");
        assertEquals("1", customer.getId());
        assertEquals("Raed Shomali", customer.getName());
    }

    @Test
    void testInvalidGet() {
        assertThrows(IllegalArgumentException.class, () -> {
            customerService.getCustomer("unknown");
        });
    }
}
