package MDS;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
//import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FuncionarioHotel extends Person implements interfaceAdmin, InterfaceGeral {

    public FuncionarioHotel(int idPessoa, String nome, String BI, LocalDate dataNascimento) {
        super(idPessoa, nome, BI, dataNascimento);
    }

    @Override
    public void verificarManutencao(Manutencao manutencao) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verificarManutencao'");
    }

    @Override
    public void confirmarReserva(List<Reserva> reservasPendentes, int idReserva) {
        for (Reserva r : reservasPendentes) {
            if (r.getIdReserva() == idReserva) {
                r.alterarStatus("confirmada");
                return;
            }
        }
        System.out.println("Reserva não encontrada");
    }

    @Override
    public void adicionarManutencao(Manutencao manutencao, List<Manutencao> listaManutencoes) {
        for (Manutencao m : listaManutencoes) {
            if (m.getIdManutencao() == manutencao.getIdManutencao()) {
                System.out.println("Essa manutenção já foi adicionada");
                return;
            }
        }
        listaManutencoes.add(manutencao);
    }

    @Override
    public void removerManutencao(Manutencao m, List<Manutencao> listaManutencoes) {
        listaManutencoes.removeIf(man -> man.getIdManutencao() == m.getIdManutencao());
    }

    @Override
    public void modificarQuarto(int id, int capacidade, int nCamas, int nWC, String tipoVista, String status,
            boolean temCozinha) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarQuarto'");
    }

    @Override
    public void adicionarQuarto(Quarto quarto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'adicionarQuarto'");
    }

    @Override
    public void removerQuarto(Quarto quarto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removerQuarto'");
    }

    @Override
    public void verificarDisponibilidade(int idQuarto, List<Quarto> listaQuartos) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verificarDisponibilidade'");
    }

    @Override
    public void procurarQuarto(Quarto quarto) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("quartos.json");
            if (arquivo.exists()) {
                List<Quarto> quartos = objectMapper.readValue(arquivo, new TypeReference<List<Quarto>>() {
                });
                boolean encontrado = false;
                for (Quarto q : quartos) {
                    if (q.getId() == quarto.getId()) {
                        System.out.println("Quarto encontrado: " + q);
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    System.out.println("Quarto não encontrado no ficheiro: " + quarto);
                }
            } else {
                System.out.println("Ficheiro quartos.json não encontrado.");
            }
        } catch (IOException e) {
            System.out.println("erro ao carregar quartos: " + e.getMessage());
        }
    }

    @Override
    public void cancelarReserva(int idReserva) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("reservasPendentes.json");
            if (arquivo.exists()) {
                List<Reserva> reservas = objectMapper.readValue(arquivo, new TypeReference<List<Reserva>>() {
                });
                boolean reservaRemovida = reservas.removeIf(reserva -> reserva.getIdReserva() == idReserva);

                if (reservaRemovida) {
                    objectMapper.writeValue(arquivo, reservas);
                    System.out.println("Reserva com ID " + idReserva + " foi cancelada com sucesso.");
                } else {
                    System.out.println("Reserva com ID " + idReserva + " não encontrada.");
                }
            } else {
                System.out.println("Arquivo de reservas.json não encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao processar o arquivo de reservas: " + e.getMessage());
        }
    }

    @Override
    public void consultarReserva(int idReserva) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'consultarReserva'");
    }

    @Override
    public void registarOcupacaoQuarto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarOcupacaoQuarto'");
    }

    public void cancelarReservaConfirmada(Reserva res, Quarto q, List<Quarto> list) {
        for (Quarto quarto : list) {
            if (QuartotemMesmasCaracteristicas(quarto, q)) {
                List<Reserva> resLista = quarto.getReservas();
                // System.out.println("##########LISTA ANTES DA REMOÇÃO#############");
                //System.out.println(resLista);
                Reserva reservaEncontrada = null;
                for (Reserva r : resLista) {
                    // System.out.println("A comparar com: " + r);
                    if (ReservatemMesmasCaracteristicas(r, res)) {
                        reservaEncontrada = r;
                        break;
                    }
                }

                if (reservaEncontrada != null) {
                    // System.out.println("A tentar remover: " + reservaEncontrada);
                    int index = resLista.indexOf(reservaEncontrada);
                    if (index != -1) {
                        resLista.remove(index);
                        System.out.println("Reserva removida");
                    } else {
                        System.out.println("Falha ao encontrar reserva.");
                    }

                    quarto.setListaReservas(resLista);
                    // System.out.println("##########LISTA APÓS REMOÇÃO#############");
                    // System.out.println(quarto.getReservas());
                    ArquivoUtil.guardarLista("quartos.json", list);
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
}
