package MDS;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

public class GestorSistemaImplTest {

    private List<Quarto> listaQuartos;

    @BeforeEach
    public void setup() {
        listaQuartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
        });
    }

    public void guardarListaDeQuartos() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("quartosTeste.json"), listaQuartos);
        } catch (IOException e) {
            System.out.println("Erro ao guardar lista: " + e.getMessage());
        }
    }

    public void adicionarQuarto(Quarto quarto) {
        if (!listaQuartos.contains(quarto)) {
            listaQuartos.add(quarto);
            guardarListaDeQuartos();
            System.out.println("Quarto adicionado com sucesso: " + quarto);
        } else {
            System.out.println("esse quarto já existe na lista");
        }
    }

    public void removerQuarto(Quarto quarto) {
        if (listaQuartos.remove(quarto)) {
            guardarListaDeQuartos();
            System.out.println("quarto removido com sucesso: " + quarto);
        } else {
            System.out.println("quarto não encontrado na lista");
        }
    }

    @Test
    public void testAdicionarQuarto() {
        // GestorSistema gestorSistema = new GestorSistema(0, null, null, null);
        Quarto novoQuarto = new Quarto(listaQuartos.size() + 1, 40, 2, 1, "Vista Montanha", "disponivel", false);
        adicionarQuarto(novoQuarto);
        List<Quarto> listaAtualizada = ArquivoUtil.carregarLista("quartosTeste.json",
                new TypeReference<List<Quarto>>() {
                });
        System.out.println(listaAtualizada);
        assertTrue(listaAtualizada.stream().anyMatch(q -> q.getId() == novoQuarto.getId()),
                "O novo quarto não foi adicionado");
        removerQuarto(novoQuarto);
        // para fins de teste, manter o ficheiro pequeno e manter a persistencia
    }
    @Test
    public void testAdicionarQuartoSemSucesso() {
        //unico cenario de insucesso é se , por algum motivo, o id do quarto coincidir com um id já existente
        Quarto novoQuarto = new Quarto(1, 40, 2, 1, "Vista Montanha", "disponivel", false); //tentar adicionar um quarto com um id já existente
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));
        adicionarQuarto(novoQuarto);
        System.setOut(originalOut);
        String resultado = saidaCapturada.toString();
        assertTrue(resultado.contains("esse quarto já existe na lista"));
    }
    @Test
    public void testRemoverQuarto() {
        Quarto novoQuarto = new Quarto(listaQuartos.size() + 1, 40, 2, 1, "Vista Montanha", "disponivel", false);
        listaQuartos.add(novoQuarto);
        removerQuarto(novoQuarto);
        List<Quarto> listaAtualizada = ArquivoUtil.carregarLista("quartosTeste.json",
                new TypeReference<List<Quarto>>() {
                });
        assertFalse(listaAtualizada.stream().anyMatch(q -> q.getId() == novoQuarto.getId()),
                "O novo quarto não foi adicionado");
        adicionarQuarto(novoQuarto); // para fins de teste, manter o ficheiro pequeno e manter a persistencia
    }

    @Test
    public void testRemoverQuartoSemSucesso(){
        //unico cenario de insucesso- se o quarto não existir (id) 
        Quarto q = new Quarto(391, 0, 0, 0, null, null, false);
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));
        removerQuarto(q);
        System.setOut(originalOut);
        String resultado = saidaCapturada.toString();
        assertTrue(resultado.contains("quarto não encontrado na lista"));

    }

    

    @Test
    public void testConsultarQuarto() {
        //GestorSistema g = new GestorSistema(0, null, null, null);
        int inputSimulado = 1;
        Quarto q = consultarQuarto(inputSimulado);
        assertTrue(q.toString().contains(
                "Quarto{id=1, capacidade=2, nCamas=3, nWC=4, tipoVista='mar', status='disponivel', temCozinha=true}"));

        /* ############### SEM SUCESSO ################## */

        Quarto q2 = consultarQuarto(99);
        assertNull(q2);
    }

    @Test
    public void consultarQuartoSemSucesso(){
        // o unico cenario de insucesso é se o quarto nao existir
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));
        consultarQuarto(99);
        System.setOut(originalOut);
        String resultado = saidaCapturada.toString();
        assertTrue(resultado.contains("quarto com ID " + 99 + " não encontrado"));
    }

    @Test
    public void modificarQuarto() {
       // GestorSistema g = new GestorSistema(0, null, null, null);
        int max_id = getMaxIdQuarto(listaQuartos);
        Quarto q = new Quarto(max_id, 15, 15, 15, "serra111", "disponivel", false);
        listaQuartos.add(q);
        ArquivoUtil.guardarLista("quartosTeste.json", listaQuartos);
        assertNotNull(consultarQuarto(max_id));
        modificarQuarto(max_id, 16, 16, 16, "testemodificarquarto", "disponivel", true);
        removerQuarto(q); // remover no fim para repetir o processo sempre que necessário
        List<Quarto> listaAtualizada = ArquivoUtil.carregarLista("quartosTeste.json",
                new TypeReference<List<Quarto>>() {
                });
        assertFalse(listaAtualizada.contains(q));

    }

    @Test
    public void modificarQuartoSemSucesso() {
        // o unico cenario de insucesso é se o quarto nao existir
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));
        modificarQuarto(99, 0, 0, 0, null, null, false);
        System.setOut(originalOut);
        String resultado = saidaCapturada.toString();
        assertTrue(resultado.contains("quarto não encontrado"));

    }

    public static int getMaxIdQuarto(List<Quarto> list) {
        if (list.isEmpty()) {
            return 1;
        }
        int max = list.get(0).getId();
        for (Quarto q : list) {
            if (q.getId() > max) {
                max = q.getId();
            }
        }
        return max + 1;
    }

    public void modificarQuarto(int id, int capacidade, int nCamas, int nWC, String tipoVista, String status,
            boolean temCozinha) {
        for (Quarto q : listaQuartos) {
            if (q.getId() == id) {
                q.setCapacidade(capacidade);
                q.setnCamas(nCamas);
                q.setnWC(nWC);
                q.setTipoVista(tipoVista);
                q.setStatus(status);
                q.setTemCozinha(temCozinha);
                guardarListaDeQuartos();
                System.out.println("quarto atualizado com sucesso: " + q);
                return;
            }
        }
        System.out.println("quarto não encontrado");
    }
    public Quarto consultarQuarto(int idQuarto) {
        for (Quarto quarto : listaQuartos) {
            if (quarto.getId() == idQuarto) {
                return quarto;
            }
        }
        System.out.println("quarto com ID " + idQuarto + " não encontrado");
        return null;
    }
}
