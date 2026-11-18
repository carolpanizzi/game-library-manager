
package src.service;

import java.util.ArrayList;
import java.util.List;

import src.model.Game;

public class GameManager {

    private ArrayList<Game> jogos;

    // Construtor
    public GameManager() {
        this.jogos = new ArrayList<>();
    }

    // Adicionar jogo
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

    // Listar todos os jogos
    public List<Game> listarJogos() {
        return new ArrayList<>(jogos);
    }

    // Buscar jogo pelo título
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

    // Buscar jogo pelo ID
    public Game buscarPorId(int id) {
        for (Game jogo : jogos) {
            if (jogo.getId() == id) {
                return jogo;
            }
        }

        return null;
    }

    // Alterar status de conclusão
    public void alterarStatus(int id, boolean zerado) {
        Game jogo = buscarPorId(id);

        if (jogo == null) {
            throw new IllegalArgumentException(
                "Não existe jogo com esse ID."
            );
        }

        jogo.setZerado(zerado);
    }

    // Expor os jogos cadastrados
    public List<Game> getJogos() {
        return listarJogos();
    }
}
