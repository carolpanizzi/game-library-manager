package src.service;
import java.util.ArrayList;
import java.util.List;
import src.model.Game;

public class GameManager {

    private ArrayList<Game> jogos;
   
    public GameManager() {
        this.jogos = new ArrayList<>();
    }

    public void adicionarJogo(Game jogo) {
        if (jogo == null) {
            throw new IllegalArgumentException(
                "O jogo não pode ser nulo."
            );
        }

        if (buscarPorId(jogo.getId()) != null) {
            throw new IllegalArgumentException(
                "Já existe um jogo com esse ID."
            );
        }

        jogos.add(jogo);
    }

    public List<Game> listarJogos() {
        return new ArrayList<>(jogos);
    }

    public List<Game> buscarPorTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "O título da busca não pode ser vazio."
            );
        }

        List<Game> encontrados = new ArrayList<>();

        for (Game jogo : jogos) {
            if (jogo.getTitulo().toLowerCase()
                    .contains(titulo.trim().toLowerCase())) {
                encontrados.add(jogo);
            }
        }

        return encontrados;
    }

    public Game buscarPorId(int id) {
        for (Game jogo : jogos) {
            if (jogo.getId() == id) {
                return jogo;
            }
        }

        return null;
    }

    public void alterarStatus(int id, boolean zerado) {
        Game jogo = buscarPorId(id);

        if (jogo == null) {
            throw new IllegalArgumentException(
                "Não existe jogo com esse ID."
            );
        }

        jogo.setZerado(zerado);
    }

    public List<Game> getJogos() {
        return listarJogos();
    }
}
