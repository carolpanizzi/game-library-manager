
package src.model;

public abstract class Game {

    private int id;
    private String titulo;
    private String genero;
    private double horasJogadas;
    private double avaliacao;
    private boolean zerado;

    public Game(int id, String titulo, String genero,
                double horasJogadas, double avaliacao, boolean zerado) {

        setId(id);
        setTitulo(titulo);
        setGenero(genero);
        setHorasJogadas(horasJogadas);
        setAvaliacao(avaliacao);
        this.zerado = zerado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("O ID deve ser maior que zero.");
        }
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("O título não pode ser vazio.");
        }
        this.titulo = titulo.trim();
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("O gênero não pode ser vazio.");
        }
        this.genero = genero.trim();
    }

    public double getHorasJogadas() {
        return horasJogadas;
    }

    public void setHorasJogadas(double horasJogadas) {
        if (!Double.isFinite(horasJogadas) || horasJogadas < 0) {
            throw new IllegalArgumentException(
                "As horas jogadas devem ser um número válido e não negativo."
            );
        }
        this.horasJogadas = horasJogadas;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        if (!Double.isFinite(avaliacao) || avaliacao < 0 || avaliacao > 10) {
            throw new IllegalArgumentException("A nota deve estar entre 0 e 10.");
        }
        this.avaliacao = avaliacao;
    }

    public boolean isZerado() {
        return zerado;
    }

    public void setZerado(boolean zerado) {
        this.zerado = zerado;
    }

    public abstract String getTipo();

    public abstract String resumo();
}
