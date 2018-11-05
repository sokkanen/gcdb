package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;

    @Before
    public void setUp() {
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    
    @Test
    public void kortinSaldoAlussaOikein() {
        assertEquals("saldo: 0.10", kortti.toString());      
    }
    
    @Test
    public void kortinLataaminenLisaaSaldoaOikein() {
        kortti.lataaRahaa(1000);
        assertEquals("saldo: 10.10", kortti.toString());      
    }
    
    @Test
    public void saldoVaheneeOikeinKunRahaaOn() {
        kortti.lataaRahaa(990);
        assertTrue(kortti.otaRahaa(240));
    }
    
    @Test
    public void saldoEiVaheneKunRahaaEiOle() {
        assertFalse(kortti.otaRahaa(240));
    }
    
    @Test
    public void saldoPalauttaaOikeanMaaran() {
        assertEquals(10, kortti.saldo());
    }
}
