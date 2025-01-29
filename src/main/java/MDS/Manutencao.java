package MDS;

//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
import java.util.Objects;


public class Manutencao {
    private int idManutencao;
    private String tipoManutencao;

    public Manutencao(int idManutencao, String tipoManutencao) {
        this.idManutencao = idManutencao;
        this.tipoManutencao = tipoManutencao;
    }
    public Manutencao(){

    }
    public int getIdManutencao() {
        return idManutencao;
    }

    public String getTipoManutencao() {
        return tipoManutencao;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Manutencao that = (Manutencao) obj;
        return idManutencao == that.idManutencao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idManutencao);
    }

    @Override
    public String toString() {
        return "Manutencao{" + "idManutencao=" + idManutencao + ", tipoManutencao='" + tipoManutencao + '\'' + '}';
    }
}