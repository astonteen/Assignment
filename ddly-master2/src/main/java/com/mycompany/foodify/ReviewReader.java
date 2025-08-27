import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReviewReader extends JFrame {
    private JTextArea displayArea;
    
    public ReviewReader() {
        setTitle("Review History");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea));
        
        loadReviews();
    }
    
    private void loadReviews() {
        try (BufferedReader reader = new BufferedReader(new FileReader("review.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                displayArea.append(line + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReviewReader().setVisible(true);
        });
    }
}