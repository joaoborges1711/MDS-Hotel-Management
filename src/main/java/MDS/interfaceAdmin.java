package MDS;

import java.util.List;

public interface interfaceAdmin {
    void verificarManutencao(Manutencao manutencao);
    void confirmarReserva(List<Reserva> reservasPendentes, int idReserva);
    void adicionarManutencao(Manutencao manutencao,List<Manutencao> listaManutencoes);
    void removerManutencao(Manutencao m,List<Manutencao> listaManutencoes);
    void modificarQuarto(int id, int capacidade, int nCamas, int nWC, String tipoVista, String status, boolean temCozinha);
    void adicionarQuarto(Quarto quarto);
    void removerQuarto(Quarto quarto);
}
