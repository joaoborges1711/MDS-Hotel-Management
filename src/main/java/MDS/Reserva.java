package MDS;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reserva {
    private int idReserva, idQuarto;
    @JsonProperty("idcliente")
    private int idCliente;
    private Date dataEntrada, dataSaida;
    private String status; //confirmada, pendente

    public Reserva(int idReserva, Date dataEntrada, Date dataSaida, String status, int idQuarto, int idCliente) {
        this.idReserva = idReserva;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.status = status;
        this.idQuarto = idQuarto;
        this.idCliente = idCliente;
    }
    public Reserva(){

    }

    public Reserva(Date dataEntrada, Date dataSaida){
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
    }
    public void setId(int id){
        this.idReserva = id;
    }
public int getIdCliente() {
    return idCliente;
}

    public int getIdReserva() {
        return idReserva;
    }
    public int getIdQuarto() {
        return idQuarto;
    }

    public Date getDataEntrada() {
        return dataEntrada;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public String getStatus() {
        return status;
    }

    public void alterarStatus(String novoStatus) {
        this.status = novoStatus;
    }

    public boolean conflitaCom(Reserva outra) {
        return !(this.dataSaida.before(outra.dataEntrada) || this.dataEntrada.after(outra.dataSaida));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return idReserva == reserva.idReserva && 
               dataEntrada.equals(reserva.dataEntrada) &&
               dataSaida.equals(reserva.dataSaida);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva, dataEntrada, dataSaida);
    }

    @Override
    public String toString() {
        return "Reserva{" + "idReserva=" + idReserva + ", dataEntrada=" + dataEntrada + ", dataSaida=" + dataSaida +
                ", status='" + status + '\'' + '}';
    }
}