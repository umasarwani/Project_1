package com.hotelreservation.model;

import com.hotelreservation.service.CustomerService;
import com.hotelreservation.service.ReservationService;

public class Tester {
    public static void main(String[] args) {
//      Customer customer=new Customer("Uma name", "uma last name","uma.sarw@gmail.com");
//        System.out.println(customer);
        CustomerService.customerServiceInstance().addCustomer("uma.sarw@gmail.com", "uma", "sarw");

        CustomerService.customerServiceInstance().getCustomer("uma.sarw@gmail.com");
        System.out.println(CustomerService.customerServiceInstance().getCustomer("uma.sarw@gmail.com"));

        }
}