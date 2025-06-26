import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
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
        miVentana.setLocationRelativeTo(null); // Centrar ventana

        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        miVentana.setContentPane(mainPanel);

        // Crear menú
        crearMenu();

        // Crear panel superior para botones
        JPanel panelBotones = crearPanelBotones();
        mainPanel.add(panelBotones, BorderLayout.NORTH);

        // Crear panel central para áreas de texto
        JPanel panelTextos = crearPanelTextos();
        mainPanel.add(panelTextos, BorderLayout.CENTER);

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

        // Acciones menú
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

        // Estado inicial botones
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

        // Program
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5,5,5,5);
        panel.add(scrollProgram, gbc);

        // Intermedio
        gbc.gridx = 1;
        gbc.weightx = 0.35;
        panel.add(scrollIntermedio, gbc);

        // Objeto
        gbc.gridx = 2;
        gbc.weightx = 0.35;
        panel.add(scrollObjeto, gbc);

        return panel;
    }

    private void configurarListeners() {
        // Listener para activar/desactivar botones al escribir
        textAreaProgram.getDocument().addDocumentListener(new DocumentListener() {
            private void resetButtons() {
                botonLexico.setEnabled(true);
                botonParser.setEnabled(true);
                botonSemantico.setEnabled(false);
                botonLimpiar.setEnabled(true);
            }
            @Override
            public void insertUpdate(DocumentEvent e) { resetButtons(); }
            @Override
            public void removeUpdate(DocumentEvent e) { resetButtons(); }
            @Override
            public void changedUpdate(DocumentEvent e) { resetButtons(); }
        });

        botonLimpiar.addActionListener(e -> limpiarTexto());

        botonParser.addActionListener(e -> ejecutarParser());

        // Aquí puedes añadir más listeners para otros botones según tu lógica
    }

    private void limpiarTexto() {
        textAreaProgram.setText("");
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

    private void ejecutarParser() {
        String codigo = textAreaProgram.getText();
        try {
            new Parser(codigo);
            JOptionPane.showMessageDialog(miVentana, "Análisis sintáctico completado correctamente.", "Parser",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(miVentana, "Error en Parser:\n" + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
