package MDS;

import java.util.List;

interface InterfaceGeral {
    void verificarDisponibilidade(int idQuarto, List<Quarto> listaQuartos);
    void procurarQuarto(Quarto quarto);
    void cancelarReserva(int idReserva);
    void consultarReserva(int idReserva);
    void registarOcupacaoQuarto();
}