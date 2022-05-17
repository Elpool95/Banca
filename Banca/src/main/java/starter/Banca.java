package starter;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import Datastore.DataBaseContiInterface;
import Datastore.Movimento;
import PackageAnagrafica.Cliente;
import Prodotti.Carta;
import Prodotti.CartaDiCredito;
import Prodotti.ContoBanca;
import Prodotti.Prepagata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Component
@AllArgsConstructor
@Getter
@Setter
public class Banca implements Iterable<Carta>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(Banca.class);


	@Autowired
	private DataBaseContiInterface databaseConti;
	@SuppressWarnings("unused")
	@Autowired(required = false)
	private int nconti = 0;
	@SuppressWarnings("unused")
	private String nomeB;
	@SuppressWarnings("unused")
	@Autowired(required = false)
	private String indrizzo;

	public Banca() {
	}

	public Banca(DataBaseContiInterface databaseConti) {
		this.databaseConti = databaseConti;
	}

	//esegue bonifico da cf1 a cf2
	public void bonifico(String cf1, String cf2, double saldo) throws ClassNotFoundException, IOException {	
		Carta conto = databaseConti.getConto(cf1);
		if (conto.getSaldo() > saldo) 
			databaseConti.bonifico(conto,databaseConti.getConto(cf2), saldo);
	}

	//dato un cf ritorna tutte le transazioni eseguite dal cf
	public void estrattoConto(String cf) {
		List<Movimento> set;
		set =databaseConti.estrattoConto(cf);
		for (Movimento e : set) {
			if(cf.equals(e.getCliente().getCf())) {
				System.out.println(e.toString());			
			}
		}
	}

	// dato un CF, deve poter versare soldi
	public void versa(double amount, String cf) throws ClassNotFoundException, IOException {
		Carta conto = databaseConti.getConto(cf);
		conto.setSaldo(amount + conto.getSaldo());
		databaseConti.updateConto(conto);
	}

	// dato un CF deve poter prelevare soldi
	public void preleva(String cf, double prelevo) throws ClassNotFoundException, IOException {
		Carta conto = databaseConti.getConto(cf);
		double ris = 0;
		ris = conto.getSaldo() - prelevo;
		
		conto.setSaldo(ris);
		databaseConti.updateConto(conto);
	}

	// dato un CF deve poter prelevare soldi
	public CartaDiCredito preleva(CartaDiCredito cdc, double prelevo) {
		double ris = 0;
		ris = cdc.getSaldo() - prelevo;
		if (ris < (0 - cdc.getFido()))
			log.info("Stai superando il fido");
		else if (ris > (0 - cdc.getFido()))
			cdc.setSaldo(ris);
		databaseConti.updateConto(cdc);
		return cdc;
	}

	// dato un CF deve poter chiudere il conto
	public void chiudiConto(String cf) {
		databaseConti.chiudiConto(cf);

	}

	// dato un CF, deve essere possibile sapere saldo
	public double getDisponibilita(String cf) throws ClassNotFoundException, IOException {
		return databaseConti.getConto(cf).getSaldo();
	}

	// dati i parametri in input crea un ContoBancario
	public Carta openConto(String nome, String cognome, String cf, String sceltaB, String iban, double saldo) {
		Cliente c1 = new Cliente(nome, cognome, cf, sceltaB);
		ContoBanca conto = new ContoBanca(c1, iban, saldo);
		databaseConti.aggiungiConto(conto);
		nconti += 1;
		return conto;
	}

	// dati i parametri in input crea una cdc
	public Carta openCdc(String nome, String cognome, String cf, String sceltaB, String iban, double saldo,
			double fido) {
		Cliente c1 = new Cliente(nome, cognome, cf, sceltaB);
		CartaDiCredito cdc = new CartaDiCredito(c1, iban, saldo, fido);
		databaseConti.aggiungiConto(cdc);
		nconti += 1;
		
		return cdc;
	}

	// dati i parametri in input crea una prepagata
	public Carta openPrepagata(String nome, String cognome, String cf, String sceltaB, double saldo) {
		Cliente c1 = new Cliente(nome, cognome, cf, sceltaB);
		Prepagata prepagata = new Prepagata(c1, saldo);
		databaseConti.aggiungiConto(prepagata);
		nconti += 1;
		return prepagata;
	}

	@Override
	public Iterator<Carta> iterator() {
		return databaseConti.iterator();
	}

}
