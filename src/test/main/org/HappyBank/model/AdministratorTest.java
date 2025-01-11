package org.HappyBank.model;

import java.math.BigDecimal;

import org.HappyBank.model.repository.AdministratorRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class AdministratorTest {
    AdministratorRepositoryImpl administratorRepository ;
    Administrator mockAdmin;
    
    @Before
    public void setUp() {
        mockAdmin = new Administrator("Juan", "Pérez", "12345678A", "400123001", new BigDecimal(2000), "HappyBank");
        administratorRepository = mock(AdministratorRepositoryImpl.class);
        mockAdmin.setAdministratorRepository(administratorRepository);
        
        doNothing().when(administratorRepository).update(any());
    }
    
    @Test
    public void getNameTest() {
        assertEquals("Juan", mockAdmin.getName());
    }
    
    @Test
    public void getSurnameTest() {
        assertEquals("Pérez", mockAdmin.getSurname());
    }
    
    @Test
    public void getNIFTest() {
        assertEquals("12345678A", mockAdmin.getNIF());
    }
    
    @Test
    public void getSSNTest() {
        assertEquals("400123001", mockAdmin.getSSN());
    }
    
    @Test
    public void getSalaryTest() {
        assertEquals(new BigDecimal(2000), mockAdmin.getSalary());
    }
    
    @Test
    public void getBankTest() {
        assertEquals("HappyBank", mockAdmin.getBank());
    }
    
    @Test
    public void setSalaryTest() {
        mockAdmin.setSalary(new BigDecimal(2500));
        assertEquals(new BigDecimal(2500), mockAdmin.getSalary());
    }
    
    @Test
    public void toStringTest() {
        assertEquals("Administrator Juan Pérez: , BankName: HappyBank , NIF: 12345678A , SSN: 400123001 , Salary: 2000.00", mockAdmin.toString());
    }
}
