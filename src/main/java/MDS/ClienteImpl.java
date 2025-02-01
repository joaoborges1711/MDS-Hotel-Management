package MDS;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;

public class ClienteImpl {
    public static void main(String[] args) {
        Cliente cliente = new Cliente(1, "João", "123456789", LocalDate.of(1990, 1, 1));
        List<Quarto> listaQuartos = ArquivoUtil.carregarLista("quartos.json", new TypeReference<List<Quarto>>() {
        });
        // List<Quarto> quartosDisponiveis = new ArrayList<>();
        List<Reserva> reservasPendentes = ArquivoUtil.carregarLista("reservasPendentes.json",
                new TypeReference<List<Reserva>>() {
                });
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 9) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1:
                        verificarDisponibilidade(cliente, listaQuartos, scanner);
                        break;

                    case 2:
                        procurarQuarto(cliente, scanner);
                        break;

                    case 3:
                        if(reservasPendentes.isEmpty()){
                            System.out.println("\nNão existem reservas pendentes de confirmação");
                            continue;
                        }
                        System.out.println("Estas são as suas reservas (pendentes de confirmação):");
                        System.out.println("\n");
                        System.out.println(reservasPendentes);
                        System.out.println("\n");
                        cancelarReserva(cliente, scanner);
                        break;

                    case 4:
                        consultarReserva(cliente, scanner);
                        break;

                    case 5:
                        registrarOcupacao();
                        break;

                    case 6:
                        fazerReserva(cliente, listaQuartos, reservasPendentes, scanner);
                        break;

                    case 9:
                        System.out.println("Obrigado por usar o sistema! Adeus!");
                        break;

