package src.model;

public class PCGame extends Game {

    private String plataformaPC;
    private String requisitosMinimos;

    public PCGame(int id, String titulo, String genero,
                  double horasJogadas, double avaliacao, boolean zerado,
                  String plataformaPC, String requisitosMinimos) {

        super(id, titulo, genero, horasJogadas, avaliacao, zerado);

        setPlataformaPC(plataformaPC);
        setRequisitosMinimos(requisitosMinimos);
    }

    public String getPlataformaPC() {
        return plataformaPC;
    }

    public void setPlataformaPC(String plataformaPC) {
        if (plataformaPC == null || plataformaPC.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "A plataforma do PC não pode ser vazia."
            );
        }
        this.plataformaPC = plataformaPC.trim();
    }

    public String getRequisitosMinimos() {
        return requisitosMinimos;
    }

    public void setRequisitosMinimos(String requisitosMinimos) {
        if (requisitosMinimos == null || requisitosMinimos.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Os requisitos mínimos não podem ser vazios."
            );
        }
        this.requisitosMinimos = requisitosMinimos.trim();
    }

    @Override
    public String getTipo() {
        return "PC";
    }

    @Override
    public String resumo() {
        return "ID: " + getId()
                + " | Título: " + getTitulo()
                + " | Gênero: " + getGenero()
                + " | Plataforma: " + plataformaPC
                + " | Horas jogadas: " + getHorasJogadas()
                + " | Avaliação: " + getAvaliacao()
                + " | Zerado: " + (isZerado() ? "Sim" : "Não")
                + " | Requisitos mínimos: " + requisitosMinimos;
    }
}
