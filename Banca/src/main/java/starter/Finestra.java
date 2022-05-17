package starter;



import java.awt.Color;
import java.awt.event.TextListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Finestra {


	private static final Logger log = LogManager.getLogger(Finestra.class);
	private Banca banca;
	private String message="CREAZIONE OK";

	JFrame windows = new JFrame("BANCA FINTA");

	String iban =("IT"+ Math.random());


	String nomebanca = "intesa";

	JTextField cf = new JTextField(16);
	JTextField saldo = new JTextField(16);
	JTextField output = new JTextField(16);

	TextListener versaListener;
	TextListener prelevaListener;

	JPanel pannelloContenitore = new JPanel();
	JButton versa = new JButton("Versa");
	JButton preleva = new JButton("preleva");

	JTextField testocf = new JTextField("CF");
	JTextField testosoldi = new JTextField("soldi");

	JButton estratto = new JButton("estratto");
	JTextField cfestra = new JTextField("CF1");
	JTextField cf1estratto = new JTextField(16);
	
	JButton delete = new JButton("delete");
	JTextField cfdel = new JTextField("CF da cancellare ");
	JTextField cfdeltext = new JTextField(16);

	// creazione Bonifico
	JButton bonifico = new JButton("Bonifico");
	JTextField cf1 = new JTextField("CF1");
	JTextField getcf1 = new JTextField(16);
	JTextField cf2 = new JTextField("CF2");
	JTextField getcf2 = new JTextField(16);
	JTextField importo = new JTextField("importo");
	JTextField getimporto = new JTextField(16);
	JTextField outBon = new JTextField(16);

	// creazione apertura conto /cdc/ prepagata
	JTextField nome = new JTextField("nome");
	JTextField cognome = new JTextField("cognome");
	JTextField codicef = new JTextField("codice fiscale");
	JTextField getnome = new JTextField(16);
	JTextField getcognome = new JTextField(16);
	JTextField getcf = new JTextField(16);

	JButton conto = new JButton("Apri conto");
	JButton cdc = new JButton("Apri cdc");
	JButton prep = new JButton("Apri prepagata");

	JTextField output1 = new JTextField(16);

	TextListener creaContoListener;

	public Finestra(Banca banca) {
		nome.setEditable(false);
		cf1.setEditable(false);
		cf2.setEditable(false);
		cognome.setEditable(false);
		codicef.setEditable(false);
		testocf.setEditable(false);
		testosoldi.setEditable(false);
		this.banca =banca;
	}

	@SuppressWarnings("static-access")
	public void show() {

		// versa e preleva
		pannelloContenitore.add(testocf);
		pannelloContenitore.add(cf);
		pannelloContenitore.add(testosoldi);
		pannelloContenitore.add(saldo);
		pannelloContenitore.add(versa);
		pannelloContenitore.add(preleva);
		pannelloContenitore.add(output);

		// Apertura conto cdc e prep
		// spostato a destra di 100px e in basso di 150px
		// larghezza = 100px, altezza = 50px
		nome.setBounds(100, 150, 100, 50);
		pannelloContenitore.add(nome);
		pannelloContenitore.add(getnome);
		pannelloContenitore.add(cognome);
		pannelloContenitore.add(getcognome);
		pannelloContenitore.add(codicef);
		pannelloContenitore.add(getcf);
		pannelloContenitore.add(conto);
		pannelloContenitore.add(cdc);
		pannelloContenitore.add(prep);
		pannelloContenitore.add(output1);

		// implementazione bonifico in finestra
		pannelloContenitore.add(cf1);
		pannelloContenitore.add(getcf1);
		pannelloContenitore.add(cf2);
		pannelloContenitore.add(getcf2);
		pannelloContenitore.add(importo);
		pannelloContenitore.add(getimporto);
		pannelloContenitore.add(bonifico);
		pannelloContenitore.add(outBon);

		//implementazione estratto conto
		pannelloContenitore.add(estratto);
		pannelloContenitore.add(cfestra);
		pannelloContenitore.add(cf1estratto);
		
		
		//implementazione delete
		pannelloContenitore.add(delete);
		pannelloContenitore.add(cfdel);
		pannelloContenitore.add(cfdeltext);
		
		delete.addActionListener(e->{
			banca.chiudiConto(cfdeltext.getText());
		});
		

		estratto.addActionListener(e -> {
			banca.estrattoConto(cf1estratto.getText());
		});

		versa.addActionListener(e -> {
			int soldi = Integer.parseInt(saldo.getText());
			try {
				banca.versa(soldi, cf.getText());
				output.setText("" + banca.getDisponibilita(cf.getText()));
			} catch (ClassNotFoundException | IOException e1) {
				log.error("Finestra-versa-bloccata");
				e1.printStackTrace();
			}

		});

		preleva.addActionListener(e -> {
			int soldi = Integer.parseInt(saldo.getText());
			try {
				banca.preleva(cf.getText(), soldi);
				output.setText("" + banca.getDisponibilita(cf.getText()));
			} catch (ClassNotFoundException | IOException e1) {
				log.error("Finestra-preleva-bloccata");
				e1.printStackTrace();
			}

		});

		bonifico.addActionListener(e -> {
			double soldi = Integer.parseInt(getimporto.getText());
			try {
				banca.bonifico(getcf1.getText(), getcf2.getText(), soldi);
				outBon.setText("BONIFICO OK");
			} catch (ClassNotFoundException |IOException  e1) {
				e1.printStackTrace();}
		});
		conto.addActionListener(e -> {
			banca.openConto(getnome.getText(), getcognome.getText(), getcf.getText(), nomebanca, iban, 0);
			output1.setText(message);

		});

		cdc.addActionListener(e -> {
			banca.openCdc(getnome.getText(), getcognome.getText(), getcf.getText(), nomebanca, iban, 0, 0);
			output1.setText(message);
		});

		prep.addActionListener(e -> {
			banca.openPrepagata(getnome.getText(), getcognome.getText(), getcf.getText(), nomebanca, 0);
			output1.setText(message);
		});

		pannelloContenitore.setBackground(Color.darkGray);
		pannelloContenitore.setBorder(new LineBorder(Color.CYAN));

		preleva.setForeground(Color.red);

		windows.add(pannelloContenitore);
		windows.setSize(800, 500);
		windows.setVisible(true);
		windows.setDefaultCloseOperation(windows.EXIT_ON_CLOSE);
	}

}
