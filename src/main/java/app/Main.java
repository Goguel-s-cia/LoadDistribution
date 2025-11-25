package app;

import model.Carga;
import model.HeapMaxima;
import util.LeitorCSV;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        HeapMaxima heap = new HeapMaxima(20);
        LeitorCSV leitor = new LeitorCSV();
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 6) {
            System.out.println("\n=== SISTEMA DE DISTRIBUIÇÃO DE CARGAS (HEAP) ===");
            System.out.println("1. Carregar cargas de arquivo CSV");
            System.out.println("2. Inserir nova carga manualmente");
            System.out.println("3. Exibir carga de maior prioridade (Topo)");
            System.out.println("4. Remover carga de maior prioridade");
            System.out.println("5. Exibir todas as cargas (Ordenadas)");
            System.out.println("6. Sair");
            System.out.println("7. [DEBUG] Gerar Visualização da Árvore (.dot)");
            System.out.print("Escolha: ");

            try {
                String entrada = scanner.nextLine();
                opcao = Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                opcao = 0;
            }

            switch (opcao) {
                case 1:
                    System.out.print("Nome do arquivo (ex: cargas.csv): ");
                    String arquivo = scanner.nextLine();
                    leitor.carregarCargas(arquivo, heap);
                    break;

                case 2:
                    inserirManual(scanner, heap);
                    break;

                case 3:
                    Carga topo = heap.consultarTopo();
                    if (topo != null) {
                        System.out.println("--- TOPO (MAIOR PRIORIDADE) ---");
                        System.out.println(topo);
                    } else {
                        System.out.println("Heap vazio.");
                    }
                    break;

                case 4:
                    Carga removida = heap.removerMaximo();
                    if (removida != null) {
                        System.out.println("--- CARGA REMOVIDA ---");
                        System.out.println(removida);
                        System.out.println("Nova prioridade máxima no topo: " + 
                            (heap.consultarTopo() != null ? heap.consultarTopo().getPrioridade() : "N/A"));
                    } else {
                        System.out.println("Heap vazio.");
                    }
                    break;

                case 5:
                    listarTodasOrdenadas(heap);
                    break;

                case 6:
                    System.out.println("Saindo...");
                    break;
                    
                case 7:
                    heap.gerarArquivoDOT("heap.dot");
                    break;

                default:
                    System.out.println("Opção inválida.");
            }
        }
        scanner.close();
    }

    private static void inserirManual(Scanner sc, HeapMaxima heap) {
        try {
            System.out.print("ID: ");
            int id = Integer.parseInt(sc.nextLine());
            
            System.out.print("Tipo (9-Medicamento, 5-Eletro, 3-Roupa): ");
            int tipo = Integer.parseInt(sc.nextLine());
            
            System.out.print("Urgência (1-3): ");
            int urgencia = Integer.parseInt(sc.nextLine());
            
            System.out.print("Peso (kg): ");
            int peso = Integer.parseInt(sc.nextLine());
            
            System.out.print("Descrição: ");
            String descricao = sc.nextLine();

            Carga carga = new Carga(id, tipo, urgencia, peso, descricao);
            heap.inserir(carga);
            System.out.println("Carga inserida! Prioridade calculada: " + carga.getPrioridade());

        } catch (Exception e) {
            System.out.println("Erro ao inserir: " + e.getMessage());
        }
    }

    private static void listarTodasOrdenadas(HeapMaxima heapOriginal) {
        if (heapOriginal.estaVazia()) {
            System.out.println("Heap vazio.");
            return;
        }
        
        System.out.println("\n--- LISTA DE CARGAS (PRIORIDADE DECRESCENTE) ---");
        System.out.printf("%-5s | %-4s | %-8s | %-5s | %-10s | %s\n", "ID", "TIPO", "URGENCIA", "PESO", "PRIORIDADE", "DESCRICAO");
        
        // Copiamos o heap para não destruir o original ao remover
        HeapMaxima copia = heapOriginal.copiar();
        
        while (!copia.estaVazia()) {
            Carga c = copia.removerMaximo();
            System.out.println(c.toTableString());
        }
    }
}