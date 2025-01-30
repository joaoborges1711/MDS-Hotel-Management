package MDS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestorSistema extends Person implements interfaceAdmin {

    private List<Quarto> listaQuartos;

    public GestorSistema(int idPessoa, String nome, String BI, LocalDate dataNascimento) {
        super(idPessoa, nome, BI, dataNascimento);
        this.listaQuartos = carregarListaDeQuartos();
    }

    public List<Quarto> getLista() {
        return this.listaQuartos;
    }

    public List<Quarto> carregarListaDeQuartos() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File("quartos.json");
            if (arquivo.exists()) {
                return objectMapper.readValue(arquivo, new TypeReference<List<Quarto>>() {});
            }
        } catch (IOException e) {
            System.out.println("erro ao carregar lista " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public void guardarListaDeQuartos() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File("quartos.json"), listaQuartos);
        } catch (IOException e) {
            System.out.println("erro ao guardar lista  " + e.getMessage());
        }
    }

    @Override
    public void adicionarQuarto(Quarto quarto) {
        if (!listaQuartos.contains(quarto)) {
            listaQuartos.add(quarto);
            guardarListaDeQuartos();
            System.out.println("Quarto adicionado com sucesso: " + quarto);
        } else {
            System.out.println("esse quarto já existe na lista");
        }
    }

    @Override
    public void removerQuarto(Quarto quarto) {
        if (listaQuartos.remove(quarto)) {
            guardarListaDeQuartos();
            System.out.println("quarto removido com sucesso: " + quarto);
        } else {
            System.out.println("quarto não encontrado na lista");
        }
    }

    @Override
    public void modificarQuarto(int id, int capacidade, int nCamas, int nWC, String tipoVista, String status, boolean temCozinha) {
        for (Quarto q : listaQuartos) {
            if (q.getId() == id) {
                q.setCapacidade(capacidade);
                q.setnCamas(nCamas);
                q.setnWC(nWC);
                q.setTipoVista(tipoVista);
                q.setStatus(status);
                q.setTemCozinha(temCozinha);
                guardarListaDeQuartos();
                System.out.println("quarto atualizado com sucesso: " + q);
                return;
            }
        }
        System.out.println("quarto não encontrado");
    }

    public Quarto consultarQuarto(int idQuarto) {
        for (Quarto quarto : listaQuartos) {
            if (quarto.getId() == idQuarto) {
                return quarto;
            }
        }
        System.out.println("quarto com ID " + idQuarto + " não encontrado");
        return null;
    }

   
    @Override
    public void verificarManutencao(Manutencao manutencao) {
        throw new UnsupportedOperationException("não é suposto implementar");
    }

    @Override
    public void confirmarReserva(List<Reserva> reservasPendentes, int idReserva) {
        throw new UnsupportedOperationException("não é suposto implementar");
    }

    @Override
    public void adicionarManutencao(Manutencao manutencao, List<Manutencao> listaManutencoes) {
        throw new UnsupportedOperationException("não é suposto implementar");
    }

    @Override
    public void removerManutencao(Manutencao m, List<Manutencao> listaManutencoes) {
        throw new UnsupportedOperationException("não é suposto implementar");
    }
}
