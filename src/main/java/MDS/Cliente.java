package MDS;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


public class Cliente extends Person implements InterfaceGeral {
    //public int saldo;

    public Cliente(int idPessoa, String nome, String BI, LocalDate dataNascimento) {
        super(idPessoa, nome, BI, dataNascimento);
    }

    @Override
    public void verificarDisponibilidade(int idQuarto, List<Quarto> listaQuartos) {
        for (Quarto q : listaQuartos) {
            if (q.getId() == idQuarto && q.getStatus() == "disponivel") {
                System.out.println("quarto disponivel");
                return;
            }
        }
        System.out.println("quarto não encontrado ou indisponivel");
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
                        System.out.println("quarto encontrado: " + q);
                        encontrado = true;
                        break;
                    }
                }

                if (!encontrado) {
                    System.out.println("quarto não encontrado no ficheiro: " + quarto);
                }
            } else {
                System.out.println("ficheiro quartos.json não encontrado.");
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
                System.out.println("Ficheiro de reservasPendentes.json não encontrado.");
            }
        } catch (IOException e) {
            System.out.println("erro ao processar o ficheiro de reservas: " + e.getMessage());
        }
    }

    @Override
    public void consultarReserva(int idReserva) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("reservasPendentes.json");
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

    // método implicito no metodo fazerReserva
    @Override
    public void registarOcupacaoQuarto() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarOcupacaoQuarto'");
    }

}
