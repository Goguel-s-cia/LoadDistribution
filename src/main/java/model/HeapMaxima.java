package model;

import java.io.FileWriter;
import java.io.IOException;

public class HeapMaxima {
    private Carga[] heap;
    private int quantidade;
    private int capacidade;

    public HeapMaxima(int capacidadeInicial) {
        this.capacidade = Math.max(10, capacidadeInicial);
        this.heap = new Carga[this.capacidade];
        this.quantidade = 0;
    }

    public void inserir(Carga novaCarga) {
        garantirCapacidade();
        this.heap[this.quantidade] = novaCarga;
        subir(this.quantidade);
        this.quantidade++;
    }

    public Carga removerMaximo() {
        if (this.quantidade == 0) return null;

        Carga raiz = this.heap[0];
        
        // Pega o último elemento e coloca na raiz
        this.heap[0] = this.heap[this.quantidade - 1];
        this.heap[this.quantidade - 1] = null; // Limpa referência
        this.quantidade--;

        if (this.quantidade > 0) {
            descer(0);
        }

        return raiz;
    }

    public Carga consultarTopo() {
        if (this.quantidade == 0) return null;
        return this.heap[0];
    }
    
    public int tamanho() {
        return this.quantidade;
    }
    
    public boolean estaVazia() {
        return this.quantidade == 0;
    }

    // --- MOVIMENTAÇÃO DO HEAP (0-INDEXADO) ---

    private void subir(int i) {
        int pai = (i - 1) / 2;
        // Enquanto não for a raiz E o filho for "maior" que o pai
        while (i > 0 && ehMaisPrioritario(i, pai)) {
            trocar(i, pai);
            i = pai;
            pai = (i - 1) / 2;
        }
    }

    private void descer(int i) {
        int maior = i;
        int esquerdo = 2 * i + 1;
        int direito = 2 * i + 2;

        if (esquerdo < quantidade && ehMaisPrioritario(esquerdo, maior)) {
            maior = esquerdo;
        }
        if (direito < quantidade && ehMaisPrioritario(direito, maior)) {
            maior = direito;
        }

        if (maior != i) {
            trocar(i, maior);
            descer(maior);
        }
    }

    // --- CRITÉRIOS DE DESEMPATE  ---
    private boolean ehMaisPrioritario(int indiceA, int indiceB) {
        Carga a = heap[indiceA];
        Carga b = heap[indiceB];

        // 1. Maior Prioridade calculada
        if (a.getPrioridade() > b.getPrioridade()) return true;
        if (a.getPrioridade() < b.getPrioridade()) return false;

        // 2. Maior Urgência
        if (a.getUrgencia() > b.getUrgencia()) return true;
        if (a.getUrgencia() < b.getUrgencia()) return false;

        // 3. Maior Peso
        if (a.getPeso() > b.getPeso()) return true;
        if (a.getPeso() < b.getPeso()) return false;

        // 4. Menor ID (Chegou primeiro/cadastro mais antigo)
        return a.getId() < b.getId();
    }

    private void trocar(int i, int j) {
        Carga temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    private void garantirCapacidade() {
        if (this.quantidade >= this.capacidade) {
            this.capacidade *= 2;
            Carga[] novoHeap = new Carga[this.capacidade];
            // Cópia manual
            for (int i = 0; i < this.quantidade; i++) {
                novoHeap[i] = this.heap[i];
            }
            this.heap = novoHeap;
        }
    }
    
    // Copia o heap atual para permitir simulações sem destruir o original
    public HeapMaxima copiar() {
        HeapMaxima copia = new HeapMaxima(this.capacidade);
        for (int i = 0; i < this.quantidade; i++) {
            copia.heap[i] = this.heap[i];
        }
        copia.quantidade = this.quantidade;
        return copia;
    }
    
    public void gerarArquivoDOT(String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write("digraph HeapCargas {\n");
            writer.write("  node [shape=record, style=filled, color=\"lightgrey\"];\n");
            
            for (int i = 0; i < quantidade; i++) {
                Carga c = heap[i];
                // Label com detalhes: ID, Prioridade, Nome
                String label = String.format("{ID: %d | Pri: %d | %s}", c.getId(), c.getPrioridade(), c.getDescricao());
                writer.write(String.format("  %d [label=\"%s\"];\n", i, label));
                
                // Conectar com filho esquerdo
                int esq = 2 * i + 1;
                if (esq < quantidade) {
                    writer.write(String.format("  %d -> %d;\n", i, esq));
                }
                
                // Conectar com filho direito
                int dir = 2 * i + 2;
                if (dir < quantidade) {
                    writer.write(String.format("  %d -> %d;\n", i, dir));
                }
            }
            
            writer.write("}\n");
            System.out.println("Arquivo DOT gerado: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar DOT: " + e.getMessage());
        }
    }
}