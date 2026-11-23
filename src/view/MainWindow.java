package src.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import src.model.ConsoleGame;
import src.model.Game;
import src.model.PCGame;
import src.persistence.ArquivoJogos;
import src.service.EstatisticasJogos;
import src.service.GameManager;
import src.view.GameRegistrationWindow.RegisteredGame;

/** Janela principal com os comandos centrais da aplicação. */
public class MainWindow extends JFrame {

    private final GameManager gerenciador;
    private final ArquivoJogos arquivoJogos;
    private final EstatisticasJogos estatisticas;
    private final DefaultListModel<Game> listModel;
    private final JList<Game> gameList;

    public MainWindow() {
        super("Game Library Manager");
        gerenciador = new GameManager();
        arquivoJogos = new ArquivoJogos();
        estatisticas = new EstatisticasJogos();
        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(780, 460);
        setLocationRelativeTo(null);
        buildWindow();
        carregarInicial();
    }

    private void buildWindow() {
        JButton listGamesButton = new JButton("Listar jogos");
        JButton registerGameButton = new JButton("Cadastrar jogo");
        JButton searchButton = new JButton("Pesquisar");
        JButton toggleButton = new JButton("Alternar conclusão");
        JButton statisticsButton = new JButton("Estatísticas");
        JButton saveButton = new JButton("Salvar");

        JPanel buttonsPanel = new JPanel(new GridLayout(6, 1, 8, 8));
        buttonsPanel.add(listGamesButton);
        buttonsPanel.add(registerGameButton);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(toggleButton);
        buttonsPanel.add(statisticsButton);
        buttonsPanel.add(saveButton);

        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Game) {
                    setText(formatar((Game) value));
                }
                return this;
            }
        });

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.add(new JScrollPane(gameList), BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.EAST);
        setContentPane(content);

        listGamesButton.addActionListener(event -> listGames());
        registerGameButton.addActionListener(event -> openRegistration());
        searchButton.addActionListener(event -> searchGames());
        toggleButton.addActionListener(event -> alternarConclusao());
        statisticsButton.addActionListener(event -> showStatistics());
        saveButton.addActionListener(event -> saveGames());
    }

    private void carregarInicial() {
        try {
            for (Game jogo : arquivoJogos.carregar(ArquivoJogos.CAMINHO_PADRAO)) {
                gerenciador.adicionarJogo(jogo);
            }
            updateList(gerenciador.listarJogos());
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Leitura", JOptionPane.WARNING_MESSAGE);
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Leitura", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void listGames() {
        updateList(gerenciador.listarJogos());
        if (gerenciador.listarJogos().isEmpty()) {
            JOptionPane.showMessageDialog(this, "A biblioteca ainda não possui jogos.",
                    "Listar jogos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openRegistration() {
        GameRegistrationWindow registrationWindow = new GameRegistrationWindow(this::addGame);
        registrationWindow.setVisible(true);
    }

    private void addGame(RegisteredGame game) {
        try {
            gerenciador.adicionarJogo(criarJogo(game));
            updateList(gerenciador.listarJogos());
            JOptionPane.showMessageDialog(this, "Jogo cadastrado com sucesso.",
                    "Cadastro", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Cadastro", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Game criarJogo(RegisteredGame game) {
        int id = proximoId();
        if ("PC".equals(game.getType())) {
            return new PCGame(
                    id, game.getTitle(), game.getGenre(),
                    game.getHours(), game.getRating(), false,
                    game.getPlatform(), game.getRequirements()
            );
        }
        return new ConsoleGame(
                id, game.getTitle(), game.getGenre(),
                game.getHours(), game.getRating(), false,
                game.getConsoleName(), game.hasPhysicalMedia()
        );
    }

    private int proximoId() {
        int maior = 0;
        for (Game jogo : gerenciador.listarJogos()) {
            if (jogo.getId() > maior) {
                maior = jogo.getId();
            }
        }
        return maior + 1;
    }

    private void searchGames() {
        String title = JOptionPane.showInputDialog(this, "Digite o título ou parte dele:", "Pesquisar",
                JOptionPane.QUESTION_MESSAGE);
        if (title == null) {
            return;
        }
        title = title.trim();
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe um título para pesquisar.",
                    "Pesquisa", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<Game> results = gerenciador.buscarPorTitulo(title);
            updateList(results);
            if (results.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nenhum jogo encontrado para: " + title,
                        "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Pesquisa", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void alternarConclusao() {
        Game selecionado = gameList.getSelectedValue();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um jogo para alterar a conclusão.",
                    "Alternar conclusão", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean concluido = !selecionado.isZerado();
        try {
            gerenciador.alterarStatus(selecionado.getId(), concluido);
        } catch (IllegalArgumentException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Alternar conclusão", JOptionPane.WARNING_MESSAGE);
            return;
        }

        updateList(gerenciador.listarJogos());
        selecionarPorId(selecionado.getId());
        JOptionPane.showMessageDialog(this,
                selecionado.getTitulo() + (concluido
                        ? " foi marcado como concluído."
                        : " foi marcado como pendente."),
                "Alternar conclusão", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateList(List<Game> items) {
        listModel.clear();
        for (Game jogo : items) {
            listModel.addElement(jogo);
        }
    }

    private void selecionarPorId(int id) {
        for (int i = 0; i < listModel.getSize(); i++) {
            if (listModel.getElementAt(i).getId() == id) {
                gameList.setSelectedIndex(i);
                gameList.ensureIndexIsVisible(i);
                return;
            }
        }
    }

    private void showStatistics() {
        List<Game> jogos = gerenciador.getJogos();
        StatisticsWindow statisticsWindow = new StatisticsWindow(
                estatisticas.totalGeral(jogos),
                estatisticas.totalPC(jogos),
                estatisticas.totalConsole(jogos),
                estatisticas.somaHoras(jogos),
                estatisticas.mediaNotas(jogos));
        statisticsWindow.setVisible(true);
    }

    private void saveGames() {
        try {
            arquivoJogos.salvar(gerenciador.getJogos(), ArquivoJogos.CAMINHO_PADRAO);
            JOptionPane.showMessageDialog(this,
                    "Biblioteca salva em " + ArquivoJogos.CAMINHO_PADRAO + ".",
                    "Salvar", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException erro) {
            JOptionPane.showMessageDialog(this, erro.getMessage(),
                    "Salvar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatar(Game jogo) {
        return jogo.getId() + " | " + jogo.getTitulo()
                + " | " + jogo.getTipo()
                + " | " + jogo.getGenero()
                + " | " + jogo.getHorasJogadas() + " h"
                + " | Nota: " + jogo.getAvaliacao()
                + " | " + (jogo.isZerado() ? "Concluído" : "Pendente");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
