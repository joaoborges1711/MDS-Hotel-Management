package MDS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.io.File;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;

public class FuncionarioHotelImpl {
    public static void main(String[] args) {
        FuncionarioHotel funcionario = new FuncionarioHotel(1, "Admin", "123456", LocalDate.of(1985, 5, 10));
        Scanner scanner = new Scanner(System.in);
        List<Quarto> quartosTotais = ArquivoUtil.carregarLista("quartos.json", new TypeReference<List<Quarto>>() {
        });
        List<Reserva> reservasPendentes = ArquivoUtil.carregarLista("reservasPendentes.json",
                new TypeReference<List<Reserva>>() {
                });
        // Scanner s = new Scanner(System.in);
        int opcao = 0;
        while (opcao != 9) {
            System.out.print("\n==============================\n");
            System.out.println("     SISTEMA FUNCIONÁRIO");
            System.out.println("==============================");
            System.out.println("1 - Confirmar Reserva");
            System.out.println("2 - Adicionar Manutenção");
            System.out.println("3 - Remover Manutenção");
            System.out.println("4 - Procurar Quarto");
            System.out.println("5 - Cancelar Reserva Pendente");
            System.out.println("6 - Cancelar Reserva Confirmada");
            System.out.println("9 - SAIR");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            System.out.println();

            switch (opcao) {
                case 1:
                    if (reservasPendentes.size() == 0) {
                        System.out.println("não existem reservas pendentes de confirmação");
                        continue;
                    }
                    System.out.println("Lista das reservas pendentes:");

                    for (Reserva r : reservasPendentes) {
                        System.out.println(r);
                    }
                    System.out.print("Digite o ID da reserva que pretende confirmar: ");
                    int idReservaConfirmar = scanner.nextInt();

                    Reserva reservaConfirmada = null;

                    for (Reserva r : reservasPendentes) {
                        if (r.getIdReserva() == idReservaConfirmar) {
                            reservaConfirmada = r;
                            break;
                        }
                    }

                    if (reservaConfirmada == null) {
                        System.out.println("Reserva não encontrada");
                        break;
                    }

                    try {
                        List<Quarto> quartos = ArquivoUtil.carregarLista("quartos.json",
                                new TypeReference<List<Quarto>>() {
                                });
                        Quarto quartoReservado = null;
                        for (Quarto room1 : quartos) {
                            if (room1.getId() == reservaConfirmada.getIdQuarto()) {
                                quartoReservado = room1;
                                break;
                            }
                        }

                        if (quartoReservado == null) {
                            System.out.println("Erro: Quarto associado à reserva não encontrado");
                            break;
                        }

                        reservaConfirmada.alterarStatus("confirmada");
                        reservaConfirmada.setId(getMaxIdReserva(quartoReservado.getReservas()));
                        quartoReservado.getReservas().add(reservaConfirmada);

                        reservasPendentes.remove(reservaConfirmada);

                        ArquivoUtil.guardarLista("quartos.json", quartos);
                        ArquivoUtil.guardarLista("reservasPendentes.json", reservasPendentes);

                        System.out.println("Reserva confirmada com sucesso: " + reservaConfirmada);
                    } catch (Exception e) {
                        System.out.println("Erro ao confirmar reserva: " + e.getMessage());
                    }
                    break;

                case 2:
                    for (Quarto q : quartosTotais) {
                        System.out.println(q);
                    }
                    System.out.println("Detalhes da Manutenção:");
                    System.out.print("ID do Quarto: ");
                    int idQuarto = scanner.nextInt();
                    System.out.print("Tipo de Manutenção: ");
                    scanner.nextLine();
                    String tipoManutencao = scanner.nextLine().trim();

                    if (tipoManutencao.isEmpty()) {
                        System.out.println("tipo de manutenção não pode ser vazio");
                        break;
                    }

                    try {

                        List<Quarto> quartos = ArquivoUtil.carregarLista("quartos.json",
                                new TypeReference<List<Quarto>>() {
                                });

                        Quarto quarto = null;
                        for (Quarto room : quartos) {
                            if (room.getId() == idQuarto) {
                                quarto = room;
                                break;
                            }
                        }

                        if (quarto == null) {
                            System.out.println("Quarto não encontrado");
                        } else {

                            Manutencao manutencao = new Manutencao(getMaxIdManutencao(quarto.getManutencoes()), tipoManutencao);
                            quarto.getManutencoes().add(manutencao);

                            if (!"em manutenção".equals(quarto.getStatus())) {
                                quarto.setStatus("em manutenção");
                            }

                            ArquivoUtil.guardarLista("quartos.json", quartos);
                            System.out.println("Manutenção adicionada com sucesso: " + manutencao);
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao adicionar manutenção: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("ID da Manutenção para Remover: ");
                    int idManutencao = scanner.nextInt();
                    System.out.println("Digite o ID do Quarto:");
                    int idQuarto2 = scanner.nextInt();
                    boolean removido = false;

                    try {
                        List<Quarto> quartos = ArquivoUtil.carregarLista("quartos.json",
                                new TypeReference<List<Quarto>>() {
                                });

                        for (Quarto quarto : quartos) {
                            if (quarto.getId() == idQuarto2) {
                                for (Manutencao m : quarto.getManutencoes()) {
                                    if (m.getIdManutencao() == idManutencao) {
                                        quarto.getManutencoes().remove(m);
                                        if (quarto.getManutencoes().isEmpty()) {
                                            quarto.setStatus("disponivel");
                                        }
                                        removido = true;
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        if (removido) {
                            ArquivoUtil.guardarLista("quartos.json", quartos);
                            System.out.println("Manutenção removida com sucesso");
                        } else {
                            System.out.println("Manutenção não encontrada");
                        }
                    } catch (Exception e) {
                        System.out.println("Erro ao remover manutenção: " + e.getMessage());
                    }
                    break;

                case 4:
                    procurarQuarto(funcionario, scanner);
                    break;

                case 5:
                    if(reservasPendentes.isEmpty()){
                        System.out.println("Não existem reservas pendentes de confirmação");
                        continue;
                    }
                    System.out.println("Lista das reservas pendentes : " + reservasPendentes);
                    cancelarReserva(funcionario, scanner);
                    break;

                case 6:
                    System.out.println("Digite as características do quarto:");
                    System.out.println("Digite a capacidade do quarto:");
                    int capacidadeQ = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Digite o número de camas:");
                    int nCamasQ = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Digite o número de WC's:");
                    int nWCQ = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Digite o tipo de vista:");
                    String tipoVistaQ = scanner.nextLine();
                    System.out.println("O quarto tem cozinha? (s/n):");
                    boolean temCozinhaQ = scanner.nextLine().equalsIgnoreCase("s");
                    Quarto qu = new Quarto(0, capacidadeQ, nCamasQ, nWCQ, tipoVistaQ, "disponivel", temCozinhaQ);
                    try {
                        System.out.print("Data de Entrada (yyyy-MM-dd): ");
                        String entrada1 = scanner.next();
                        scanner.nextLine();
                        System.out.print("Data de Saída (yyyy-MM-dd): ");
                        String saida1 = scanner.next();
                        scanner.nextLine();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date dataEntrada = sdf.parse(entrada1);
                        Date dataSaida = sdf.parse(saida1);
                        if (!dataEntrada.before(dataSaida)) {
                            System.out.println("A data de entrada deve ser anterior à data de saída");
                            return;
                        }
                        Reserva r = new Reserva(dataEntrada, dataSaida);
                        funcionario.cancelarReservaConfirmada(r, qu, quartosTotais);
                    } catch (ParseException e) {
                        System.out.println("Erro ao interpretar as datas, certifique-se de usar o formato yyyy-MM-dd");
                        e.printStackTrace();
                    }
                    break;
                case 9:
                    System.out.println("A sair do sistema...");
                    break;

                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }

    private static void procurarQuarto(FuncionarioHotel f, Scanner scanner) {
        int idQuarto = lerNumero(scanner, "Digite o ID do quarto para procurar: ");
        Quarto quartoProcurado = new Quarto(idQuarto, 0, 0, 0, "", "", false);
        f.procurarQuarto(quartoProcurado);
    }

    private static int lerNumero(Scanner scanner, String mensagem) {
        int numero;
        while (true) {
            System.out.print(mensagem);
            try {
                numero = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("entrada inválida, por favor, insira um número");
            }
        }
        return numero;
    }

    private static void cancelarReserva(FuncionarioHotel f, Scanner scanner) {
        int idReserva = lerNumero(scanner, "Digite o ID da reserva a ser cancelada: ");
        f.cancelarReserva(idReserva);
    }
    public static int getMaxIdReserva(List<Reserva> list){
        if (list.isEmpty()) {
            return 1;
        }
        int max = list.get(0).getIdReserva();
        for(Reserva r : list){
            if(r.getIdReserva() > max){
                max = r.getIdReserva();
            }
        }
        return max + 1;
    }
    public static int getMaxIdManutencao(List<Manutencao> list){
        if (list.isEmpty()) {
            return 1; 
        }
        int max = list.get(0).getIdManutencao();
        for(Manutencao m : list){
            if(m.getIdManutencao() > max){
                max = m.getIdManutencao();
            }
        }
        return max + 1;
    }
}
