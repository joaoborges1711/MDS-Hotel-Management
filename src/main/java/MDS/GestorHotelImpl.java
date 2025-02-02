package MDS;

import java.util.List;
import java.util.Scanner;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class GestorHotelImpl {
    public static void main(String[] args) {
        GestorHotel g = new GestorHotel(0, "joao", "123", LocalDate.of(2003, 12, 12));
        List<Quarto> quartosTotais = ArquivoUtil.carregarLista("quartos.json", new TypeReference<List<Quarto>>() {
        });
        List<Reserva> reservasPendentes = ArquivoUtil.carregarLista("reservasPendentes.json",
                new TypeReference<List<Reserva>>() {
                });

        try {
            Scanner s = new Scanner(System.in);
            int c = 0;
            while (c != 9) {

                System.out.print("\n==============================\n");
                System.out.println("   BEM VINDO AO SISTEMA DE   ");
                System.out.println("      GESTÃO DO HOTEL");
                System.out.print("==============================\n");
                System.out.println("1 - Consultar Quarto");
                System.out.println("2 - Confirmar Reserva");
                System.out.println("3 - Adicionar Manutenção");
                System.out.println("4 - Adicionar Reserva");
                System.out.println("5 - Listar Quartos");
                System.out.println("6 - Remover Manutenção");
                System.out.println("7 - Cancelar Reserva Pendente");
                System.out.println("8 - Cancelar Reserva Confirmada");
                System.out.println("9 - SAIR\n");
                System.out.print("Escolha uma operação: ");
                c = s.nextInt();
                System.out.println();

                switch (c) {
                    case 1:
                        System.out.print("ID do Quarto: ");
                        int idQuarto = s.nextInt();
                        Quarto q = g.consultarQuarto(idQuarto, quartosTotais);
                        if (q == null) {
                            System.out.println("Esse quarto não existe");
                        } else {
                            System.out.println(q);
                        }
                        break;

                    case 2:
                    if (reservasPendentes.size() == 0) {
                        System.out.println("não existem reservas pendentes de confirmação");
                        continue;
                    }
                    System.out.println("Lista das reservas pendentes:");

                    for (Reserva r : reservasPendentes) {
                        System.out.println(r);
                    }
                        System.out.println("Digite o ID da reserva que pretende confirmar: ");
                        int idReservaConfirmar = s.nextInt();
                        if(idReservaConfirmar == 0){
                            continue;
                        }

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

                            if (quartoReservado == null || quartoReservado.getStatus().equals("em manutenção")) {
                                System.out.println("erro: Quarto associado à reserva não encontrado ou em manutenção");
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

                    case 3:
                        System.out.println("Detalhes da Manutenção:");
                        System.out.print("ID do Quarto: ");
                        idQuarto = s.nextInt();
                        System.out.print("Tipo de Manutenção: ");
                        s.nextLine();
                        String tipoManutencao = s.nextLine().trim();

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

                                Manutencao manutencao = new Manutencao(getMaxIdManutencao(quarto.getManutencoes()),
                                        tipoManutencao);
                                quarto.getManutencoes().add(manutencao);
                                
                                  if (!"em manutenção".equals(quarto.getStatus())) {
                                  quarto.setStatus("em manutenção");
                                  }
                                 

                                ArquivoUtil.guardarLista("quartos.json", quartos);
                                System.out.println("manutenção adicionada com sucesso: " + manutencao);
                            }
                        } catch (Exception e) {
                            System.out.println("erro ao adicionar manutenção: " + e.getMessage());
                        }
                        break;

                    case 4:
                        System.out.println("Adicionar Reserva:");
                        System.out.print("ID do Quarto: ");
                        idQuarto = s.nextInt();
                        System.out.print("Data de Entrada (yyyy-MM-dd): ");
                        String entrada1 = s.next();
                        System.out.print("Data de Saída (yyyy-MM-dd): ");
                        String saida1 = s.next();
                        System.out.println("ID cliente:");
                        int idCliente = s.nextInt();
                        try {
                            List<Quarto> quartos = ArquivoUtil.carregarLista("quartos.json",
                                    new TypeReference<List<Quarto>>() {
                                    });
                            Quarto quarto = null;
                            for (Quarto q1 : quartos) {
                                if (q1.getId() == idQuarto) {
                                    quarto = q1;
                                    break;
                                }
                            }

                            if (quarto == null || quarto.getStatus().equals("em manutenção")) {
                                System.out.println("Quarto não encontrado ou em manutenção");
                            } else {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date dataEntrada = sdf.parse(entrada1);
                                Date dataSaida = sdf.parse(saida1);

                                if (!quarto.isDisponivel(dataEntrada, dataSaida) && !quarto.getStatus().equals("em manutenção")) {
                                    System.out.println("erro: Quarto não está disponível para o período solicitado");
                                } else {
                                    Reserva reserva = new Reserva(getMaxIdReserva(quarto.getReservas()), dataEntrada,
                                            dataSaida, "confirmada", idQuarto, idCliente);
                                    quarto.getReservas().add(reserva);
                                    ArquivoUtil.guardarLista("quartos.json", quartos);
                                    System.out.println("Reserva adicionada com sucesso: " + reserva);
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Erro ao adicionar reserva: " + e.getMessage());
                        }
                        break;

                    case 5:
                        System.out.println("Lista de Todos os Quartos:");
                        for (Quarto quarto : quartosTotais) {
                            System.out.println(quarto);
                        }
                        break;

                    case 6:
                        System.out.println("ID do quarto:");
                        int idQ = s.nextInt();
                        System.out.print("ID da Manutenção para Remover: ");
                        int idManutencao = s.nextInt();
                        boolean removido = false;
                        boolean quartoEncontrado = false;
                        Quarto toChange = null;
                        for (Quarto quarto : quartosTotais) {
                            if (quarto.getId() == idQ) {
                                toChange = quarto;
                                quartoEncontrado = true;
                                for (Manutencao m : quarto.getManutencoes()) {
                                    if (m.getIdManutencao() == idManutencao) {
                                        g.removerManutencao(m, quarto.getManutencoes());
                                        System.out.println("Manutenção removida com sucesso");
                                        removido = true;
                                        break;
                                    }
                                }
                                break;
                            }
                        }

                        if (!quartoEncontrado) {
                            System.out.println("quarto não encontrado");
                        } else if (!removido) {
                            System.out.println("manutenção não encontrada");
                        } else {
                            try {
                                toChange.setStatus("disponivel");
                                ArquivoUtil.guardarLista("quartos.json", quartosTotais);

                            } catch (Exception e) {
                                System.out.println("Erro ao guardar as alterações no ficheiro: " + e.getMessage());
                            }
                        }
                        break;
                    case 7:
                        System.out.println("Lista das reservas pendentes : " + reservasPendentes);
                        cancelarReserva(g, s);
                        break;
                    case 8:
                        System.out.println("Digite as características do quarto:");
                        System.out.println("Digite a capacidade do quarto:");
                        int capacidadeQ = s.nextInt();
                        s.nextLine();
                        System.out.println("Digite o número de camas:");
                        int nCamasQ = s.nextInt();
                        s.nextLine();
                        System.out.println("Digite o número de WC's:");
                        int nWCQ = s.nextInt();
                        s.nextLine();
                        System.out.println("Digite o tipo de vista:");
                        String tipoVistaQ = s.nextLine();
                        System.out.println("O quarto tem cozinha? (s/n):");
                        boolean temCozinhaQ = s.nextLine().equalsIgnoreCase("s");
                        Quarto qu = new Quarto(0, capacidadeQ, nCamasQ, nWCQ, tipoVistaQ, "disponivel", temCozinhaQ);
                        try {
                            System.out.print("Data de Entrada (yyyy-MM-dd): ");
                            String entrada2 = s.next();
                            s.nextLine();
                            System.out.print("Data de Saída (yyyy-MM-dd): ");
                            String saida2 = s.next();
                            s.nextLine();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date dataEntrada = sdf.parse(entrada2);
                            Date dataSaida = sdf.parse(saida2);
                            if (!dataEntrada.before(dataSaida)) {
                                System.out.println("A data de entrada deve ser anterior à data de saída");
                                return;
                            }
                            Reserva r = new Reserva(dataEntrada, dataSaida);
                            g.cancelarReservaConfirmada(r, qu, quartosTotais);
                        } catch (ParseException e) {
                            System.out.println(
                                    "Erro ao interpretar as datas, certifique-se de usar o formato yyyy-MM-dd");
                            e.printStackTrace();
                        }
                        break;

                    case 9:
                        System.out.println("ADEUS");
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

    private static void cancelarReserva(GestorHotel g, Scanner scanner) {
        int idReserva = lerNumero(scanner, "Digite o ID da reserva a ser cancelada: ");
        g.cancelarReserva(idReserva);
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
