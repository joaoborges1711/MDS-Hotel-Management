package MDS;

//import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//import java.util.Date;
//import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class GestorSistemaImpl {
    public static void main(String[] args) {
        GestorSistema g = new GestorSistema(0, "admin", "admin", LocalDate.of(2000, 1, 1));
        List<Quarto> quartosTotais = g.carregarListaDeQuartos(); 
        System.out.println(quartosTotais);
        try {
            Scanner s = new Scanner(System.in);
            int c = 0;
            while (c != 9) {

                System.out.print("\n==============================\n");
                System.out.println("   BEM VINDO AO SISTEMA DE   ");
                System.out.println("     GESTÃO DO SISTEMA");
                System.out.print("==============================\n");
                System.out.println("1 - Consultar Quarto");
                System.out.println("2 - Adicionar Quarto");
                System.out.println("3 - Atualizar Quarto");
                System.out.println("4 - Remover Quarto");
                System.out.println("5 - Listar Quartos");
                System.out.println("9 - SAIR\n");
                System.out.print("Escolha uma operação: ");
                c = s.nextInt();
                System.out.println();

                switch (c) {
                    case 1:
                        System.out.println("lista de todos os Quartos:");
                        for (Quarto quarto : quartosTotais) {
                            System.out.println(quarto);
                        }
                        System.out.print("ID do Quarto: ");
                        int idQuarto = s.nextInt();
                        Quarto q = g.consultarQuarto(idQuarto);
                        if (q == null) {
                            System.out.println("Esse quarto não existe!");
                        } else {
                            System.out.println(q);
                        }
                        break;

                    case 2:
                        System.out.print("Capacidade: ");
                        int capacidade = s.nextInt();
                        System.out.print("Número de Camas: ");
                        int nCamas = s.nextInt();
                        System.out.print("Número de WC: ");
                        int nWC = s.nextInt();
                        System.out.print("Tipo de Vista: ");
                        s.nextLine();
                        String tipoVistaNovo = s.nextLine();
                        System.out.print("Status do Quarto (disponivel/em manutenção): ");
                        String statusNovo = s.nextLine();
                        System.out.print("Tem Cozinha (true/false): ");
                        boolean temCozinhaNovo = s.nextBoolean();

                        Quarto novoQuarto = new Quarto(getMaxIdQuarto(quartosTotais), capacidade, nCamas, nWC, tipoVistaNovo,
                                statusNovo, temCozinhaNovo);
                        g.adicionarQuarto(novoQuarto);
                        g.guardarListaDeQuartos();

                        break;

                    case 3:
                        System.out.println("Lista de todos os Quartos:");
                        for (Quarto quarto : quartosTotais) {
                            System.out.println(quarto);
                        }

                        System.out.print("ID do Quarto para Atualizar: ");
                        int idQuartoAtualizar = s.nextInt();
                        Quarto quartoAtualizar = g.consultarQuarto(idQuartoAtualizar);

                        if (quartoAtualizar == null) {
                            System.out.println("Esse quarto não existe");
                        } else {
                            System.out.print("Nova capacidade do Quarto: ");
                            int novaCapacidade = s.nextInt();
                            quartoAtualizar.setCapacidade(novaCapacidade);

                            System.out.print("Novo número de camas: ");
                            int novoNCamas = s.nextInt();
                            quartoAtualizar.setnCamas(novoNCamas);

                            System.out.print("Novo número de WC's: ");
                            int novoWC = s.nextInt();
                            quartoAtualizar.setnWC(novoWC);

                            s.nextLine();
                            System.out.print("Nova vista do Quarto: ");
                            String novaVista = s.nextLine();
                            quartoAtualizar.setTipoVista(novaVista);

                            System.out.print("Tem cozinha? (s/n): ");
                            boolean temCozinhaNovo2 = s.nextLine().equalsIgnoreCase("s");
                            quartoAtualizar.setTemCozinha(temCozinhaNovo2);
                            g.guardarListaDeQuartos();

                            System.out.println("Dados do quarto atualizados com sucesso");
                            System.out.println("Quarto com id " + quartoAtualizar.getId() + ":");
                            System.out.println(quartoAtualizar);
                        }
                        break;

                    case 4:
                        System.out.println("lista de todos os Quartos:");
                        for (Quarto quarto : quartosTotais) {
                            System.out.println(quarto);
                        }
                        System.out.print("ID do Quarto para Remover: ");
                        int idQuartoRemover = s.nextInt();
                        Quarto quartoRemover = g.consultarQuarto(idQuartoRemover);
                        if (quartoRemover != null) {
                            g.removerQuarto(quartoRemover);
                            System.out.println("Quarto removido com sucesso");
                        } else {
                            System.out.println("Esse quarto não existe");
                        }
                        break;

                    case 5:
                        System.out.println("lista de todos os Quartos:");
                        for (Quarto quarto : quartosTotais) {
                            System.out.println(quarto);
                        }
                        break;

                    case 9:
                        System.out.println("Adeus!");
                        s.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Opção inválida");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int getMaxIdQuarto(List<Quarto> list){
        if (list.isEmpty()) {
            return 1; 
        }
        int max = list.get(0).getId();
        for(Quarto q : list){
            if(q.getId() > max){
                max = q.getId();
            }
        }
        return max + 1;
    }
}
