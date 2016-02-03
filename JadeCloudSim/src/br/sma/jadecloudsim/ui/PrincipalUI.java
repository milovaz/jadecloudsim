package br.sma.jadecloudsim.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import br.sma.jadecloudsim.cloud.CloudElements;
import br.sma.jadecloudsim.main.Principal;

/**
 * Classe que implementa a GUI do sistema e permite iniciar a simulação
 * 
 * @author thiago
 *
 */
public class PrincipalUI {

	private static PrincipalUI instance = null;
	
	private final static String newline = "\n";
	private final static int NUM_CLOUDLETS_PADRAO = 2;
	private final static int NUM_DATA_CENTERS = 3;
	
	private JFrame janela;
	private JFrame janelaAuxiliar;
	private JPanel painelPrincipal;
	
	private JTextField qtdDataCentersTxtField;
	private JTextField qtdCloudletsTxtField;
	
	private JTextArea logTextArea;	
	private JTable table;
	private JPanel painelBase;
	private JPanel painelAuxiliar;
	
	protected PrincipalUI() {
		
	}
	
	public static PrincipalUI getInstance() {
		if(instance == null) {
			instance = new PrincipalUI();
		}
		return instance;
	}
	
	public static void main(String[] args) {
		PrincipalUI.getInstance().montaTela();
	}
	
	public void montaTela() {
		preparaJanela();
		preparaPainelPrincipal();
		mostraJanela();
	}
	
	private void preparaJanela() {
		janela = new JFrame("SMA");
		janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		janelaAuxiliar = new JFrame("Auxiliar");
		janelaAuxiliar.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void mostraJanela() {
		janela.pack();
		janela.setSize(540, 340);
		janela.setVisible(true);
		
		//janelaAuxiliar.pack();
		//janelaAuxiliar.setSize(540, 540);
		//janelaAuxiliar.setVisible(true);
	}
	
	private void preparaPainelPrincipal() {
		painelPrincipal = new JPanel();
		painelBase = new JPanel();
		painelAuxiliar = new JPanel();
		
		painelPrincipal.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;
		
		
		painelBase.setLayout(new GridLayout(1, 2));
		painelBase.add(painelPrincipal);
		
		
		janela.add(painelPrincipal);
		
		janelaAuxiliar.add(painelAuxiliar);
		
		JLabel l1 = new JLabel();
		l1.setText("Qtd. DataCenters");
		painelPrincipal.add(l1, c);
		
		c.gridx = 1;
		c.gridy = 0;
		
		qtdDataCentersTxtField = new JTextField(10);
		qtdDataCentersTxtField.setName("qtdDataCenters");
		qtdDataCentersTxtField.setText("" + NUM_DATA_CENTERS);
		painelPrincipal.add(qtdDataCentersTxtField, c);
		
		c.gridx = 0;
		c.gridy = 1;
		
		JLabel l2 = new JLabel();
		l2.setText("Qtd. Cloudlets");
		painelPrincipal.add(l2, c);
		
		c.gridx = 1;
		c.gridy = 1;
		
		
		qtdCloudletsTxtField = new JTextField(10);
		qtdCloudletsTxtField.setName("qtdCloudletsTxtField");
		qtdCloudletsTxtField.setText("" + NUM_CLOUDLETS_PADRAO);
		painelPrincipal.add(qtdCloudletsTxtField, c);
		
		c.weighty = 0.05;
		c.weightx = 0.05;
		c.gridx = 0;
		c.gridy = 2;
		
		
		painelPrincipal.add(preparaBotaoCarregar(), c);
		
		c.gridx = 1;
		c.gridy = 2;
		
		painelPrincipal.add(preparaBotaoSair(), c);
		
		c.gridx = 0;
		c.gridy = 3;
		
		painelPrincipal.add(preparaBotaoFalhaIndisponibilidade(), c);
		
		c.gridx = 1;
		c.gridy = 3;
		
		painelPrincipal.add(prepararBotaoFechar(), c);
		
		//c.ipady = 0; 
		c.weighty = 0.1;
		c.gridx = 0;
		c.gridy = 4;
		c.anchor = GridBagConstraints.PAGE_END;
		c.gridwidth = 2;
		
		logTextArea = new JTextArea(10, 30);
		JScrollPane scrollPane = new JScrollPane(logTextArea);
		painelPrincipal.add(scrollPane, c);
	}
	
	private JButton preparaBotaoFalhaIndisponibilidade() {
		JButton causarIndisponibilidade = new JButton("Forçar indisponibilidade");
		causarIndisponibilidade.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CloudElements.setFalhaIndisponilidadeTeste(true);
			}
		});
		
		return causarIndisponibilidade;
	}
	
	private JButton preparaBotaoCarregar() {
		JButton botaoCarregar = new JButton("Iniciar");
		botaoCarregar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int qtdDataCenters = NUM_DATA_CENTERS;
				int qtdCloudlets = NUM_CLOUDLETS_PADRAO;
				
				if(qtdDataCentersTxtField.getText() != null && !qtdDataCentersTxtField.getText().isEmpty()){
					qtdDataCenters = Integer.valueOf(qtdDataCentersTxtField.getText());
				}
				
				if(qtdCloudletsTxtField.getText() != null && !qtdCloudletsTxtField.getText().isEmpty()){
					qtdCloudlets = Integer.valueOf(qtdCloudletsTxtField.getText());
				}
				
				Principal principal = Principal.getInstance();
				principal.initJade();
				principal.initCloudSim(qtdDataCenters, qtdCloudlets);
			}
		});
		
		//painelPrincipal.add(botaoCarregar);
		
		return botaoCarregar;
	}
	
	private JButton prepararBotaoFechar() {
		JButton botaoFechar = new JButton("Fechar");
		botaoFechar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		return botaoFechar;
	}
	
	public void carregarTabelaDataCenters(Object[][] data) {
		table = new JTable(data, new String [] {"#", "Nome", "Status"});
		
		painelAuxiliar.add(table);
		painelAuxiliar.doLayout();
		
		System.out.println("Tabel cell = " + table.getModel().getValueAt(0, 0));
	}
	
	public void alterarStatusTableDataCenter(int dataCenterId, String status) {
		int rowCount = table.getModel().getRowCount();
		for(int i = 0; i < rowCount; i++){
			if((int)table.getModel().getValueAt(i, 0) == dataCenterId){
				table.getModel().setValueAt(status, i, 2);
				table.repaint();
			}
		}
	}
	
	private JButton preparaBotaoSair() {
		JButton botaoSair = new JButton("Parar");
		botaoSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
		      //System.exit(0);
				Principal.getInstance().stopSimulation();
		    }
		});
		//painelPrincipal.add(botaoSair);
		
		return botaoSair;
	}
	
	public void addLog(String txt) {
		logTextArea.append(txt + newline);
		logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
	}
}
