import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceGrafica {

    private IndiceHash indice;
    private JTextArea resultArea;
    private JTextField chaveBuscaField;
    private JTextField tamanhoPagina;
    private JTextField tamanhoBucket;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfaceGrafica().criarInterface());
    }

    public void criarInterface() {
        // Criar a janela principal
        JFrame frame = new JFrame("Visualizador de Índice Hash");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Inicializa o índice hash (exemplo de arquivo)
        indice = new IndiceHash("C:\\Users\\User\\Downloads\\words.txt");

        // Painel para os controles (campo de busca e botões)
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(7, 1, 10, 10));

        // Campo para inserir chave de busca
        chaveBuscaField = new JTextField(20);
        chaveBuscaField.setToolTipText("Digite a chave para busca");

        tamanhoPagina = new JTextField();
        tamanhoBucket = new JTextField();

        // Botões para ações
        JButton btnBuscarChave = new JButton("Buscar Chave");

        // Adicionando ações aos botões
        btnBuscarChave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarChave();
            }
        });

        // Adicionando componentes ao painel de controles
        controlPanel.add(new JLabel("Digite a chave:"));
        controlPanel.add(chaveBuscaField);

        controlPanel.add(new JLabel("Digite o tamanho da página:"));
        controlPanel.add(tamanhoPagina);

        controlPanel.add(new JLabel("Digite o tamanho do bucket:"));
        controlPanel.add(tamanhoBucket);

        controlPanel.add(btnBuscarChave);


        // Área para exibir os resultados
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // Adicionando o painel de controle e área de resultados ao frame
        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Exibir a janela
        frame.setVisible(true);
    }

    private void buscarChave() {
        String chave = chaveBuscaField.getText().trim();
        if (chave.isEmpty()) {
            resultArea.setText("Por favor, insira uma chave para busca.");
            return;
        }

        try {
            // Contando as buscas pelo índice
            int qtdBuscaIndice = indice.chaveBusca(chave);

            // Contando as buscas pelo table scan
            int qtdBuscaTabela = indice.tableScan(chave);

            // Calcula as taxas de colisões e overflow
            double taxaColisoes = 100 * (double) indice.getNColisoes() / indice.getPaginasLength();
            double taxaOverflow = 100 * (double) indice.getNBucketOverflow() / indice.getBucketsLength();

            // Exibe os resultados na área de texto
            String resultadoBusca = "Busca pelo Índice:\n";
            resultadoBusca += "Chave encontrada após " + qtdBuscaIndice + " páginas no índice.\n";
            resultadoBusca += "\nBusca pelo Table Scan:\n";
            resultadoBusca += "Chave encontrada após " + qtdBuscaTabela + " páginas na tabela.\n";
            resultadoBusca += String.format("\nTaxa de Colisões = %.6f%%\n", taxaColisoes);
            resultadoBusca += String.format("Taxa de Buckets Overflow = %.6f%%\n", taxaOverflow);
            resultArea.setText(resultadoBusca);

        } catch (Exception e) {
            try {
                // Caso o índice falhe, fazemos a busca completa no table scan
                int qtdBuscaTabela = indice.tableScan(chave);
                double taxaColisoes = 100 * (double) indice.getNColisoes() / indice.getPaginasLength();
                double taxaOverflow = 100 * (double) indice.getNBucketOverflow() / indice.getBucketsLength();

                // Exibe os resultados do table scan na área de texto
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
