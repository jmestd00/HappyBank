package org.HappyBank.model;

import org.HappyBank.model.repository.ClientRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class ClientTest {
    ClientRepositoryImpl clientRepository;
    Client mockClient;
    
    @Before
    public void setUp() {
        mockClient = new Client("Juan", "Pérez", "12345678A", "juanperez@gmail.com", "123456789", "Calle de Prueba 123", "HappyBank");
        clientRepository = mock(ClientRepositoryImpl.class);
        mockClient.setClientRepository(clientRepository);
        
        doNothing().when(clientRepository).update(any());
    }
    
    @Test
    public void getNameTest() {
        assertEquals("Juan", mockClient.getName());
    }
    
    @Test
    public void getSurnameTest() {
        assertEquals("Pérez", mockClient.getSurname());
    }
    
    @Test
    public void getNIFTest() {
        assertEquals("12345678A", mockClient.getNIF());
    }
    
    @Test
    public void getEmailTest() {
        assertEquals("juanperez@gmail.com", mockClient.getEmail());
    }
    
    @Test
    public void getPhoneTest() {
        assertEquals("123456789", mockClient.getPhone());
    }
    
    @Test
    public void getAddressTest() {
        assertEquals("Calle de Prueba 123", mockClient.getAddress());
    }
    
    @Test
    public void getBankTest() {
        assertEquals("HappyBank", mockClient.getBank());
    }
    
    @Test
    public void setEmailTest() {
        mockClient.setEmail("juanperez@example.com");
        assertEquals("juanperez@example.com", mockClient.getEmail());
    }
    
    @Test
    public void setPhoneTest() {
        mockClient.setPhone("987654321");
        assertEquals("987654321", mockClient.getPhone());
    }
    
    @Test
    public void setBankTest() {
        mockClient.setAddress("Calle de Prueba 321");
        assertEquals("Calle de Prueba 321", mockClient.getAddress());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Juan Pérez , BankName: HappyBank , DNI: 12345678A , Email: juanperez@gmail.com , Phone Number: 123456789 , Address: Calle de Prueba 123", mockClient.toString());
    }
}
