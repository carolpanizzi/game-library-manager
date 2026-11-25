package src.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Formulário para informar os dados comuns e específicos de um jogo. */
public class GameRegistrationWindow extends JFrame {

    private final JTextField titleField;
    private final JTextField genreField;
    private final JTextField hoursField;
    private final JTextField ratingField;
    private final JComboBox<String> typeCombo;
    private final CardLayout specificCards;
    private final JPanel specificFields;
    private final JTextField pcPlatformField;
    private final JTextField pcRequirementsField;
    private final JTextField consoleNameField;
    private final JComboBox<String> physicalMediaCombo;
    private final RegistrationListener registrationListener;

    public GameRegistrationWindow() {
        this(null);
    }

    public GameRegistrationWindow(RegistrationListener registrationListener) {
        super("Cadastrar jogo");
        this.registrationListener = registrationListener;
        titleField = new JTextField(22);
        genreField = new JTextField(22);
        hoursField = new JTextField(22);
        ratingField = new JTextField(22);
        typeCombo = new JComboBox<>(new String[]{"PC", "Console"});
        specificCards = new CardLayout();
        specificFields = new JPanel(specificCards);
        pcPlatformField = new JTextField(22);
        pcRequirementsField = new JTextField(22);
        consoleNameField = new JTextField(22);
        physicalMediaCombo = new JComboBox<>(new String[]{"Não", "Sim"});

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        buildWindow();
        pack();
        setLocationRelativeTo(null);
    }

