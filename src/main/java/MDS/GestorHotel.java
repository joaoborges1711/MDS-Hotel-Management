package MDS;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GestorHotel extends Person implements interfaceAdmin {

    private List<Quarto> quartosGeridos;

    public GestorHotel(int idPessoa, String nome, String BI, LocalDate dataNascimento) {
        super(idPessoa, nome, BI, dataNascimento);
        this.quartosGeridos = new ArrayList<>();
    }

    public Quarto consultarQuarto(int idQuarto, List<Quarto> list) {
        for (Quarto quarto : list) {
            if (quarto.getId() == idQuarto) {
                return quarto;
            }
        }
        return null;
    }

    public List<Quarto> consultarQuartosDisponiveis(List<Quarto> totalQuartos, Date dataEntrada, Date dataSaida) {
        List<Quarto> quartosDisponiveis = new ArrayList<>();
        for (Quarto q : totalQuartos) {
            if (q.isDisponivel(dataEntrada, dataSaida)) {
                quartosDisponiveis.add(q);
            }
        }
        return quartosDisponiveis;
    }

    public List<Quarto> listarQuartos() {
        return new ArrayList<>(quartosGeridos);
    }

    @Override
    public String toString() {
        return "GestorHotel{" +
                "idPessoa=" + getIdPessoa() +
                ", BI=" + getBI() +
                ", nome='" + getNome() + '\'' +
                ", quartosGeridos=" + quartosGeridos.size() +
                '}';
    }

    @Override
    public void verificarManutencao(Manutencao m) {
        System.out.println(m.toString());
    }

    @Override
    public void confirmarReserva(List<Reserva> reservasPendentes, int idReserva) {
        for (Reserva r : reservasPendentes) {
            if (r.getIdReserva() == idReserva) {
                r.alterarStatus("confirmada");
                return;
            }
        }
        System.out.println("reserva não encontrada");
    }
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
    public void cancelarReservaConfirmada(Reserva res, Quarto q, List<Quarto> list) {
        for (Quarto quarto : list) {
            if (QuartotemMesmasCaracteristicas(quarto, q)) {
                List<Reserva> resLista = quarto.getReservas();
                // System.out.println("##########LISTA ANTES DA REMOÇÃO#############");
                System.out.println(resLista);
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
        System.out.println("Quarto não encontrado na lista.");
    }
    @Override
    public void adicionarManutencao(Manutencao manutencao, List<Manutencao> listaManutencoes) {
        for (Manutencao m : listaManutencoes) {
            if (m.getIdManutencao() == manutencao.getIdManutencao()) {
                System.out.println("essa manutenção já foi adicionada");
                return;
            }
        }
        listaManutencoes.add(manutencao);
    }

    @Override
    public void removerManutencao(Manutencao m, List<Manutencao> listaManutencoes) {
        listaManutencoes.removeIf(man -> man.getIdManutencao() == m.getIdManutencao());
    }

    public List<Quarto> carregarListaDeReservas() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("reservas.json");
            if (arquivo.exists()) {
                return objectMapper.readValue(arquivo, new TypeReference<List<Quarto>>() {
                });
            }
        } catch (IOException e) {
            System.out.println("erro ao carregar lista" + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void guardarListaDeReservas(List<Reserva> listaReservas) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("reservas.json"), listaReservas);
        } catch (IOException e) {
            System.out.println("erro ao carregar lista: " + e.getMessage());
        }
    }

    @Override
    public void modificarQuarto(int id, int capacidade, int nCamas, int nWC, String tipoVista, String status,
            boolean temCozinha) {
        throw new UnsupportedOperationException("não é suposto implementar");
    }

    @Override
    public void adicionarQuarto(Quarto quarto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("não é suposto implementar'");
    }

    @Override
    public void removerQuarto(Quarto quarto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("não é suposto implementar");
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
