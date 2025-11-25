package model;

public class Carga {
    private int id;
    private int tipo;      
    private int urgencia;  
    private int peso;
    private String descricao;
    private int prioridade;

    public Carga(int id, int tipo, int urgencia, int peso, String descricao) throws IllegalArgumentException {
        this.id = id;
        this.tipo = tipo;
        this.urgencia = urgencia;
        this.peso = peso;
        this.descricao = descricao;

        validar(); // Valida ao construir
        calcularPrioridade();
    }

    private void validar() {
        if (this.urgencia < 1 || this.urgencia > 3) {
            throw new IllegalArgumentException("Urgência deve ser 1, 2 ou 3.");
        }
        if (this.peso <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero.");
        }
    }

    private void calcularPrioridade() {
        // Fórmula: (Urgência * 10) + (Peso * 2) + (Tipo * 5)
        this.prioridade = (this.urgencia * 10) + (this.peso * 2) + (this.tipo * 5);
    }

    // Getters
    public int getId() { return id; }
    public int getTipo() { return tipo; }
    public int getUrgencia() { return urgencia; }
    public int getPeso() { return peso; }
    public int getPrioridade() { return prioridade; }
    public String getDescricao() { return descricao; }

    @Override
    public String toString() {
        return String.format("%d | Tipo:%d | Urg:%d | Peso:%d | PRI:%d | %s",
                id, tipo, urgencia, peso, prioridade, descricao);
    }
    
    // Formato tabulado para exibição em tabela
    public String toTableString() {
        return String.format("%-5d | %-4d | %-8d | %-5d | %-10d | %s",
                id, tipo, urgencia, peso, prioridade, descricao);
    }
}