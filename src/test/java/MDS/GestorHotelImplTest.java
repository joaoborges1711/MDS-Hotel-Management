package MDS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

public class GestorHotelImplTest {
    
    private GestorHotel g;
    
    @BeforeEach
     void setUp(){
       g = new GestorHotel(0, null, null, null);
     }



    @Test
    public void testConsultarQuarto() {
        String inputSimulado = "1";
        List<Quarto> consultarQuarto = ArquivoUtil.carregarLista("quartosTeste.json",
                new TypeReference<List<Quarto>>() {
                });
        Quarto q = g.consultarQuarto(Integer.parseInt(inputSimulado), consultarQuarto);
        assertEquals(
                "Quarto{" + "id=1, " + "capacidade=2, " + "nCamas=3, " + "nWC=4, " + "tipoVista='mar', "
                        + "status='disponivel', " +
                        "temCozinha=true" +
                        "}",
                q.toString());
    }

    @Test
    public void testConsultarQuartoFail() {
        String inputSimulado = "1019";
        List<Quarto> consultarQuarto = ArquivoUtil.carregarLista("quartosTeste.json",
                new TypeReference<List<Quarto>>() {
                });
        Quarto q = g.consultarQuarto(Integer.parseInt(inputSimulado), consultarQuarto);
        assertNull(q);
    }
}
