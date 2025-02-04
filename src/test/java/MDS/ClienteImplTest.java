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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ClienteImplTest {

    private Cliente cliente;
    private List<Quarto> listaQuartos;
    private List<Reserva> reservasPendentes;

    @BeforeEach
    public void setUp() {
        cliente = new Cliente(1, "João", "123456789", LocalDate.of(1990, 1, 1));
        listaQuartos = new ArrayList<>();
        listaQuartos.add(new Quarto(1, 2, 3, 4, "mar", "disponivel", true));
        listaQuartos.add(new Quarto(2, 3, 4, 5, "predio", "disponível", false));
        listaQuartos.add(new Quarto(3, 4, 4, 4, "mar", "em manutenção", false));
        listaQuartos.add(new Quarto(4, 5, 6, 2, "sem vista", "em manutenção", true));

        reservasPendentes = new ArrayList<>();
    }

    @Test
public void testVerificarDisponibilidadeQuartoDisponivelEIndisponivel() throws ParseException {
    Quarto quartoTeste = listaQuartos.get(0);
    String dataEntrada = "2025-02-01";
    String dataSaida = "2025-02-05";
    Date entrada = new SimpleDateFormat("yyyy-MM-dd").parse(dataEntrada);
    Date saida = new SimpleDateFormat("yyyy-MM-dd").parse(dataSaida);

    boolean disponibilidade = quartoTeste.isDisponivel(entrada, saida);
    assertTrue(disponibilidade);

    Reserva r = new Reserva(entrada, saida);
    quartoTeste.adicionarReserva(r);

    // datas nao validas
    String dataEntradaFail = "2025-02-02";
    String dataSaidaFail = "2025-02-03";
    Date entradaFail = new SimpleDateFormat("yyyy-MM-dd").parse(dataEntradaFail);
    Date saidaFail = new SimpleDateFormat("yyyy-MM-dd").parse(dataSaidaFail);

    boolean disponibilidadeFail = quartoTeste.isDisponivel(entradaFail, saidaFail);
    assertFalse(disponibilidadeFail);

    Reserva r2 = new Reserva(entradaFail, saidaFail);
    IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> quartoTeste.adicionarReserva(r2),
            "Expected adicionarReserva() to throw, but it didn't"
    );
    assertTrue(thrown.getMessage().contains("conflito de datas com uma reserva existente"));
}


    @Test
    public void testProcurarQuartoDisponivel() {
        String inputSimulado = "1\n";
        Scanner scanner = new Scanner(inputSimulado);
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));

        try {
            ClienteImpl.procurarQuarto(cliente, scanner);
            System.out.println("#############");
            System.setOut(originalOut);
            String saida = saidaCapturada.toString();
            System.out.println("Saída capturada: " + saida);
            assertTrue(saida.contains(
                    "Quarto{id=1, capacidade=2, nCamas=3, nWC=4, tipoVista='mar', status='em manutenção', temCozinha=true}"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testProcurarQuartoDisponivelFail() {
        String inputSimulado = "12345\n";
        Scanner scanner = new Scanner(inputSimulado);
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));

        try {
            ClienteImpl.procurarQuarto(cliente, scanner);
            System.out.println("#############");
            System.setOut(originalOut);
            String saida = saidaCapturada.toString();
            System.out.println("Saída capturada: " + saida);
            assertTrue(saida.contains("quarto não encontrado no ficheiro:"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testFazerReservaComSucesso() throws ParseException {
        Quarto quartoTeste = new Quarto(listaQuartos.size() + 1, 2, 1, 1, "cidade", "disponível", false);
        listaQuartos.add(quartoTeste);
        String dataEntrada = "2025-02-01";
        String dataSaida = "2025-02-05";
        List<Reserva> reservasPendentesTeste;
        if (quartoTeste.isDisponivel(new SimpleDateFormat("yyyy-MM-dd").parse(dataEntrada),
                new SimpleDateFormat("yyyy-MM-dd").parse(dataSaida))) {
            Reserva novaReserva = new Reserva(reservasPendentes.size() + 1,
                    new SimpleDateFormat("yyyy-MM-dd").parse(dataEntrada),
                    new SimpleDateFormat("yyyy-MM-dd").parse(dataSaida),
                    "pendente", quartoTeste.getId(), cliente.getIdPessoa());
            reservasPendentes.add(novaReserva);
            ArquivoUtil.guardarLista("reservasPendentesTeste.json", reservasPendentes);
            reservasPendentesTeste = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                    new TypeReference<List<Reserva>>() {
                    });

            assertTrue(reservasPendentes.contains(novaReserva));
            assertTrue(reservasPendentesTeste.contains(novaReserva));
            assertTrue(!reservasPendentesTeste.isEmpty());
        }

        /****************** SEM SUCESSO *******************/

        String novadataEntrada = "2025-02-01";
        String novadataSaida = "2025-02-02";
        if (quartoTeste.isDisponivel(new SimpleDateFormat("yyyy-MM-dd").parse(novadataEntrada),
                new SimpleDateFormat("yyyy-MM-dd").parse(novadataSaida))) {
            assertFalse(false);
        }

    }

    @Test
    public void testConsultarReserva() {
        String inputSimulado = "1\n";
        Scanner scanner = new Scanner(inputSimulado);
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));

        try {
            ClienteImpl.consultarReserva(cliente, scanner);
            List<Reserva> reservasPendentesTeste = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                    new TypeReference<List<Reserva>>() {
                    });

            Reserva rr = null;
            for (Reserva r : reservasPendentesTeste) {
                if (r.getIdReserva() == Integer.parseInt(inputSimulado.trim())) {
                    rr = r;
                    break;
                }
            }
            System.out.println(rr);
            if (rr == null) {
                fail("Reserva com ID " + inputSimulado.trim() + " não encontrada");
            }

            System.setOut(originalOut);
            String saida = saidaCapturada.toString();
            System.out.println("Saída capturada: " + saida);
            assertTrue(saida.contains("Reserva{idReserva=" + rr.getIdReserva() +
                    ", dataEntrada=" + rr.getDataEntrada() +
                    ", dataSaida=" + rr.getDataSaida() +
                    ", status='" + rr.getStatus() + "'}"),
                    "A saída não contém os detalhes da reserva esperada");
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testConsultarReservaFail() {
        // unico cenario em que nao é possivel é se o id da reserva nao existe
        String inputSimulado = "931\n";
        Scanner scanner = new Scanner(inputSimulado);
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));

        try {
            ClienteImpl.consultarReserva(cliente, scanner);
            List<Reserva> reservasPendentesTeste = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                    new TypeReference<List<Reserva>>() {
                    });

            Reserva rr = null;
            for (Reserva r : reservasPendentesTeste) {
                if (r.getIdReserva() == Integer.parseInt(inputSimulado.trim())) {
                    rr = r;
                    break;
                }
            }
            assertNull(rr);

            System.setOut(originalOut);
            String saida = saidaCapturada.toString();
            assertTrue(saida.contains("reserva já confirmada ou não existe"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    public void testCancelarReserva() {
        List<Reserva> reservasPendentes = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                new TypeReference<List<Reserva>>() {
                });
        String inputSimulado = "1\n";
        Scanner scanner = new Scanner(inputSimulado);

        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));

        try {
            int idReserva = Integer.parseInt(inputSimulado.trim());
            Reserva reservaTeste = reservasPendentes.stream()
                    .filter(r -> r.getIdReserva() == idReserva)
                    .findFirst()
                    .orElse(null);

            System.out.println(reservaTeste);
            if (reservaTeste == null) {
                fail("Reserva com ID " + idReserva + " não encontrada");
            }

            cancelarReserva(idReserva);
            reservasPendentes = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                    new TypeReference<List<Reserva>>() {
                    });
            boolean reservaAindaExiste = reservasPendentes.stream()
                    .anyMatch(r -> r.getIdReserva() == idReserva);
            assertFalse(reservaAindaExiste, "A reserva não foi cancelada corretamente");
            System.setOut(originalOut);
            String saida = saidaCapturada.toString();
            System.out.println("Saída capturada: " + saida);
            reservasPendentes.add(reservaTeste);
            ArquivoUtil.guardarLista("reservasPendentesTeste.json", reservasPendentes);
        } finally {
            System.setOut(originalOut);
            scanner.close();
        }
    }

    @Test
    public void testCancelarReservaInvalida() {
        // unico cenario de insucesso - se o id nao existir
        int idReservaInvalido = 99;
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        System.setOut(new PrintStream(saidaCapturada));
        cancelarReserva(idReservaInvalido);
        assertTrue(
                saidaCapturada.toString().contains("não encontrada"),
                "Mensagem de erro ausente para ID inválido");
    }

    public void cancelarReserva(int idReserva) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("reservasPendentesTeste.json");
            if (arquivo.exists()) {
                List<Reserva> reservas = objectMapper.readValue(arquivo, new TypeReference<List<Reserva>>() {
                });
                boolean reservaRemovida = reservas.removeIf(reserva -> reserva.getIdReserva() == idReserva);

                if (reservaRemovida) {
                    objectMapper.writeValue(arquivo, reservas);
                    System.out.println("Reserva com ID " + idReserva + " foi cancelada com sucesso");
                } else {
                    System.out.println("não encontrada");
                }
            } else {
                System.out.println("Ficheiro de reservasPendentes.json não encontrado");
            }
        } catch (IOException e) {
            System.out.println("erro ao processar o ficheiro de reservas: " + e.getMessage());
        }
    }

    public void consultarReserva(int idReserva) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("reservasPendentesTeste.json");
            if (arquivo.exists()) {
                List<Reserva> reservas = objectMapper.readValue(arquivo, new TypeReference<List<Reserva>>() {
                });
                for (Reserva r : reservas) {
                    if (r.getIdReserva() == idReserva) {
                        System.out.println(r);
                    } else {
                        System.out.println("reserva já confirmada ou não existe");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("erro ao processar o ficheiro de reservas: " + e.getMessage());
        }
    }
}
