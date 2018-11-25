package ohtu.verkkokauppa;

import ohtu.verkkokauppa.Kauppa;
import ohtu.verkkokauppa.Pankki;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class KauppaTest {

    Kauppa k;
    
    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaParametreilla() {
        // luodaan ensin mock-oliot
        Pankki pankki = mock(Pankki.class);

        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        // määritellään että viitegeneraattori palauttaa viitten 42
        when(viite.uusi()).thenReturn(42);

        Varasto varasto = mock(Varasto.class);
        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));

        // sitten testattava kauppa 
        k = new Kauppa(varasto, pankki, viite);

        // tehdään ostokset
        k.aloitaAsiointi();
        k.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        k.tilimaksu("pekka", "12345");

        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto("pekka", 42, "12345", "33333-44455", 5);
        // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
    }
    
    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaParametreilla2() {
        Pankki pankki = mock(Pankki.class);
        
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).thenReturn(1);
        
        Varasto varasto = mock(Varasto.class);
        
        when(varasto.saldo(1)).thenReturn(1);
        when(varasto.saldo(2)).thenReturn(1);
        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 10);
    }
    
    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaParametreilla3() {
        Pankki pankki = mock(Pankki.class);
        
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).thenReturn(1);
        
        Varasto varasto = mock(Varasto.class);
        
        when(varasto.saldo(1)).thenReturn(2);
        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(1);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 10);
    }
    
    @Test
    public void ostoksenPaaytyttyaPankinMetodiaTilisiirtoKutsutaanOikeillaParametreilla4() {
        Pankki pankki = mock(Pankki.class);
        
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).thenReturn(1);
        
        Varasto varasto = mock(Varasto.class);
        
        when(varasto.saldo(1)).thenReturn(1);
        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 5);
    }
    
    @Test
    public void aloitaAsiointiNollaaEdellisenOstoksenTiedot() {
        Pankki pankki = mock(Pankki.class);
        
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).thenReturn(1);
        
        Varasto varasto = mock(Varasto.class);
        
        when(varasto.saldo(1)).thenReturn(1);
        when(varasto.saldo(2)).thenReturn(1);
        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 5);
        
        when(viite.uusi()).thenReturn(2);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(2);
        k.tilimaksu("pekka", "12345");
        verify(pankki).tilisiirto("pekka", 2, "12345", "33333-44455", 5);
    }
    
    @Test
    public void uusiViiteJokaOstokselle() {
        Pankki pankki = mock(Pankki.class);
        
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).
                thenReturn(1).
                thenReturn(2);
        
        Varasto varasto = mock(Varasto.class);
        
        when(varasto.saldo(1)).thenReturn(1);
        when(varasto.saldo(2)).thenReturn(1);
        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "leipa", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto("pekka", 1, "12345", "33333-44455", 5);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(2);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto("pekka", 2, "12345", "33333-44455", 5);
    }
    
    @Test 
    public void koristaPoistaminenPalauttaaVarastoon() {
        Pankki pankki = mock(Pankki.class);
        Viitegeneraattori viite = mock(Viitegeneraattori.class);
        when(viite.uusi()).thenReturn(1);
        
        Varasto varasto = mock(Varasto.class);
        
        when(varasto.saldo(1)).thenReturn(2);

        
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.poistaKorista(1);
        
        verify(varasto, times(1)).palautaVarastoon(new Tuote(1, "maito", 5));
    }
    
}
