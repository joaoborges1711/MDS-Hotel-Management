package MDS;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Quarto {
    private int id, capacidade, nCamas, nWC;
    private String tipoVista, status;
    private boolean temCozinha;
    private List<Manutencao> manutencoes;
    private List<Reserva> reservas;

    public Quarto(int id, int capacidade, int nCamas, int nWC, String tipoVista, String status, boolean temCozinha) {
        this.id = id;
        this.capacidade = capacidade;
        this.nCamas = nCamas;
        this.nWC = nWC;
        this.tipoVista = tipoVista;
        this.status = status;
        this.temCozinha = temCozinha;
        this.manutencoes = new ArrayList<>();
        this.reservas = new ArrayList<>();
    }
    public Quarto(){
        
    }

    public int getId() {
        return id;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int novaCapacidade) {
        this.capacidade = novaCapacidade;
    }

    public int getnCamas() {
        return nCamas;
    }

    public void setnCamas(int nCamas) {
        this.nCamas = nCamas;
    }

    public int getnWC() {
        return nWC;
    }

    public void setnWC(int nWC) {
        this.nWC = nWC;
    }

    public String getTipoVista() {
        return tipoVista;
    }

    public void setTipoVista(String tipoVista) {
        this.tipoVista = tipoVista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isTemCozinha() {
        return temCozinha;
    }

    public void setTemCozinha(boolean temCozinha) {
        this.temCozinha = temCozinha;
    }

    public List<Manutencao> getManutencoes() {
        return manutencoes;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }

    public void adicionarManutencao(Manutencao manutencao) {
        if (!manutencoes.contains(manutencao)) {
            manutencoes.add(manutencao);
        }
    }

    public void removerManutencao(int idManutencao) {
        manutencoes.removeIf(m -> m.getIdManutencao() == idManutencao);
    }

    public void adicionarReserva(Reserva reserva) {
        for (Reserva r : reservas) {
            if (r.conflitaCom(reserva)) {
                throw new IllegalArgumentException("conflito de datas com uma reserva existente");
            }
        }
        reservas.add(reserva);
    }

    public void cancelarReserva(int idReserva) {
        for (Reserva r : reservas) {
            if (r.getIdReserva() == idReserva) {
                r.alterarStatus("Cancelada");
                return;
            }
        }
        System.out.println("Reserva não encontrada");
    }
    public void setListaReservas(List<Reserva> lista){
        this.reservas = lista;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Quarto quarto = (Quarto) obj;
        return id == quarto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public boolean isDisponivel(Date dataEntrada, Date dataSaida) {
        for (Reserva reserva : reservas) {
            if (reserva.conflitaCom(new Reserva(0, dataEntrada, dataSaida, "", this.id, 0))) {
                return false;
            }
        }
        return true; 
    }
    
    @Override
    public String toString() {
        return "Quarto{" + "id=" + id + ", capacidade=" + capacidade + ", nCamas=" + nCamas + ", nWC=" + nWC +
                ", tipoVista='" + tipoVista + '\'' + ", status='" + status + '\'' + ", temCozinha=" + temCozinha + '}';
    }
}
