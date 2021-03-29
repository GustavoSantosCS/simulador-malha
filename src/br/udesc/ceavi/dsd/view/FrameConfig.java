package br.udesc.ceavi.dsd.view;

import br.udesc.ceavi.dsd.controller.SystemController;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.logging.Logger;

/**
 * @author Gustavo C. Santos
 * @since 28/08/2019
 */
public class FrameConfig extends JFrame {

    private String caminho;

    private Dimension size;
    private JLabel lbArquivo;
    private JLabel lbAreaCreatica;
    private JTextField tfArquivo;
    private JButton btnBuscarArquivo;
    private JButton btnCreateMalha;
    private JButton btnCancelar;
    private JRadioButton rbMonitor;
    private JRadioButton rbSemafaro;
    private ButtonGroup radioGrupo;
    private GridBagConstraints cons;
    private SystemController controller;
    private FramePrincipal framePrincipal;

    public FrameConfig() {
        this.controller = SystemController.getInstance();
        initFrameConfig();
        initComponents();
        initListeners();
    }

    public FrameConfig(FramePrincipal framePrincipal) {
        this();
        this.framePrincipal = framePrincipal;
    }

    private void initFrameConfig() {
        this.setTitle("Simulador Trafico - Config");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        size = new Dimension(525, 210);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            Logger.getLogger(FrameConfig.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        this.lbArquivo = new JLabel("Arquivo: ");
        this.lbAreaCreatica = new JLabel("Selecione o controler de área critica:");
        this.tfArquivo = new JTextField();
        this.btnBuscarArquivo = new JButton("Abrir Arquivo");
        this.btnCancelar = new JButton("Cancelar");
        this.btnCreateMalha = new JButton("Create Manha");
        this.rbMonitor = new JRadioButton("Monitor");
        this.rbSemafaro = new JRadioButton("Semafaro");
        this.radioGrupo = new ButtonGroup();
        radioGrupo.add(rbMonitor);
        radioGrupo.add(rbSemafaro);
        initPosition();
    }

    private void setSizeI(JComponent c, Dimension d) {
        c.setSize(d);
        c.setMinimumSize(d);
        c.setPreferredSize(d);
    }

    private void initPosition() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());

        JPanel jpArquivo = new JPanel();
        contentPane.setBackground(jpArquivo.getBackground());
        size = new Dimension(450, 50);
        setSizeI(jpArquivo, size);
        jpArquivo.setMaximumSize(size);
        jpArquivo.setBackground(this.getBackground());
        jpArquivo.add(lbArquivo);
        size = new Dimension(250, 24);
        setSizeI(tfArquivo, size);
        tfArquivo.setEditable(false);
        jpArquivo.add(tfArquivo);
        jpArquivo.add(btnBuscarArquivo);

        cons = new GridBagConstraints();
        cons.insets = new Insets(-10, 0, 0, 0);
        contentPane.add(jpArquivo, cons);

        JPanel p = new JPanel(new GridBagLayout());
        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 2;
        p.add(lbAreaCreatica, cons);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 2;
        p.add(rbMonitor, cons);

        cons = new GridBagConstraints();
        cons.gridx = 1;
        cons.gridy = 2;
        p.add(rbSemafaro, cons);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 3;
        p.add(btnCreateMalha, cons);

        cons = new GridBagConstraints();
        cons.gridx = 1;
        cons.gridy = 3;
        p.add(btnCancelar, cons);

        cons = new GridBagConstraints();
        cons.gridy = 1;

        cons.insets = new Insets(7, 0, 0, 0);
        contentPane.add(p, cons);
    }

    private void initListeners() {
        btnBuscarArquivo.addActionListener((e) -> btnAbrirListeners());
        btnCancelar.addActionListener((e) -> btnCancelarListeners());
        btnCreateMalha.addActionListener((e) -> btnCreateListeners());
    }

    private void btnAbrirListeners() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileFilter fileFilter = new FileNameExtensionFilter("txt", "txt");
        fileChooser.setFileFilter(fileFilter);
        int retorno = fileChooser.showOpenDialog(this);
        if (retorno == JFileChooser.APPROVE_OPTION) {
            String absolutePath = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                controller.readFile(absolutePath);

                caminho = absolutePath;
                tfArquivo.setText(caminho);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void btnCancelarListeners() {
    }

    private void btnCreateListeners() {
        if (caminho == null || caminho.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Arquivo de Malha Não Informado");
            return;
        }
        if (radioGrupo.getSelection() == null) {
            JOptionPane.showMessageDialog(null, "Selecione O Tipo de Controller de Regiao Critica");
            return;
        }

        if (rbMonitor.isSelected()) {
            controller.setFactory("Monitor");
        } else if (rbSemafaro.isSelected()) {
            controller.setFactory("Semafaro");
        }

        controller.getMalhaController().initMalha();

        if (framePrincipal != null) {
            controller.rebutMalha();
            framePrincipal.initTableFrame();
        } else {
            EventQueue.invokeLater(() -> new FramePrincipal().setVisible(true));
        }
        this.dispose();
    }


}
