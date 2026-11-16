package src.view;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Janela principal com os comandos centrais da aplicação. */
public class MainWindow extends JFrame {

    public MainWindow() {
        super("Game Library Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(360, 300);
        setLocationRelativeTo(null);

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
        setContentPane(buttonsPanel);

        listGamesButton.addActionListener(event -> showNotImplementedMessage("Listar jogos"));
        registerGameButton.addActionListener(event -> showNotImplementedMessage("Cadastrar jogo"));
        searchButton.addActionListener(event -> showNotImplementedMessage("Pesquisar"));
        statisticsButton.addActionListener(event -> showNotImplementedMessage("Estatísticas"));
        saveButton.addActionListener(event -> showNotImplementedMessage("Salvar"));
    }

    private void showNotImplementedMessage(String action) {
        JOptionPane.showMessageDialog(this,
                "A opção \"" + action + "\" será conectada nas próximas etapas.",
                action, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}
