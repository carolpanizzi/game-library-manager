package src.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import src.view.GameRegistrationWindow.RegisteredGame;

/** Janela principal com os comandos centrais da aplicação. */
public class MainWindow extends JFrame {

    private final List<RegisteredGame> games;
    private final DefaultListModel<RegisteredGame> listModel;
    private final JList<RegisteredGame> gameList;

    public MainWindow() {
        super("Game Library Manager");
        games = new ArrayList<>();
        listModel = new DefaultListModel<>();
        gameList = new JList<>(listModel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(620, 380);
        setLocationRelativeTo(null);
        buildWindow();
    }

    private void buildWindow() {
        JButton listGamesButton = new JButton("Listar jogos");
        JButton registerGameButton = new JButton("Cadastrar jogo");
        JButton searchButton = new JButton("Pesquisar");
        JButton statisticsButton = new JButton("Estatísticas");
        JButton saveButton = new JButton("Salvar");

        JPanel buttonsPanel = new JPanel(new GridLayout(5, 1, 8, 8));
        buttonsPanel.add(listGamesButton);
        buttonsPanel.add(registerGameButton);
        buttonsPanel.add(searchButton);
        buttonsPanel.add(statisticsButton);
        buttonsPanel.add(saveButton);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.add(new JScrollPane(gameList), BorderLayout.CENTER);
        content.add(buttonsPanel, BorderLayout.EAST);
        setContentPane(content);

        listGamesButton.addActionListener(event -> listGames());
        registerGameButton.addActionListener(event -> openRegistration());
        searchButton.addActionListener(event -> searchGames());
        statisticsButton.addActionListener(event -> showStatistics());
        saveButton.addActionListener(event -> saveGames());
    }

    private void listGames() {
        updateList(games);
        if (games.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A biblioteca ainda não possui jogos.",
                    "Listar jogos", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openRegistration() {
        GameRegistrationWindow registrationWindow = new GameRegistrationWindow(this::addGame);
        registrationWindow.setVisible(true);
    }

    private void addGame(RegisteredGame game) {
        games.add(game);
        listModel.addElement(game);
        JOptionPane.showMessageDialog(this, "Jogo cadastrado com sucesso.",
                "Cadastro", JOptionPane.INFORMATION_MESSAGE);
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

        List<RegisteredGame> results = new ArrayList<>();
        for (RegisteredGame game : games) {
            if (game.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(game);
            }
        }
        updateList(results);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum jogo encontrado para: " + title,
                    "Pesquisa", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateList(List<RegisteredGame> items) {
        listModel.clear();
        for (RegisteredGame game : items) {
            listModel.addElement(game);
        }
    }

    private void showStatistics() {
        int pcGames = 0;
        int consoleGames = 0;
        double totalHours = 0;
        double totalRating = 0;
        for (RegisteredGame game : games) {
            if ("PC".equals(game.getType())) {
                pcGames++;
            } else if ("Console".equals(game.getType())) {
                consoleGames++;
            }
            totalHours += game.getHours();
            totalRating += game.getRating();
        }
        double averageRating = games.isEmpty() ? 0 : totalRating / games.size();
        StatisticsWindow statisticsWindow = new StatisticsWindow(
                games.size(), pcGames, consoleGames, totalHours, averageRating);
        statisticsWindow.setVisible(true);
    }

    private void saveGames() {
        JOptionPane.showMessageDialog(this,
                "O salvamento em arquivo será conectado quando a persistência estiver pronta.",
                "Salvar", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
