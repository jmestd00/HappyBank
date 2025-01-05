package org.HappyBank.model;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AdministratorTest {
    Administrator admin;
    
    @Before
    public void setUp() {
        admin = new Administrator("Juan", "Pérez", "12345678A", "400123001", new BigDecimal(2000), "HappyBank");
    }
    
    @Test
    public void getNameTest() {
        assertEquals("Juan", admin.getName());
    }
    
    @Test
    public void getSurnameTest() {
        assertEquals("Pérez", admin.getSurname());
    }
    
    @Test
    public void getNIFTest() {
        assertEquals("12345678A", admin.getNIF());
    }
    
    @Test
    public void getSSNTest() {
        assertEquals("400123001", admin.getSSN());
    }
    
    @Test
    public void getSalaryTest() {
        assertEquals(new BigDecimal(2000), admin.getSalary());
    }
    
    @Test
    public void getBankTest() {
        assertEquals("HappyBank", admin.getBank());
    }
    
    @Test
    public void setSalaryTest() {
        admin.setSalary(new BigDecimal(2500));
        assertEquals(new BigDecimal(2500), admin.getSalary());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Administrator Juan Pérez: , BankName: HappyBank , NIF: 12345678A , SSN: 400123001 , Salary: 2000.00", admin.toString());
    }
}
