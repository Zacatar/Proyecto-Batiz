import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.*;

public class Main {

    private JFrame miVentana;
    private JTextArea textAreaProgram;
    private JTextArea textAreaIntermedio;
    private JTextArea textAreaObjeto;
    private JButton botonLexico;
    private JButton botonParser;
    private JButton botonLimpiar;
    private JButton botonSemantico;
    private JButton botonIntermedio;
    private JButton botonObjeto;

    public Main() {
        initUI();
    }

    private void initUI() {
        miVentana = new JFrame("Compilador");
        miVentana.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        miVentana.setSize(1000, 600);
        miVentana.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        miVentana.setContentPane(mainPanel);

        crearMenu();
        mainPanel.add(crearPanelBotones(), BorderLayout.NORTH);
        mainPanel.add(crearPanelTextos(), BorderLayout.CENTER);

        configurarListeners();

        miVentana.setVisible(true);
    }

    private void crearMenu() {
        JMenuBar menu = new JMenuBar();
        miVentana.setJMenuBar(menu);

        JMenu archivo = new JMenu("Archivo");
        menu.add(archivo);

        JMenuItem nuevo = new JMenuItem("Nuevo");
        JMenuItem abrir = new JMenuItem("Abrir");
        JMenuItem guardar = new JMenuItem("Guardar");
        JMenuItem salir = new JMenuItem("Salir");

        archivo.add(nuevo);
        archivo.add(abrir);
        archivo.add(guardar);
        archivo.addSeparator();
        archivo.add(salir);

        nuevo.addActionListener(e -> limpiarTexto());
        abrir.addActionListener(e -> abrirArchivo());
        guardar.addActionListener(e -> guardarArchivo());
        salir.addActionListener(e -> System.exit(0));
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        botonLexico = new JButton("Lexico");
        botonParser = new JButton("Parser");
        botonLimpiar = new JButton("Limpiar");
        botonSemantico = new JButton("Semantico");
        botonIntermedio = new JButton("Intermedio");
        botonObjeto = new JButton("Objeto");

        botonParser.setEnabled(false);
        botonSemantico.setEnabled(false);
        botonIntermedio.setEnabled(false);
        botonObjeto.setEnabled(false);

        panel.add(new JLabel("Programa:"));
        panel.add(botonLexico);
        panel.add(botonLimpiar);
        panel.add(botonParser);
        panel.add(botonSemantico);
        panel.add(botonIntermedio);
        panel.add(botonObjeto);

        return panel;
    }

    private JPanel crearPanelTextos() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        textAreaProgram = new JTextArea();
        textAreaIntermedio = new JTextArea();
        textAreaObjeto = new JTextArea();

        textAreaProgram.setFont(new Font("Arial", Font.PLAIN, 16));
        textAreaIntermedio.setFont(new Font("Arial", Font.PLAIN, 16));
        textAreaObjeto.setFont(new Font("Arial", Font.PLAIN, 16));

        JScrollPane scrollProgram = new JScrollPane(textAreaProgram);
        JScrollPane scrollIntermedio = new JScrollPane(textAreaIntermedio);
        JScrollPane scrollObjeto = new JScrollPane(textAreaObjeto);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(scrollProgram, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        panel.add(scrollIntermedio, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.35;
        panel.add(scrollObjeto, gbc);

        return panel;
    }

    private void configurarListeners() {
        textAreaProgram.getDocument().addDocumentListener(new DocumentListener() {
            private void resetButtons() {
                botonLexico.setEnabled(true);
                botonParser.setEnabled(false);
                botonSemantico.setEnabled(false);
                botonLimpiar.setEnabled(true);
            }
            public void insertUpdate(DocumentEvent e) { resetButtons(); }
            public void removeUpdate(DocumentEvent e) { resetButtons(); }
            public void changedUpdate(DocumentEvent e) { resetButtons(); }
        });

        botonLimpiar.addActionListener(e -> limpiarTexto());
        botonLexico.addActionListener(e -> ejecutarLexico());
        botonParser.addActionListener(e -> ejecutarParser());
    }

    private void limpiarTexto() {
        textAreaProgram.setText("");
        textAreaIntermedio.setText("");
        textAreaObjeto.setText("");
        botonParser.setEnabled(false);
        botonLexico.setEnabled(true);
    }

    private void abrirArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto", "txt"));
        int resultado = fileChooser.showOpenDialog(miVentana);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                StringBuilder contenido = new StringBuilder();
                String linea;
                while ((linea = br.readLine()) != null) {
                    contenido.append(linea).append("\n");
                }
                textAreaProgram.setText(contenido.toString());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(miVentana, "Error al abrir archivo:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarArchivo() {
        if (textAreaProgram.getText().isEmpty()) {
            JOptionPane.showMessageDialog(miVentana, "No hay nada que guardar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showSaveDialog(miVentana);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(textAreaProgram.getText());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(miVentana, "Error al guardar archivo:\n" + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ejecutarLexico() {
    String codigo = textAreaProgram.getText();
    Scanner scanner = new Scanner(codigo);
    StringBuilder resultado = new StringBuilder();

    String token;

    // Proteger con límite de tokens y checar índice
    int maxTokens = 10000; // seguridad para evitar ciclos infinitos
    int contador = 0;

    do {
        if (scanner.checkNextToken() == null || scanner.checkNextToken().isEmpty()) break;

        token = scanner.getToken(true);

        if (token == null) break;

        if (token.equals("TOKEN INVÁLIDO")) {
            resultado.append("Token inválido encontrado. Análisis detenido.\n");
            break;
        }

        resultado.append(token).append(" - ").append(scanner.getTipoToken()).append("\n");

        contador++;
        if (contador > maxTokens) {
            resultado.append("Error: demasiados tokens. Posible bucle infinito.\n");
            break;
        }

    } while (true);

    textAreaIntermedio.setText(resultado.toString());

    botonParser.setEnabled(true);
    botonSemantico.setEnabled(false);
    botonIntermedio.setEnabled(false);
    botonObjeto.setEnabled(false);
}


    private void ejecutarParser() {
    String codigo = textAreaProgram.getText();
    try {
        Parser parser = new Parser(codigo);

        // Mostrar la tabla de símbolos en el textAreaIntermedio
        String tablaSimbolosTexto = parser.getSymbolTableString();
        textAreaIntermedio.setText(tablaSimbolosTexto);

        JOptionPane.showMessageDialog(miVentana, "Análisis sintáctico completado correctamente.", "Parser",
                JOptionPane.INFORMATION_MESSAGE);

        // Habilitar otros botones si quieres
        botonSemantico.setEnabled(true);
        botonIntermedio.setEnabled(true);
        botonObjeto.setEnabled(true);

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(miVentana, "Error en Parser:\n" + ex.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
