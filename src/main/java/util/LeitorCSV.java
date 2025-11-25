package util;

import model.Carga;
import model.HeapMaxima;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LeitorCSV {

    public void carregarCargas(String caminhoArquivo, HeapMaxima heap) {
        System.out.println("--- Carregando arquivo: " + caminhoArquivo + " ---");
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int numLinha = 0;

            while ((linha = br.readLine()) != null) {
                numLinha++;
                String[] partes = linha.split(",");

                // Validação básica de formato
                if (partes.length < 5) continue;

                try {
                    // Tenta identificar cabeçalho verificando se a primeira coluna é número
                    try {
                        Integer.parseInt(partes[0].trim());
                    } catch (NumberFormatException e) {
                        if (numLinha == 1) continue; // Pula cabeçalho
                    }

                    int id = Integer.parseInt(partes[0].trim());
                    int tipo = Integer.parseInt(partes[1].trim());
                    int urgencia = Integer.parseInt(partes[2].trim());
                    int peso = Integer.parseInt(partes[3].trim());
                    String descricao = partes[4].trim();

                    Carga carga = new Carga(id, tipo, urgencia, peso, descricao);
                    heap.inserir(carga);
                    
                } catch (IllegalArgumentException e) {
                    System.err.println("Erro na linha " + numLinha + ": " + e.getMessage());
                }
            }
            System.out.println("Cargas carregadas. Total no Heap: " + heap.tamanho());
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }
}