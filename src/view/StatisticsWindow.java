package src.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class StatisticsWindow extends JFrame {

    private final JLabel totalGamesValue;
    private final JLabel pcGamesValue;
    private final JLabel consoleGamesValue;
    private final JLabel totalHoursValue;
    private final JLabel averageRatingValue;

    public StatisticsWindow(int totalGames, int pcGames, int consoleGames,
                            double totalHours, double averageRating) {
        super("Estatísticas dos jogos");
        totalGamesValue = createValueLabel(String.valueOf(totalGames));
        pcGamesValue = createValueLabel(String.valueOf(pcGames));
        consoleGamesValue = createValueLabel(String.valueOf(consoleGames));
        totalHoursValue = createValueLabel(String.format("%.2f", totalHours));
        averageRatingValue = createValueLabel(String.format("%.2f", averageRating));

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        buildWindow();
        pack();
        setLocationRelativeTo(null);
    }

    private void buildWindow() {
        JPanel statisticsPanel = new JPanel(new GridLayout(5, 2, 12, 10));
        statisticsPanel.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));
        statisticsPanel.add(new JLabel("Total geral de jogos:"));
        statisticsPanel.add(totalGamesValue);
        statisticsPanel.add(new JLabel("Total de jogos de PC:"));
        statisticsPanel.add(pcGamesValue);
        statisticsPanel.add(new JLabel("Total de jogos de console:"));
        statisticsPanel.add(consoleGamesValue);
        statisticsPanel.add(new JLabel("Soma das horas jogadas:"));
        statisticsPanel.add(totalHoursValue);
        statisticsPanel.add(new JLabel("Média das notas:"));
        statisticsPanel.add(averageRatingValue);

        JButton closeButton = new JButton("Fechar");
        closeButton.addActionListener(event -> dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());
        add(statisticsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createValueLabel(String value) {
        return new JLabel(value);
    }
    
    public static void open(int totalGames, int pcGames, int consoleGames,
                            double totalHours, double averageRating) {
        SwingUtilities.invokeLater(() -> new StatisticsWindow(
                totalGames, pcGames, consoleGames, totalHours, averageRating).setVisible(true));
    }
}
