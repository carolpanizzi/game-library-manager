package src.model;

public class ConsoleGame extends Game {

    private String nomeConsole;
    private boolean midiaFisica;

    public ConsoleGame(int id, String titulo, String genero,
                       double horasJogadas, double avaliacao, boolean zerado,
                       String nomeConsole, boolean midiaFisica) {

        super(id, titulo, genero, horasJogadas, avaliacao, zerado);

        setNomeConsole(nomeConsole);
        this.midiaFisica = midiaFisica;
    }

    public String getNomeConsole() {
        return nomeConsole;
    }

    public void setNomeConsole(String nomeConsole) {
        if (nomeConsole == null || nomeConsole.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O nome do console não pode ser vazio."
            );
        }
        this.nomeConsole = nomeConsole.trim();
    }

    public boolean isMidiaFisica() {
        return midiaFisica;
    }

    public void setMidiaFisica(boolean midiaFisica) {
        this.midiaFisica = midiaFisica;
    }

    @Override
    public String getTipo() {
        return "Console";
    }

    @Override
    public String resumo() {
        return "ID: " + getId()
                + " | Título: " + getTitulo()
                + " | Gênero: " + getGenero()
                + " | Console: " + nomeConsole
                + " | Horas jogadas: " + getHorasJogadas()
                + " | Avaliação: " + getAvaliacao()
                + " | Zerado: " + (isZerado() ? "Sim" : "Não")
                + " | Mídia física: " + (midiaFisica ? "Sim" : "Não");
    }
}
