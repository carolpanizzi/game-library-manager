package src.service;
import java.util.List;
import src.model.Game;

/**
 * Calcula as estatísticas da biblioteca a partir da coleção de jogos.
 * Os métodos recebem a lista para que a interface use a mesma coleção
 * gerenciada pela aplicação, sem recalcular os valores nas janelas.
 */
public class EstatisticasJogos {

    public int totalGeral(List<Game> jogos) {
        validarLista(jogos);
        return jogos.size();
    }

    public int totalPC(List<Game> jogos) {
        validarLista(jogos);
        int total = 0;
        for (Game jogo : jogos) {
            if ("PC".equals(jogo.getTipo())) {
                total++;
            }
        }
        return total;
    }

    public int totalConsole(List<Game> jogos) {
        validarLista(jogos);
        int total = 0;
        for (Game jogo : jogos) {
            if ("Console".equals(jogo.getTipo())) {
                total++;
            }
        }
        return total;
    }

    public double somaHoras(List<Game> jogos) {
        validarLista(jogos);
        double soma = 0;
        for (Game jogo : jogos) {
            soma += jogo.getHorasJogadas();
        }
        return soma;
    }

    public double mediaNotas(List<Game> jogos) {
        validarLista(jogos);
        if (jogos.isEmpty()) {
            return 0;
        }

        double soma = 0;
        for (Game jogo : jogos) {
            soma += jogo.getAvaliacao();
        }
        return soma / jogos.size();
    }

    private void validarLista(List<Game> jogos) {
        if (jogos == null) {
            throw new IllegalArgumentException(
                "A coleção de jogos não pode ser nula."
            );
        }
        for (int i = 0; i < jogos.size(); i++) {
            if (jogos.get(i) == null) {
                throw new IllegalArgumentException(
                    "A coleção contém um jogo nulo na posição " + i + "."
                );
            }
        }
    }
}
