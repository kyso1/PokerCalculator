import javax.swing.*;
import java.awt.*;

public class LoadingScreen {
    private JFrame loadingFrame;
    private JProgressBar progressBar;
    private JLabel loadingLabel;
    private JLabel spinnerLabel;
    private Timer spinnerTimer;
    private int spinnerAngle = 0;

    public LoadingScreen() {
        // Configurar a janela de loading
        loadingFrame = new JFrame("Carregando...");
        loadingFrame.setSize(400, 150); // Aumentado para caber o texto e a barra
        loadingFrame.setLocationRelativeTo(null); // Centraliza na tela
        loadingFrame.setUndecorated(true); // Remove bordas e barra de título
        loadingFrame.setLayout(new BorderLayout());

        // Painel para o texto e a barra de progresso
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Configurar o texto "Loading, please wait..."
        loadingLabel = new JLabel("Loading, please wait...", JLabel.CENTER);
        loadingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        loadingPanel.add(loadingLabel, BorderLayout.NORTH);

        // Configurar a barra de progresso
        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(350,1)); // Barra ainda mais fina (2px de altura)
        progressBar.setForeground(Color.GREEN); // Cor verde
        progressBar.setStringPainted(false); // Sem texto
        progressBar.setBorderPainted(false); // Sem borda
        loadingPanel.add(progressBar, BorderLayout.CENTER);

        // Configurar o spinner (bolinha rodando)
        spinnerLabel = new JLabel("⬤");
        spinnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingPanel.add(spinnerLabel, BorderLayout.SOUTH);

        // Adicionar o painel à janela
        loadingFrame.add(loadingPanel);

        // Criar animação do spinner (rotate ⬤)
        spinnerTimer = new Timer(100, e -> {
            spinnerAngle += 30; // Ângulo de rotação
            spinnerLabel.setText(getSpinnerText());
        });
    }

    // Método para criar o efeito do spinner (bolinha rodando usando texto)
    private String getSpinnerText() {
        char[] spinnerChars = {'⬤', '◐', '◑', '◒', '◓'};
        return String.valueOf(spinnerChars[spinnerAngle / 30 % spinnerChars.length]);
    }

    // Método para mostrar a tela de loading
    public void showLoadingScreen() {
        loadingFrame.setVisible(true);
        spinnerTimer.start(); // Iniciar o spinner
    }

    // Método para ocultar a tela de loading
    public void hideLoadingScreen() {
        spinnerTimer.stop(); // Parar o spinner
        loadingFrame.setVisible(false);
        loadingFrame.dispose();
    }

    // Método para atualizar o progresso
    public void updateProgress(int value) {
        progressBar.setValue(value);
    }

    public static void main(String[] args) {
        LoadingScreen loadingScreen = new LoadingScreen();
        loadingScreen.showLoadingScreen();

        // Simulação de carregamento e atualização da barra
        for (int i = 0; i <= 100; i += 10) {
            try {
                Thread.sleep(500); // Simula o tempo de carregamento
                loadingScreen.updateProgress(i); // Atualiza a barra
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        loadingScreen.hideLoadingScreen();
    }
}