    private void buildWindow() {
        JPanel fields = new JPanel(new GridBagLayout());
        fields.setBorder(BorderFactory.createEmptyBorder(14, 14, 8, 14));
        addRow(fields, 0, "Título:", titleField);
        addRow(fields, 1, "Gênero:", genreField);
        addRow(fields, 2, "Horas jogadas:", hoursField);
        addRow(fields, 3, "Nota (0 a 10):", ratingField);
        addRow(fields, 4, "Tipo:", typeCombo);

        JPanel pcFields = new JPanel(new GridBagLayout());
        addRow(pcFields, 0, "Plataforma PC:", pcPlatformField);
        addRow(pcFields, 1, "Requisitos mínimos:", pcRequirementsField);

        JPanel consoleFields = new JPanel(new GridBagLayout());
        addRow(consoleFields, 0, "Nome do console:", consoleNameField);
        addRow(consoleFields, 1, "Mídia física:", physicalMediaCombo);

        specificFields.add(pcFields, "PC");
        specificFields.add(consoleFields, "Console");
        addRow(fields, 5, "Dados específicos:", specificFields);
        typeCombo.addActionListener(event -> specificCards.show(
                specificFields, (String) typeCombo.getSelectedItem()));

        JButton registerButton = new JButton("Cadastrar");
        JButton cancelButton = new JButton("Cancelar");
        registerButton.addActionListener(event -> confirmRegistration());
        cancelButton.addActionListener(event -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setBorder(BorderFactory.createEmptyBorder(0, 8, 10, 12));
        buttons.add(cancelButton);
        buttons.add(registerButton);

        JPanel content = new JPanel(new BorderLayout());
        content.add(fields, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        setContentPane(content);
        getRootPane().setDefaultButton(registerButton);
    }

    private void addRow(JPanel panel, int row, String label, java.awt.Component field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(5, 5, 5, 10);
        panel.add(new JLabel(label), labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(5, 0, 5, 5);
        panel.add(field, fieldConstraints);
    }

    private void confirmRegistration() {
        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String type = (String) typeCombo.getSelectedItem();

        if (title.isEmpty() || genre.isEmpty()) {
            showValidationError("Preencha o título e o gênero.");
            return;
        }
        if (containsDelimiter(title) || containsDelimiter(genre)) {
            showValidationError("Título e gênero não podem conter ponto e vírgula.");
            return;
        }

        double hours;
        double rating;
        try {
            hours = Double.parseDouble(hoursField.getText().trim().replace(',', '.'));
            rating = Double.parseDouble(ratingField.getText().trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            showValidationError("Informe horas e nota usando valores numéricos.");
            return;
        }
        if (Double.isNaN(hours) || Double.isInfinite(hours) || hours < 0) {
            showValidationError("As horas jogadas devem ser um número não negativo.");
            return;
        }
        if (Double.isNaN(rating) || Double.isInfinite(rating) || rating < 0 || rating > 10) {
            showValidationError("A nota deve estar entre 0 e 10.");
            return;
        }

        String specificSummary;
        String platform = "";
        String requirements = "";
        String consoleName = "";
        boolean physicalMedia = false;
        if ("PC".equals(type)) {
            platform = pcPlatformField.getText().trim();
            requirements = pcRequirementsField.getText().trim();
            if (platform.isEmpty() || requirements.isEmpty()) {
                showValidationError("Preencha a plataforma e os requisitos mínimos do jogo de PC.");
                return;
            }
            if (containsDelimiter(platform) || containsDelimiter(requirements)) {
                showValidationError("Os campos específicos não podem conter ponto e vírgula.");
                return;
            }
            specificSummary = "Plataforma: " + platform + "\nRequisitos mínimos: " + requirements;
        } else {
            consoleName = consoleNameField.getText().trim();
            if (consoleName.isEmpty()) {
                showValidationError("Informe o nome do console.");
                return;
            }
            if (containsDelimiter(consoleName)) {
                showValidationError("O nome do console não pode conter ponto e vírgula.");
                return;
            }
            physicalMedia = "Sim".equals(physicalMediaCombo.getSelectedItem());
            specificSummary = "Console: " + consoleName + "\nMídia física: "
                    + physicalMediaCombo.getSelectedItem();
        }

        String summary = "Título: " + title
                + "\nGênero: " + genre
                + "\nHoras jogadas: " + hours
                + "\nNota: " + rating
                + "\nTipo: " + type
                + "\n" + specificSummary;
        int answer = JOptionPane.showConfirmDialog(this,
                summary + "\n\nConfirma o cadastro deste jogo?",
                "Confirmar cadastro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (answer == JOptionPane.YES_OPTION) {
            RegisteredGame registeredGame = new RegisteredGame(title, genre, hours, rating,
                    type, platform, requirements, consoleName, physicalMedia);
            if (registrationListener != null) {
                registrationListener.onRegistration(registeredGame);
            }
            dispose();
        }
    }

    private boolean containsDelimiter(String value) {
        return value.indexOf(';') >= 0;
    }

    private void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Dados inválidos", JOptionPane.WARNING_MESSAGE);
    }

    public static void open() {
        SwingUtilities.invokeLater(() -> new GameRegistrationWindow().setVisible(true));
    }

    public interface RegistrationListener {
        void onRegistration(RegisteredGame game);
    }

    /** Dados validados do formulário, independentes das classes de domínio. */
    public static class RegisteredGame {
        private final String title;
        private final String genre;
        private final double hours;
        private final double rating;
        private final String type;
        private final String platform;
        private final String requirements;
        private final String consoleName;
        private final boolean physicalMedia;

        public RegisteredGame(String title, String genre, double hours, double rating,
                              String type, String platform, String requirements,
                              String consoleName, boolean physicalMedia) {
            this.title = title;
            this.genre = genre;
            this.hours = hours;
            this.rating = rating;
            this.type = type;
            this.platform = platform;
            this.requirements = requirements;
            this.consoleName = consoleName;
            this.physicalMedia = physicalMedia;
        }

        public String getTitle() { return title; }
        public String getGenre() { return genre; }
        public double getHours() { return hours; }
        public double getRating() { return rating; }
        public String getType() { return type; }
        public String getPlatform() { return platform; }
        public String getRequirements() { return requirements; }
        public String getConsoleName() { return consoleName; }
        public boolean hasPhysicalMedia() { return physicalMedia; }

        @Override
        public String toString() {
            return title + " | " + type + " | " + genre + " | " + hours + " h | Nota: " + rating;
        }
    }
}
