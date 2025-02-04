package MDS;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
//import java.util.Scanner;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;

public class FuncionarioHotelImplTest {


    @Test
    public void ConfirmarReservaTest() {
        List<Reserva> reservasPendentes = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                new TypeReference<List<Reserva>>() {
                });
        if (reservasPendentes.size() == 0) {
            System.out.println("não existem reservas pendentes de confirmação");
            return;
        }
        Reserva reservaConfirmada = reservasPendentes.get(0);
        try {
            List<Quarto> quartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
            });
            Quarto quartoReservado = null;
            for (Quarto room1 : quartos) {
                if (room1.getId() == reservaConfirmada.getIdQuarto()) {
                    quartoReservado = room1;
                    break;
                }
            }

            if (quartoReservado == null) {
                System.out.println("Erro: Quarto associado à reserva não encontrado");

            }
            reservaConfirmada.alterarStatus("confirmada");
            for (Reserva r : reservasPendentes) {
                if (r.getIdReserva() == reservaConfirmada.getIdReserva()) {
                    assertTrue(r.getStatus().equals("confirmada"));
                }
            }
            reservaConfirmada.setId(quartoReservado.getReservas().size() + 1);

            quartoReservado.getReservas().add(reservaConfirmada);
            reservasPendentes.remove(reservaConfirmada);
            ArquivoUtil.guardarLista("quartosTeste.json", quartos);
            for (Quarto q : quartos) {
                List<Reserva> res = q.getReservas();
                for (Reserva r : res) {
                    if (r.getIdReserva() == reservaConfirmada.getIdReserva()) {
                        assertTrue(true);
                    }
                }
            }
            ArquivoUtil.guardarLista("reservasPendentesTeste.json", reservasPendentes);
            List<Reserva> res = ArquivoUtil.carregarLista("reservasPendentesTeste.json",
                    new TypeReference<List<Reserva>>() {
                    });
            assertFalse(res.contains(reservaConfirmada));
            System.out.println("Reserva confirmada com sucesso: " + reservaConfirmada);
            reservaConfirmada.setId(1);
            reservasPendentes.add(reservaConfirmada);
            ArquivoUtil.guardarLista("reservasPendentesTeste.json", reservasPendentes);

        } catch (Exception e) {
            System.out.println("Erro ao confirmar reserva: " + e.getMessage());
        }
    }
    @Test 
    public void ConfirmarReservaTestFail(){
        // o unico cenario de insucesso é se o id da reserva nao existir
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(saidaCapturada));

        try {
            confirmarReserva(ArquivoUtil.carregarLista("reservasPendentesTeste.json",
            new TypeReference<List<Reserva>>() {
            }), 931);
            String saida = saidaCapturada.toString();
            assertTrue(saida.contains("Reserva não encontrada"));
        } finally {
            System.setOut(originalOut);
        }

    }
    @Test
    public void adicionarManutençãoTest() {
        Random r = new Random();
        try {
            List<Quarto> quartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
            });
            Quarto quarto = null; 
            for (Quarto q : quartos) {
                if (q.getId() == 123) {
                    quarto = q;
                }
            }
            Manutencao manutencao = new Manutencao(quarto.getManutencoes().size() + 1,
                    "testeDaFunc" + Integer.toString(r.nextInt(100000))); // para teste apenas
            quarto.getManutencoes().add(manutencao);
            if (!"em manutenção".equals(quarto.getStatus())) {
                quarto.setStatus("em manutenção");
                assertTrue(quarto.getStatus().equals("em manutenção"));
            }

            ArquivoUtil.guardarLista("quartosTeste.json", quartos);
            for (Quarto q : quartos) {
                List<Manutencao> man = q.getManutencoes();
                if (man.contains(manutencao)) {
                    assertTrue(true);
                }
            }
            System.out.println("Manutenção adicionada com sucesso: " + manutencao);
            quarto.setStatus("disponivel");

        } catch (Exception e) {
            System.out.println("Erro ao adicionar manutenção: " + e.getMessage());
        }
    }

    
    @Test
    public void testRemoverManutencao() {
        FuncionarioHotel f = new FuncionarioHotel(0, null, null, null);
        String idQuarto = "1"; // input simulado
        String idManutencao = "3"; // input simulado
        List<Quarto> listaQuartos = ArquivoUtil.carregarLista("quartosTeste.json",
                new TypeReference<List<Quarto>>() {
                });
        Quarto q = null;
        Manutencao man = null;
        boolean quartoEncontrado = false;
        boolean manutencaoEncontrada = false;
        List<Manutencao> manLista = null;
        for (Quarto quarto : listaQuartos) {
            if (quarto.getId() == Integer.parseInt(idQuarto)) {
                q = quarto;
                quartoEncontrado = true;
                List<Manutencao> lista = quarto.getManutencoes();
                for (Manutencao m : lista) {
                    if (m.getIdManutencao() == Integer.parseInt(idManutencao)) {
                        man = m;
                        manLista = lista;
                        manutencaoEncontrada = true;
                        break;
                    }
                }
            }
            if (manutencaoEncontrada) {
                break;
            }
        }
        q.adicionarManutencao(man);
        f.removerManutencao(man, manLista);

        assertTrue(quartoEncontrado);
        assertTrue(manutencaoEncontrada);
        assertFalse(q.getManutencoes().contains(man));
        q.getManutencoes().add(man); // no final adicionamos outra vez para repetir o processo sempre que necessário

    }

    
    @Test
    public void RemoverReservaConfirmadaTest() throws ParseException {
        List<Quarto> quartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
        });
        Quarto q = null;
        boolean quartoEncontrado = false;
        boolean reservaEncontrada = false;
        for (Quarto quarto : quartos) {
            if (quarto.getId() == 28) { // teste para o quarto com id 28
                q = quarto;
                quartoEncontrado = true;
            }
        }
        if(q.getReservas().isEmpty()){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dataEntrada = sdf.parse("2025-02-01");
        Date dataSaida = sdf.parse("2025-02-05");
            q.getReservas().add(new Reserva(28,dataEntrada,dataSaida,"confirmada",28,0));
        }
        assertTrue(quartoEncontrado);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dataEntrada = sdf.parse("2025-02-01");
        Date dataSaida = sdf.parse("2025-02-05");

        Reserva r = new Reserva(dataEntrada, dataSaida);
        for (Reserva res : q.getReservas()) {
            if (ReservatemMesmasCaracteristicas(r, res)) {
                r = res;
                reservaEncontrada = true;
                break;
            }
        }
        assertTrue(reservaEncontrada);
        cancelarReservaConfirmada(r, q, quartos);
        quartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
        });
        for (Quarto q2 : quartos) {
            if (q2.getId() == 28) {
                assertTrue(q2.getReservas().isEmpty());
            }

        }
        q.getReservas().add(r); // para testes posteriores

    }

    @Test
    public void RemoverReservaConfirmadaTestFail() throws ParseException {
        // cenarios de insucesso - quarto existe mas manutenção não encontrada, quarto nao existe
        List<Quarto> quartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
        });
        Quarto q = null;
        boolean quartoEncontrado = false;
        boolean reservaEncontrada = false;
        for (Quarto quarto : quartos) {
            if (quarto.getId() == 28) { // teste para o quarto com id 28
                q = quarto;
                quartoEncontrado = true;
            }
        }
        if(q.getReservas().isEmpty()){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dataEntrada = sdf.parse("2025-02-01");
        Date dataSaida = sdf.parse("2025-02-05");
            q.getReservas().add(new Reserva(28,dataEntrada,dataSaida,"confirmada",28,0));
        }
        assertTrue(quartoEncontrado);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dataEntrada = sdf.parse("2035-02-01");
        Date dataSaida = sdf.parse("2035-02-05");

        Reserva r = new Reserva(dataEntrada, dataSaida);
        for (Reserva res : q.getReservas()) {
            if (ReservatemMesmasCaracteristicas(r, res)) {
                r = res;
                reservaEncontrada = true;
                break;
            }
        }
        assertFalse(reservaEncontrada);
        PrintStream originalOut = System.out;
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream printStreamCapturado = new PrintStream(saidaCapturada);
        System.setOut(printStreamCapturado);
        cancelarReservaConfirmada(r, q, quartos);
        System.setOut(originalOut);
        String resultado = saidaCapturada.toString();
        assertTrue(resultado.contains("Reserva não encontrada no quarto"));        
    }

    @Test
    public void RemoverReservaConfirmadaTestFail2() throws ParseException {
        // cenarios de insucesso - quarto existe mas manutenção não encontrada, quarto nao existe
        List<Quarto> quartos = ArquivoUtil.carregarLista("quartosTeste.json", new TypeReference<List<Quarto>>() {
        });
        Quarto q = null;
        boolean quartoEncontrado = false;
        boolean reservaEncontrada = false;
        for (Quarto quarto : quartos) {
            if (quarto.getId() == 712) { 
                q = quarto;
                quartoEncontrado = true;
            }
        }
        assertFalse(quartoEncontrado);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dataEntrada = sdf.parse("2035-02-01");
        Date dataSaida = sdf.parse("2035-02-05");

        Reserva r = new Reserva(dataEntrada, dataSaida);
        assertFalse(reservaEncontrada);
        PrintStream originalOut = System.out;
        ByteArrayOutputStream saidaCapturada = new ByteArrayOutputStream();
        PrintStream printStreamCapturado = new PrintStream(saidaCapturada);
        System.setOut(printStreamCapturado);
        cancelarReservaConfirmada(r, q, quartos);
        System.setOut(originalOut);
        String resultado = saidaCapturada.toString();
        assertTrue(resultado.contains("Quarto nao encontrado ou nulo"));        
    }

    public void cancelarReservaConfirmada(Reserva res, Quarto q, List<Quarto> list) {
        if(q == null){
            System.out.println("Quarto nao encontrado ou nulo");
            return;
        }
        for (Quarto quarto : list) {
            if (QuartotemMesmasCaracteristicas(quarto, q)) {
                List<Reserva> resLista = quarto.getReservas();
                Reserva reservaEncontrada = null;
                for (Reserva r : resLista) {
                    if (ReservatemMesmasCaracteristicas(r, res)) {
                        reservaEncontrada = r;
                        System.out.println("reserva encontrada!");
                        break;
                    }
                }

                if (reservaEncontrada != null) {
                    int index = resLista.indexOf(reservaEncontrada);
                    if (index != -1) {
                        resLista.remove(index);
                        System.out.println("Reserva removida");
                    } else {
                        System.out.println("Falha ao encontrar reserva.");
                    }
                    quarto.setListaReservas(resLista);
                    ArquivoUtil.guardarLista("quartosTeste.json", list);
                    System.out.println("Reserva cancelada com sucesso");
                } else {
                    System.out.println("Reserva não encontrada no quarto");
                }
                return;
            }
        }
        System.out.println("Quarto não encontrado na lista");
    }

    public boolean QuartotemMesmasCaracteristicas(Quarto q1, Quarto q2) {
        return q1.getCapacidade() == q2.getCapacidade() && q1.getnCamas() == q2.getnCamas()
                && q1.getnWC() == q2.getnWC() && q1.getTipoVista().equals(q2.getTipoVista())
                && q1.getStatus().equals(q2.getStatus()) && q1.isTemCozinha() == q2.isTemCozinha();
    }

    public boolean ReservatemMesmasCaracteristicas(Reserva res1, Reserva res2) {
        return res1.getDataEntrada().equals(res2.getDataEntrada()) && res1.getDataSaida().equals(res2.getDataSaida());

    }
    public void confirmarReserva(List<Reserva> reservasPendentes, int idReserva) {
        for (Reserva r : reservasPendentes) {
            if (r.getIdReserva() == idReserva) {
                r.alterarStatus("confirmada");
                return;
            }
        }
        System.out.println("Reserva não encontrada");
    }
}