                    default:
                        System.out.println("Opção inválida, tente novamente");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, por favor, insira um número válido");
            }
        }

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n==============================");
        System.out.println("   BEM VINDO AO SISTEMA DE CLIENTE");
        System.out.println("==============================");
        // System.out.println("1 - Verificar Disponibilidade de Quarto");
        System.out.println("2 - Procurar Quarto");
        System.out.println("3 - Cancelar Reserva");
        System.out.println("4 - Consultar Reserva");
        // System.out.println("5 - Registar Ocupação de Quarto");
        System.out.println("6 - Fazer Reserva");
        System.out.println("9 - Sair");
        System.out.print("Escolha uma operação: ");
    }

    private static void verificarDisponibilidade(Cliente cliente, List<Quarto> listaQuartos, Scanner scanner) {
        int idQuarto = lerNumero(scanner, "Digite o ID do quarto: ");
        Quarto quarto = encontrarQuartoPorId(idQuarto, listaQuartos);
        if (quarto != null) {
            System.out.print("Data de Entrada (yyyy-MM-dd): ");
            String entrada = scanner.next();
            System.out.print("Data de Saída (yyyy-MM-dd): ");
            String saida = scanner.next();
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date dataEntrada = sdf.parse(entrada);
                Date dataSaida = sdf.parse(saida);
                if (quarto.isDisponivel(dataEntrada, dataSaida)) {
                    System.out.println("O quarto está disponível para o período solicitado");
                } else {
                    System.out.println("O quarto está ocupado para o período solicitado");
                }
            } catch (Exception e) {
                System.out.println("Erro ao processar as datas: " + e.getMessage());
            }
        } else {
            System.out.println("Quarto não encontrado.");
        }
    }

    public static void procurarQuarto(Cliente cliente, Scanner scanner) {
        int idQuarto = lerNumero(scanner, "Digite o ID do quarto para procurar: ");
        Quarto quartoProcurado = new Quarto(idQuarto, 0, 0, 0, "", "", false);
        cliente.procurarQuarto(quartoProcurado);
    }

    public static void cancelarReserva(Cliente cliente, Scanner scanner) {
        int idReserva = lerNumero(scanner, "Digite o ID da reserva a ser cancelada: ");
        cliente.cancelarReserva(idReserva);
    }

    public static void consultarReserva(Cliente cliente, Scanner scanner) {
        System.out.println("Lista de reservas pendentes: ");
        List<Reserva> reservasPendentes = ArquivoUtil.carregarLista("reservasPendentes.json",
                new TypeReference<List<Reserva>>() {
                });
        
        if(reservasPendentes.isEmpty()){
            System.out.println("não existem reservas pendentes de confirmação");
            return;
        }
        
        System.out.println(reservasPendentes);
        System.out.println("\n\n");
        int idReserva = lerNumero(scanner, "Digite o ID da reserva para consultar: ");
        cliente.consultarReserva(idReserva);
    }

    private static void registrarOcupacao() {
        System.out.println("Registar Ocupação de Quarto ainda não implementado");
    }

    private static void fazerReserva(Cliente cliente, List<Quarto> quartos, List<Reserva> reservasPendentes,
            Scanner scanner) {
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
            System.out.print("Capacidade mínima do quarto: ");
            int capacidade = scanner.nextInt();
            System.out.print("Número de camas mínimas: ");
            int nCamas = scanner.nextInt();
            System.out.print("Número de WC's mínimos: ");
            int nWC = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Tipo de vista desejada (ex: mar, cidade): ");
            String tipoVista = scanner.nextLine();
            System.out.print("O quarto deve ter cozinha? (s/n): ");
            boolean temCozinha = scanner.next().equalsIgnoreCase("s");
            Quarto quartoDesejado = new Quarto(0, capacidade, nCamas, nWC, tipoVista, "", temCozinha);

            System.out.println("Quartos disponíveis para o período:");
            List<Quarto> quartosDisponiveis = new ArrayList<>();
            for (Quarto quarto : quartos) {
                if (quarto.isDisponivel(dataEntrada, dataSaida) && !"em manutenção".equals(quarto.getStatus())) {
                    System.out.println(quarto);
                    quartosDisponiveis.add(quarto);
                }
            }

            if (quartosDisponiveis.isEmpty()) {
                System.out.println("Não existem quartos disponíveis para o período selecionado ou todos estão em manutenção");
                return;
            }

            Quarto melhorQuarto = sugerirQuarto(quartoDesejado, quartosDisponiveis);

            if (melhorQuarto != null) {
                System.out.println("\n");
                System.out.println("#########################################");
                System.out.println();
                System.out.println("\nSugestão do melhor quarto baseado nas suas preferências:");
                System.out.println(melhorQuarto);
                System.out.print("\nDeseja aceitar esta sugestão? (s/n): ");
                String resposta = scanner.next();
                while(!resposta.equalsIgnoreCase("s") && !resposta.equalsIgnoreCase("n") ){
                    System.out.println("Por favor digite apenas s ou n");
                    System.out.print("\nDeseja aceitar esta sugestão? (s/n): ");
                    resposta = scanner.next();
                }
                if ("s".equalsIgnoreCase(resposta)) {
                    Reserva novaReserva = new Reserva(getMaxIdReserva(reservasPendentes), dataEntrada, dataSaida, "pendente",
                            melhorQuarto.getId(), cliente.getIdPessoa());
                    reservasPendentes.add(novaReserva);
                    ArquivoUtil.guardarLista("reservasPendentes.json", reservasPendentes);
                    System.out.println("Reserva enviada com sucesso. Aguarde a confirmação.");
                    return;
                }
            }

            int idQuarto = lerNumero(scanner, "Digite o ID do quarto para fazer a reserva: ");
            Quarto quartoSelecionado = encontrarQuartoPorId(idQuarto, quartosDisponiveis);

            if (quartoSelecionado == null) {
                System.out.println("Quarto não encontrado ou indisponível");
                return;
            }

            Reserva novaReserva = new Reserva(getMaxIdReserva(reservasPendentes), dataEntrada, dataSaida, "pendente",
                    idQuarto, cliente.getIdPessoa());
            reservasPendentes.add(novaReserva);
            ArquivoUtil.guardarLista("reservasPendentes.json", reservasPendentes);

            System.out.println("Reserva enviada com sucesso, aguarde a confirmação");
        } catch (Exception e) {
            System.out.println("Erro ao processar a reserva: " + e.getMessage());
        }
    }

    private static Quarto encontrarQuartoPorId(int id, List<Quarto> listaQuartos) {
        for (Quarto quarto : listaQuartos) {
            if (quarto.getId() == id) {
                return quarto;
            }
        }
        return null;
    }

    private static int lerNumero(Scanner scanner, String mensagem) {
        int numero;
        while (true) {
            System.out.print(mensagem);
            try {
                numero = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, por favor, insira um número");
            }
        }
        return numero;
    }

    private static Quarto sugerirQuarto(Quarto q, List<Quarto> quartosDisponiveis) {
        HashMap<Integer, Quarto> quartosControlo = new HashMap<>();

        for (Quarto quarto : quartosDisponiveis) {
            int controlo = 0;

            if (quarto.getCapacidade() >= q.getCapacidade()) {
                controlo++;
            }
            if (quarto.getnCamas() >= q.getnCamas()) {
                controlo++;
            }
            if (quarto.getnWC() >= q.getnWC()) {
                controlo++;
            }
            if (quarto.getTipoVista().equals(q.getTipoVista())) {
                controlo++;
            }
            if (quarto.isTemCozinha() == q.isTemCozinha()) {
                controlo++;
            }
            quartosControlo.put(controlo, quarto);
        }

        int maxControlo = -1;
        Quarto melhorQuarto = null;

        for (Map.Entry<Integer, Quarto> entry : quartosControlo.entrySet()) {
            if (entry.getKey() > maxControlo) {
                maxControlo = entry.getKey();
                melhorQuarto = entry.getValue();
            }
        }

        return melhorQuarto;
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
}
