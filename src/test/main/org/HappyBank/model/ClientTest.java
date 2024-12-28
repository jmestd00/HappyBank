package org.HappyBank.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClientTest {
    Client client;
    
    @Before
    public void setUp() {
        client = new Client("Juan", "Pérez", "12345678A", "juanperez@gmail.com", "123456789", "Calle de Prueba 123", "HappyBank");
    }
    
    @Test
    public void getNameTest() {
        assertEquals("Juan", client.getName());
    }
    
    @Test
    public void getSurnameTest() {
        assertEquals("Pérez", client.getSurname());
    }
    
    @Test
    public void getNIFTest() {
        assertEquals("12345678A", client.getNIF());
    }
    
    @Test
    public void getEmailTest() {
        assertEquals("juanperez@gmail.com", client.getEmail());
    }
    
    @Test
    public void getPhoneTest() {
        assertEquals("123456789", client.getPhone());
    }
    
    @Test
    public void getAddressTest() {
        assertEquals("Calle de Prueba 123", client.getAddress());
    }
    
    @Test
    public void getBankTest() {
        assertEquals("HappyBank", client.getBank());
    }
    
    @Test
    public void setEmailTest() {
        client.setEmail("juanperez@example.com");
        assertEquals("juanperez@example.com", client.getEmail());
    }
    
    @Test
    public void setPhoneTest() {
        client.setPhone("987654321");
        assertEquals("987654321", client.getPhone());
    }
    
    @Test
    public void setBankTest() {
        client.setAddress("Calle de Prueba 321");
        assertEquals("Calle de Prueba 321", client.getAddress());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Juan Pérez , BankName: HappyBank , DNI: 12345678A , Email: juanperez@gmail.com , Phone Number: 123456789 , Address: Calle de Prueba 123", client.toString());
    }
}
