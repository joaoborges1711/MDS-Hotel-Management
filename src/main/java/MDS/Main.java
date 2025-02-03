package MDS;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 9) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1: 
                        ClienteImpl.main(new String[0]);
                        break;

                    case 2: 
                        FuncionarioHotelImpl.main(new String[0]);
                        break;

                    case 3:
                        GestorSistemaImpl.main(new String[0]);
                        break;

                    case 4: 
                        GestorHotelImpl.main(new String[0]);
                        break;

                    case 9:
                        System.out.println("A sair...");
                        break;

                    default:
                        System.out.println("Opção inválida, por favor, tente novamente");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("entrada inválida, por favor, insira um número");
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n==============================");
        System.out.println("      SISTEMA PRINCIPAL       ");
        System.out.println("==============================");
        System.out.println("1 - Sistema Cliente");
        System.out.println("2 - Sistema Funcionário");
        System.out.println("3 - Sistema Gestor Sistema");
        System.out.println("4 - Sistema Gestor Hotel");
        System.out.println("9 - Sair");
        System.out.print("Escolha uma opção: ");
    }
}
