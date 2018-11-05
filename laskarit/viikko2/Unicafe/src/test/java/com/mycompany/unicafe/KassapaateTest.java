package com.mycompany.unicafe;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class KassapaateTest {
    
    public Kassapaate kassapaate;
    public Maksukortti maksukortti;
    public Maksukortti koyhakortti;
    
    public KassapaateTest() {
    }
    
    @Before
    public void setUp() {
        kassapaate = new Kassapaate();
        maksukortti = new Maksukortti(1000);
        koyhakortti = new Maksukortti(200);
    }
    
    @Test
    public void rahamaaraOikeinAlussa(){
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void myydytEdullisetOikeinAlussa(){
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void myydytMaukkaatOikeinAlussa(){
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKateisellaMaksuRiittaaKassanSaldoKasvaa(){
        kassapaate.syoEdullisesti(240);
        assertEquals(100240, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void edullinenKateisellaMaksuRiittaaVaihtorahaOikein(){
        assertEquals(260, kassapaate.syoEdullisesti(500));
    }
    
    @Test
    public void edullinenKateisellaMyydytPlusYksi(){
        kassapaate.syoEdullisesti(240);
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKateisellaMaksuEiRiitaSaldoEiKasva(){
        kassapaate.syoEdullisesti(230);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void edullinenKateisellaMaksuEiRiitaVaihtorahaOikein(){
        assertEquals(200, kassapaate.syoEdullisesti(200));
    }
    
    @Test
    public void edullinenKateisellaEiTarpeeksiRahaaMyydytEiKasva(){
        kassapaate.syoEdullisesti(200);
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void MaukasKateisellaMaksuRiittaaKassanSaldoKasvaa(){
        kassapaate.syoMaukkaasti(400);
        assertEquals(100400, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void MaukasKateisellaMaksuRiittaaVaihtorahaOikein(){
        assertEquals(100, kassapaate.syoMaukkaasti(500));
    }
    
    @Test
    public void MaukasKateisellaMyydytPlusYksi(){
        kassapaate.syoMaukkaasti(400);
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void MaukasKateisellaMaksuEiRiitaSaldoEiKasva(){
        kassapaate.syoMaukkaasti(390);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void MaukasKateisellaMaksuEiRiitaVaihtorahaOikein(){
        assertEquals(390, kassapaate.syoMaukkaasti(390));
    }
    
    @Test
    public void MaukasKateisellaEiTarpeeksiRahaaMyydytEiKasva(){
        kassapaate.syoMaukkaasti(390);
        assertEquals(0, kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKortillaMaksuRiittaaKassanSaldoEiKasva(){
        kassapaate.syoEdullisesti(maksukortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void edullinenKortillaMaksuRiittaaKortiltaLahteeRahaa(){
        assertTrue(kassapaate.syoEdullisesti(maksukortti));
    }
    
    @Test
    public void edullinenKortillaMyydytPlusYksi(){
        kassapaate.syoEdullisesti(maksukortti);
        assertEquals(1, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void edullinenKortillaEiTarpeeksiRahaaKortinRahaMaaraEiMuutu(){
        kassapaate.syoEdullisesti(koyhakortti);
        assertEquals(200, koyhakortti.saldo());
    }
    
    @Test
    public void edullinenKortillaEiTarpeeksiRahaaMyydytEiKasva(){
        kassapaate.syoEdullisesti(koyhakortti);
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukasKortillaMaksuRiittaaKassanSaldoEiKasva(){
        kassapaate.syoMaukkaasti(maksukortti);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void maukasKortillaMaksuRiittaaKortiltaLahteeRahaa(){
        assertTrue(kassapaate.syoMaukkaasti(maksukortti));
    }
    
    @Test
    public void maukasKortillaMyydytPlusYksi(){
        kassapaate.syoMaukkaasti(maksukortti);
        assertEquals(1, kassapaate.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void maukasKortillaEiTarpeeksiRahaaKortinRahaMaaraEiMuutu(){
        kassapaate.syoMaukkaasti(koyhakortti);
        assertEquals(200, koyhakortti.saldo());
    }
    
    @Test
    public void maukasKortillaEiTarpeeksiRahaaMyydytEiKasva(){
        kassapaate.syoMaukkaasti(koyhakortti);
        assertEquals(0, kassapaate.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void maukasKortillaEiTarpeeksiRahaaTapahtumaFalse(){
        assertFalse(kassapaate.syoMaukkaasti(koyhakortti));
    }
    
    @Test
    public void edullinenKortillaEiTarpeeksiRahaaTapahtumaFalse(){
        assertFalse(kassapaate.syoEdullisesti(koyhakortti));
    }
    
    @Test
    public void rahanLatausKortinSaldoMuuttuu(){
       kassapaate.lataaRahaaKortille(maksukortti, 1000);
        assertEquals(2000, maksukortti.saldo());
    }
    
    @Test
    public void rahanLatausKassanSaldoMuuttuu(){
       kassapaate.lataaRahaaKortille(maksukortti, 2000);
        assertEquals(102000, kassapaate.kassassaRahaa());
    }
    
    @Test
    public void rahanLatausKortinSaldoEiMuutuNegatiivisellaLuvulla(){
       kassapaate.lataaRahaaKortille(maksukortti, -100);
        assertEquals(1000, maksukortti.saldo());
    }
    
    @Test
    public void rahanLatausKassanSaldoEiMuutuNegatiivisellaLuvulla(){
       kassapaate.lataaRahaaKortille(maksukortti, -100);
        assertEquals(100000, kassapaate.kassassaRahaa());
    }
}
