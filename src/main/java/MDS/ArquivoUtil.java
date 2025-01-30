package MDS;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArquivoUtil {

    public static <T> List<T> carregarLista(String caminhoArquivo, TypeReference<List<T>> typeReference) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File arquivo = new File(caminhoArquivo);
            if (arquivo.exists()) {
                return objectMapper.readValue(arquivo, typeReference);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar lista: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    public static <T> void guardarLista(String caminhoArquivo, List<T> lista) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(caminhoArquivo), lista);
        } catch (IOException e) {
            System.out.println("Erro ao guardar lista: " + e.getMessage());
        }
    }
}
