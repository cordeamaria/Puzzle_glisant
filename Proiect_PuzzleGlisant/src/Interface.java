import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Interface {
    private JFrame frame;
    private PuzzleGame puzzleGame;
    private JPanel boardPanel; // Panou pentru tabla de joc
    private final String imagesFolder; // Directorul de imagini

    public Interface() {
        puzzleGame = new PuzzleGame();
        imagesFolder = System.getProperty("user.dir") + File.separator + "images"; // Directorul de imagini relativ la directorul curent
    }

    public void createAndShow() {
        frame = new JFrame("Puzzle Glisant");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setăm dimensiunea ferestrei pentru a include tabla de joc și butonul de amestecare
        // Fereastra va avea 400x450 pixeli (300 pentru tabla + 150 pentru butonul de amestecare)
        frame.setSize(400, 450);

        // Creăm panoul pentru tabla de joc cu dimensiune fixă 300x300 pixeli
        boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setPreferredSize(new Dimension(300, 300)); // Dimensiune fixă pentru panou
        boardPanel.setBackground(new Color(182, 237, 218));

        // Adăugăm butonul de amestecare
        JButton shuffleButton = new JButton("Amestecă");
        shuffleButton.setFont(new Font("Arial", Font.BOLD, 16));
        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                puzzleGame.shuffle(); // Amestecăm piesele
                updateBoard();        // Actualizăm tabla
            }
        });

        shuffleButton.setBackground(new Color(174, 180, 194));
        shuffleButton.setForeground(Color.WHITE);  // setam culoarea textului
        // Adăugăm butonul de amestecare în partea de jos
        frame.add(shuffleButton, BorderLayout.SOUTH);

        // Adăugăm tabla de joc
        updateBoard();


        frame.setVisible(true);
    }

    private void updateBoard() {
        // Eliminăm toate componentele anterioare
        boardPanel.removeAll();

        // Obținem tabla de joc
        int[][] board = puzzleGame.getBoard();

        // Creăm un GridBagConstraints pentru a seta dimensiunea fixă a fiecărui buton
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;   //umple complet ata pe orzontala cat si verticala
        gbc.weightx = 0;  // Împiedicăm redimensionarea butoanelor pe orizontală
        gbc.weighty = 0;  // Împiedicăm redimensionarea butoanelor pe verticală
        gbc.insets = new Insets(5, 5, 5, 5); // Adăugăm o margine de 5 pixeli între butoane

        // Căutăm piesele pentru fiecare buton
        for (int i = 0; i < puzzleGame.getSize(); i++) {
            for (int j = 0; j < puzzleGame.getSize(); j++) {
                // Creăm butonul pentru fiecare piesă
                JButton button = new JButton();
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setPreferredSize(new Dimension(110, 110)); // Dimensiune fixă pentru buton
                button.setBorder(BorderFactory.createEmptyBorder()); // Eliminăm marginile butonului
                button.setFocusPainted(false); // Eliminăm efectul de focus pe buton

                // Dacă piesa nu este goală (0), setăm imaginea pe buton
                if (board[i][j] != 0) {
                    // Încercăm să încărcăm imaginea, dacă nu reușim, setăm numărul pe buton
                    ImageIcon icon = loadImageIcon("piece" + (board[i][j]) + ".png", 110, 110);
                    if (icon != null) {
                        button.setIcon(icon); // Setăm imaginea pe buton dacă este încărcată cu succes
                    } else {
                        button.setText(String.valueOf(board[i][j])); // Setăm numărul pe buton în caz de eroare
                    }
                }
                // Dacă piesa este goală (0), nu setăm nici o imagine
                else {
                    button.setIcon(null);
                    button.setBackground(new Color(189, 195, 222));
                }

                // Adăugăm un listener pentru a muta piesele
                final int row = i, col = j;   //trebuie ca variabelel sa fie final pt a le putea folosi in listener
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (puzzleGame.movePiece(row, col)) {
                            updateBoard(); // Actualizăm tabla înainte de verificare

                            if (puzzleGame.isSolved()) {
                                // Creăm un timer care întârzie mesajul
                                Timer timer = new Timer(200, new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent evt) {
                                        showCustomMessage("Felicitări! Ai câștigat!");
                                    }
                                });
                                timer.setRepeats(false); // Timerul să ruleze o singură dată
                                timer.start();
                            }
                        }
                    }
                });

                // Adăugăm butonul la panou folosind GridBagLayout
                gbc.gridx = j; // Coloana
                gbc.gridy = i; // Linia
                boardPanel.add(button, gbc);
            }
        }

        // Adăugăm tabla de joc în fereastră
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.revalidate();     // Notifică managerul de layout al ferestrei că structura componentelor s-a schimbat
        frame.repaint();        // Solicită reafișarea ferestrei, forțând metoda paintComponent() să fie apelată pentru toate componentele.
    }

    // Metodă pentru a încărca imaginea
    public ImageIcon loadImageIcon(String fileName, int width, int height) {
        try {
            // Construim calea completă către fișierul de imagine
            File imageFile = new File(imagesFolder, fileName);
            BufferedImage img = ImageIO.read(imageFile); // Încărcăm imaginea
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); // Redimensionăm imaginea
            return new ImageIcon(scaledImg); // Creăm un ImageIcon cu imaginea redimensionată
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showCustomMessage(String message) {
        // Creăm un dialog modal
        UIManager.put("activeCaption", new javax.swing.plaf.ColorUIResource(254, 147, 112)); // Această linie modifică culoarea de fundal a barei de titlu a ferestrelor active din aplicație.
        JDialog.setDefaultLookAndFeelDecorated(true);
        JDialog dialog = new JDialog(frame, " ", true);  // true pentru a face dialogul modal (blocant)
        dialog.setLayout(new BorderLayout());

        // Creăm un JPanel pentru a adăuga JLabel
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(254, 147, 112));

        // Creăm un label cu mesajul
        JLabel label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setPreferredSize(new Dimension(300, 100)); // Dimensiune fixă pentru caseta de mesaj

        // Modificăm culoarea textului etichetei
        label.setForeground(new Color(255, 255, 255, 255));  // Text alb

        // Modificăm culoarea fundalului etichetei
        label.setOpaque(true);  // Trebuie să setăm opacitatea pentru ca fundalul să fie vizibil
        label.setBackground(new Color(254, 147, 112));

        // Adăugăm eticheta în panel
        panel.add(label, BorderLayout.CENTER);

        // Adăugăm panel-ul în dialog

        dialog.add(panel, BorderLayout.CENTER);
        // Setăm dimensiunea și vizibilitatea dialogului
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(frame);  // Poziționăm dialogul în centrul ferestrei
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);  // Închidem dialogul când utilizatorul face click pe "X"
        dialog.setVisible(true);  // Afișăm dialogul
    }
}
