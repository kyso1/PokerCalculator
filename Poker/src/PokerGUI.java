import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class PokerGUI extends JFrame {

    private JPanel playerHandDisplayPanel;
    private JPanel tableCardsDisplayPanel;
    private JLabel[] handCardLabels; // Labels para as cartas da mão
    private JLabel[] tableCardLabels; // Labels para as cartas da mesa (Flop, Turn, River)
    private JButton[] cardButtons; // Botões para seleção das cartas
    private HashMap<String, ImageIcon> cardImages; // Mapa para armazenar as imagens
    private String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K", "A"}; // Alterado o valor 10 para 'T'
    private String[] suits = {"♥", "♠", "♦", "♣"}; // Símbolos dos naipes
    private int selectedCards = 0; // Contador de cartas selecionadas
    private JTextField percentageField; // Campo de texto para a porcentagem de chance de vitória
    private JButton resetButton; // Botão para resetar o bordo

    public PokerGUI() {
        setTitle("Poker Hands Analyzer");
        setSize(800, 700); // Aumentar o tamanho da janela para comportar o campo de porcentagem
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setIconImage(new ImageIcon(getClass().getResource("/asset/appicon.png")).getImage());


        // Inicializar o mapa de imagens
        cardImages = new HashMap<>();
        loadCardImages();

        // Painel para exibir a mão do jogador
        playerHandDisplayPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        handCardLabels = new JLabel[2]; // Duas cartas para a mão
        for (int i = 0; i < 2; i++) {
            handCardLabels[i] = new JLabel("Your Hand " + (i + 1), JLabel.CENTER);
            handCardLabels[i].setPreferredSize(new Dimension(100, 150)); // Tamanho das cartas
            handCardLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            playerHandDisplayPanel.add(handCardLabels[i]);
        }

        // Painel para exibir as cartas da mesa (Flop, Turn, River)
        tableCardsDisplayPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        tableCardLabels = new JLabel[5]; // Cinco cartas para a mesa
        for (int i = 0; i < 5; i++) {
            tableCardLabels[i] = new JLabel("Table " + (i + 1), JLabel.CENTER);
            tableCardLabels[i].setPreferredSize(new Dimension(100, 150)); // Tamanho das cartas
            tableCardLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            tableCardsDisplayPanel.add(tableCardLabels[i]);
        }

        // Painel para os botões das cartas
        JPanel cardSelectionPanel = new JPanel(new GridLayout(4, 13, 5, 5));
        cardButtons = new JButton[52];
        int idx = 0;
        for (String suit : suits) {
            for (String value : values) {
                String cardDisplayValue = value;
                if (value.equals("10")) cardDisplayValue = "T";
                else if (value.equals("Jack")) cardDisplayValue = "J";
                else if (value.equals("Queen")) cardDisplayValue = "Q";
                else if (value.equals("King")) cardDisplayValue = "K";
                else if (value.equals("Ace")) cardDisplayValue = "A";
                String cardRepresentation = cardDisplayValue + " " + suit;
                JButton cardButton = new JButton(cardRepresentation);  // Texto da carta no botão
                cardButtons[idx] = cardButton;
                cardSelectionPanel.add(cardButton);

                int finalIdx = idx;
                cardButtons[idx].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        escolherCarta(finalIdx);
                    }
                });

                idx++;
            }
        }

        // Campo de texto para exibir a porcentagem de vitória
        percentageField = new JTextField("Win Prob: 0%");
        percentageField.setEditable(false);
        percentageField.setHorizontalAlignment(JTextField.CENTER);

        // Botão para resetar o bordo
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });

        // Painel para a porcentagem e o botão de reset
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomPanel.add(percentageField);
        bottomPanel.add(resetButton);

        // Adicionar painéis ao frame
        add(playerHandDisplayPanel, BorderLayout.NORTH);  // Exibir mão do jogador no topo
        add(tableCardsDisplayPanel, BorderLayout.CENTER); // Exibir as cartas da mesa no meio
        add(cardSelectionPanel, BorderLayout.SOUTH);      // Botões de seleção na parte inferior
        add(bottomPanel, BorderLayout.EAST);              // Painel de porcentagem e reset na direita
    }

    // Método para carregar as imagens das cartas
    private void loadCardImages() {
        for (String suit : suits) {
            for (String value : values) {
                String fixed = value;
                String cardKey = fixed + " " + suit;
             // System.out.println(cardKey);
                String fileName = "../assets/" + value.replace("T","10").replace("J","Jack").replace("Q","Queen").replace("K","King").replace("A","Ace") + "_of_" + suit.replace("♥", "hearts").replace("♠", "spades").replace("♦", "diamonds").replace("♣", "clubs") + ".png";  // Ajuste o caminho para suas imagens
                ImageIcon cardImage = new ImageIcon(fileName);
                // Redimensionar a imagem para caber no label (100x150)
                Image scaledImage = cardImage.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                cardImages.put(cardKey, new ImageIcon(scaledImage));
            }
        }
    }
    

    // Método para escolher cartas e atribuí-las à mão ou à mesa
    private void escolherCarta(int idxCarta) {
        if (selectedCards >= 7) { // Impedir a seleção de mais de 7 cartas
            return;
        }

        String cartaEscolhida = cardButtons[idxCarta].getText();
        ImageIcon cartaImagem = cardImages.get(cartaEscolhida);

        if (selectedCards < 2) { // Duas primeiras cartas vão para a mão do jogador
            handCardLabels[selectedCards].setIcon(cartaImagem);  // Exibir a imagem da carta
            handCardLabels[selectedCards].setText("");  // Remover o texto
        } else if (selectedCards >= 2 && selectedCards < 7) { // Flop, Turn e River (mesa)
            tableCardLabels[selectedCards - 2].setIcon(cartaImagem);  // Exibir a imagem da carta
            tableCardLabels[selectedCards - 2].setText("");  // Remover o texto
        }

        cardButtons[idxCarta].setEnabled(false); // Desabilitar o botão após a escolha
        selectedCards++;

        // Atualizar a chance de vitória com base nas cartas escolhidas
        winratioCalc();
    }

    // Método para atualizar a chance de vitória (simples cálculo fictício para exemplo)
    private void winratioCalc() {
        double chance = 32.5; // Valor fictício de exemplo, você pode mudar conforme o cálculo real
    
        // Atualizar o texto da porcentagem
        percentageField.setText("Win Prob: " + chance + "%");
    
        // Alterar a cor de fundo do campo de porcentagem com base na chance de vitória
        if (chance > 70) {
            percentageField.setBackground(Color.GREEN); // Alta chance, fundo verde
        } else if (chance > 40) {
            percentageField.setBackground(Color.YELLOW); // Chance moderada, fundo amarelo
        } else {
            percentageField.setBackground(Color.RED); // Baixa chance, fundo vermelho
            percentageField.setForeground(Color.WHITE);
        }
    }
    // Método para resetar o bordo
    private void resetBoard() {
        // Limpar as cartas da mão e da mesa
        for (int i = 0; i < 2; i++) {
            handCardLabels[i].setIcon(null);
            handCardLabels[i].setText("Your Hand " + (i + 1));
        }
        for (int i = 0; i < 5; i++) {
            tableCardLabels[i].setIcon(null);
            tableCardLabels[i].setText("Table " + (i + 1));
        }

        // Reativar todos os botões de cartas
        for (JButton cardButton : cardButtons) {
            cardButton.setEnabled(true);
        }

        // Resetar contagem de cartas
        selectedCards = 0;

        // Resetar a chance de vitória
        percentageField.setText("Win Prob: 0%");
    }

    public static void main(String[] args) {
        // Criar o objeto da tela de loading
        LoadingScreen loadingScreen = new LoadingScreen();
    
        // Mostrar a tela de loading
        loadingScreen.showLoadingScreen();
    
        // Simula o tempo de carregamento e atualiza a barra de progresso
        for (int i = 0; i <= 100; i += 10) {
            try {
                Thread.sleep(50); // Simulação de um processo de carregamento
                loadingScreen.updateProgress(i); // Atualiza a barra de progresso
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        // Esconde a tela de loading
        loadingScreen.hideLoadingScreen();
    
        // Inicia a interface principal do seu programa
        PokerGUI pokerGUI = new PokerGUI();
        pokerGUI.setVisible(true);
    }
}
