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
        List<Quarto> quartosTotais = g.carregarListaDeQuartos(); // Lista inicial de quartos
        System.out.println(quartosTotais);
        
        /*Quarto quarto1 = new Quarto(1, 2, 1, 1, "Vista Mar", "disponivel", true);
        Quarto quarto2 = new Quarto(2, 3, 2, 1, "Vista Cidade", "ocupado", false);
        quartosTotais.add(quarto1);
        quartosTotais.add(quarto2);*/

        try {
            Scanner s = new Scanner(System.in);
            int c = 0;
            while (c != 9) {
                // Menu
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
                        /*System.out.println("Detalhes do Novo Quarto:");
                        System.out.print("ID do Quarto: ");
                        int idQuartoNovo = s.nextInt();*/
                        System.out.print("Capacidade: ");
                        int capacidade = s.nextInt();
                        System.out.print("Número de Camas: ");
                        int nCamas = s.nextInt();
                        System.out.print("Número de WC: ");
                        int nWC = s.nextInt();
                        System.out.print("Tipo de Vista: ");
                        s.nextLine(); // Limpar buffer
                        String tipoVistaNovo = s.nextLine();
                        System.out.print("Status do Quarto (disponivel/em manutenção): ");
                        String statusNovo = s.nextLine();
                        System.out.print("Tem Cozinha (true/false): ");
                        boolean temCozinhaNovo = s.nextBoolean();

                        // Criar o novo quarto
                        Quarto novoQuarto = new Quarto(quartosTotais.size() + 1, capacidade, nCamas, nWC, tipoVistaNovo, statusNovo, temCozinhaNovo);
                        g.adicionarQuarto(novoQuarto);
                        g.guardarListaDeQuartos();

                        break;

                    case 3:
                    System.out.println("lista de todos os Quartos:");
                    for (Quarto quarto : quartosTotais) {
                        System.out.println(quarto);
                    }
                    System.out.print("ID do Quarto para Atualizar: ");
                        int idQuartoAtualizar = s.nextInt();
                        Quarto quartoAtualizar = g.consultarQuarto(idQuartoAtualizar);
                        if (quartoAtualizar == null) {
                            System.out.println("esse quarto não existe!");
                        } else {
                            System.out.print("novo Status do Quarto (disponivel/em manutenção): ");
                            s.nextLine(); 
                            String novoStatus = s.nextLine();
                            quartoAtualizar.setStatus(novoStatus);
                            System.out.println("status atualizado com sucesso: " + quartoAtualizar);
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
                            System.out.println("Quarto removido com sucesso!");
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
                        System.out.println("Opção inválida!");
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

