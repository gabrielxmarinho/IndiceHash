import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class InterfaceGrafica {

    private IndiceHash indice;
    private JTextArea resultArea;
    private JTextField chaveBuscaField;
    private JTextField tamanhoPaginaField;
    private JTextField tamanhoBucketField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceGrafica().criarInterface());
    }

    public void criarInterface() {
        JFrame frame = new JFrame("Visualizador de Índice Hash");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());

        // Inicializa os JTextField corretamente
        tamanhoPaginaField = new JTextField();
        tamanhoBucketField = new JTextField();

        // Extração segura dos valores para evitar NumberFormatException
        int tamanhoPagina = !tamanhoPaginaField.getText().trim().isEmpty() ? Integer.parseInt(tamanhoPaginaField.getText().trim()) : 10;
        int tamanhoBucket = !tamanhoBucketField.getText().trim().isEmpty() ? Integer.parseInt(tamanhoBucketField.getText().trim()) : 10;

        // Criação do objeto IndiceHash com valores válidos
        indice = new IndiceHash("C:\\Users\\User\\Downloads\\words.txt", tamanhoPagina, tamanhoBucket);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(7, 1, 10, 10));

        chaveBuscaField = new JTextField(20);
        chaveBuscaField.setToolTipText("Digite a chave para busca");

        JButton btnBuscarChave = new JButton("Buscar Chave");
        btnBuscarChave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarChave();
            }
        });

        controlPanel.add(new JLabel("Digite a chave:"));
        controlPanel.add(chaveBuscaField);
        controlPanel.add(new JLabel("Digite o tamanho da página:"));
        controlPanel.add(tamanhoPaginaField);
        controlPanel.add(new JLabel("Digite o tamanho do bucket:"));
        controlPanel.add(tamanhoBucketField);
        controlPanel.add(btnBuscarChave);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void buscarChave() {
        String chave = chaveBuscaField.getText().trim();
        if (chave.isEmpty()) {
            resultArea.setText("Por favor, insira uma chave para busca.");
            return;
        }

        try {
            // Extração dos valores dos JTextField corretamente
            int novoTamanhoPagina = !tamanhoPaginaField.getText().trim().isEmpty() ? Integer.parseInt(tamanhoPaginaField.getText().trim()) : indice.getPaginasLength();
            int novoTamanhoBucket = !tamanhoBucketField.getText().trim().isEmpty() ? Integer.parseInt(tamanhoBucketField.getText().trim()) : indice.getBucketsLength();

            // Atualiza o IndiceHash caso os tamanhos tenham sido alterados
            if (novoTamanhoPagina != indice.getPaginasLength() || novoTamanhoBucket != indice.getBucketsLength()) {
                indice = new IndiceHash("C:\\Users\\User\\Downloads\\words.txt", novoTamanhoPagina, novoTamanhoBucket);
            }

            int qtdBuscaIndice = indice.chaveBusca(chave);
            int qtdBuscaTabela = indice.tableScan(chave);

            double taxaColisoes = 100 * (double) indice.getNColisoes() / indice.getPaginasLength();
            double taxaOverflow = 100 * (double) indice.getNBucketOverflow() / indice.getBucketsLength();

            String resultadoBusca = "Busca pelo Índice:\n";
            resultadoBusca += "Chave encontrada após " + qtdBuscaIndice + " páginas no índice.\n";
            resultadoBusca += "\nBusca pelo Table Scan:\n";
            resultadoBusca += "Chave encontrada após " + qtdBuscaTabela + " páginas na tabela.\n";
            resultadoBusca += String.format("\nTaxa de Colisões = %.6f%%\n", taxaColisoes);
            resultadoBusca += String.format("Taxa de Buckets Overflow = %.6f%%\n", taxaOverflow);
            resultArea.setText(resultadoBusca);

        } catch (NumberFormatException e) {
            resultArea.setText("Erro: Certifique-se de inserir números válidos para os tamanhos.");
        } catch (Exception e) {
            try {
                int qtdBuscaTabela = indice.tableScan(chave);
                double taxaColisoes = 100 * (double) indice.getNColisoes() / indice.getPaginasLength();
                double taxaOverflow = 100 * (double) indice.getNBucketOverflow() / indice.getBucketsLength();

                String resultadoBusca = "Chave não encontrada no índice. Busca completa na tabela realizada após " + qtdBuscaTabela + " buscas.\n";
                resultadoBusca += String.format("Taxa de Colisões = %.6f%%\n", taxaColisoes);
                resultadoBusca += String.format("Taxa de Buckets Overflow = %.6f%%\n", taxaOverflow);
                resultArea.setText(resultadoBusca);

            } catch (Exception ex) {
                resultArea.setText("Chave não encontrada.");
            }
        }
    }
}