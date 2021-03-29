package br.udesc.ceavi.dsd.view;

import br.udesc.ceavi.dsd.controller.SystemController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

/**
 *
 * @author Gustavo C. Santos
 * @since 26/08/2019
 */
public class FramePrincipal extends JFrame implements FramePrincipalObserver {

    private static Dimension sizePrefesss = new Dimension(800, 800);
    private JPanel jpConfig;
    private JPanel jpTable;
    private JButton btnStop;
    private JButton btnStart;

    private JButton btnCarregarNovaMatriz;
    private JSpinner jsNumCarro;
    private JLabel lbLimiteCarro;
    private JLabel lbNumCarrosSimulacao;
    private SystemController controller;
    private GridBagConstraints cons;
    private MalhaTable table;

    public FramePrincipal() {
        controller = SystemController.getInstance();
        initFrameProperty();
        initComponnnets();
        initListeners();
        controller.addFramePrincipalObserver(this);
    }

    private void initFrameProperty() {
        this.setSize(sizePrefesss);
        this.setMinimumSize(sizePrefesss);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Simulação");
        this.getContentPane().setLayout(new BorderLayout(1, 2));
    }

    private void initComponnnets() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FramePrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        Dimension size;
        Container contentPane = this.getContentPane();
        this.jpConfig = new JPanel();
        this.jpTable = new JPanel();

        size = new Dimension(0, 150);
        setSizeI(jpConfig, size);
        this.jpConfig.setMaximumSize(size);

        initConfigComponets();

        contentPane.add(jpConfig, BorderLayout.NORTH);
        contentPane.add(jpTable, BorderLayout.CENTER);

        jpConfig.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));
        this.jpTable.setBackground(new Color(0, 102, 0));
    }

    private void initConfigComponets() {
        Dimension size;
        size = new Dimension(700, 140);
        JPanel jPConfingII = new JPanel();
        setSizeI(jPConfingII, size);
        jPConfingII.setMaximumSize(size);
        jPConfingII.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0)));

        this.jpConfig.add(jPConfingII);
        jPConfingII.setLayout(new GridBagLayout());
        cons = new GridBagConstraints();

        JPanel jpConfingIII = new JPanel();
        jpConfingIII.setLayout(new GridBagLayout());
        cons.gridx = 0;
        cons.gridy = 1;
        cons.gridwidth = 3;
        cons.fill = GridBagConstraints.HORIZONTAL;
        jPConfingII.add(jpConfingIII, cons);

        this.btnStop = new JButton("Stop");
        this.btnStart = new JButton("Start");
        this.jsNumCarro = new JSpinner();
        jsNumCarro.setModel(new SpinnerNumberModel(1, 1, controller.getMalhaController().getNumCasasValida(), 1));
        this.lbLimiteCarro = new JLabel("Numero de Carros: ");

        Insets insets = new Insets(0, 10, 0, 10);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 0;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = insets;
        jpConfingIII.add(this.lbLimiteCarro, cons);
        jpConfingIII.setBorder(BorderFactory.createLineBorder(new Color(102, 102, 0)));

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = insets;
        jpConfingIII.add(this.jsNumCarro, cons);

        cons = new GridBagConstraints();
        cons.gridx = 1;
        cons.gridy = 0;
        cons.ipadx = 25;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = insets;
        jpConfingIII.add(this.btnStart, cons);

        cons = new GridBagConstraints();
        cons.gridx = 1;
        cons.gridy = 1;
        cons.ipadx = 25;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = insets;
        this.btnStop.setEnabled(false);
        jpConfingIII.add(this.btnStop, cons);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 2;
        cons.ipadx = 25;
        cons.gridwidth = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = insets;
        btnCarregarNovaMatriz = new JButton("Carregar Nova Matriz");
        jpConfingIII.add(btnCarregarNovaMatriz, cons);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 2;
        cons.gridwidth = 3;
        cons.fill = GridBagConstraints.NONE;
        cons.anchor = GridBagConstraints.CENTER;
        jPConfingII.add(new JLabel("Numero de Carros Na Simulação Atualmente: "), cons);

        cons = new GridBagConstraints();
        cons.gridx = 0;
        cons.gridy = 3;
        cons.gridwidth = 3;
        cons.fill = GridBagConstraints.NONE;
        lbNumCarrosSimulacao = new JLabel("   ");
        jPConfingII.add(lbNumCarrosSimulacao, cons);
        initTableFrame();
    }

    private void setSizeI(JComponent c, Dimension d) {
        c.setSize(d);
        c.setMinimumSize(d);
        c.setPreferredSize(d);
    }

    @Override
    public void notificarNumeroDeCarro(int numCarro) {
        lbNumCarrosSimulacao.setText("" + numCarro);
    }

    private void initListeners() {
        this.btnStart.addActionListener((e) -> btnStartListeners());
        this.btnStop.addActionListener((e) -> btnStopListeners());
        this.btnCarregarNovaMatriz.addActionListener((e) -> btnCarregarNovaMatrizListeners());
    }

    public void initTableFrame() {
        jpTable.removeAll();
        jsNumCarro.setModel(new SpinnerNumberModel(1, 1, controller.getMalhaController().getNumCasasValida(), 1));
        jpTable.setLayout(new BoxLayout(jpTable, BoxLayout.PAGE_AXIS));
        table = new MalhaTable(jpTable);
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(table);
        pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        pane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        pane.setBackground(new Color(0, 0, 0, 0));
        pane.setOpaque(false);
        pane.getViewport().setOpaque(true);
        pane.setViewportBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pane.getViewport().setBackground(new Color(0, 0, 0, 0));
        jpTable.add(pane);
        jpTable.repaint();
        jpTable.revalidate();
    }

    @Override
    public void notificarSimulacaoFinalizada() {
        btnCarregarNovaMatriz.setEnabled(true);
        btnStart.setEnabled(true);
        jsNumCarro.setEnabled(true);
    }

    private void btnStopListeners() {
        btnStop.setEnabled(false);
        controller.pararRepawn();
    }

    private void btnStartListeners() {
        jsNumCarro.setEnabled(false);
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        btnCarregarNovaMatriz.setEnabled(false);

        int numeroCarro = (int) jsNumCarro.getValue();
        controller.startSimulation(numeroCarro);
    }

    private void btnCarregarNovaMatrizListeners() {
        EventQueue.invokeLater(() -> new FrameConfig(this).setVisible(true));
    }

}
