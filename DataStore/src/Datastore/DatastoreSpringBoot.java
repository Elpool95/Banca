package Datastore;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PackageAnagrafica.Cliente;
import Prodotti.Carta;
import Prodotti.ContoBanca;
import Prodotti.Prepagata;

@Component
@Primary
@Service
public class DatastoreSpringBoot extends DataBaseContiABS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private RepoCarte repocarte;
	@Autowired
	private RepoCliente repocliente;
	@Autowired
	private RepoMovimenti repomovimenti;
	@Autowired
	private RepoPrepagata repoprepagata;

	@Override
	public Carta getConto(String cf) throws ClassNotFoundException, IOException {
		// Carta conto1 = repocarte.findById(cf).get();
		Carta conto1 = repocarte.findByCf(cf);
		if (conto1 != null)
			return conto1;

		Carta conto = repoprepagata.findByCf(cf);
		if (conto != null)
			return conto;

		return null;
	}

	@Override
	@Transactional
	public void chiudiConto(String cf) {
		repocliente.deleteById(cf);
		repocarte.deleteByCf(cf);
		repoprepagata.deleteByCf(cf);
	}

	@Override
	public double getDisponibilita(String cf) {
		double saldo = 0;
		Carta conto = repocarte.findByCf(cf);
		if (conto != null)
			saldo = conto.getSaldo();
		Carta conto1 = repoprepagata.findByCf(cf);
		if (conto1 != null)
			saldo = conto.getSaldo();
		return saldo;
	}

	@Override
	public void salvaConto(Carta conto) {
		if (conto instanceof ContoBanca) {
			repocliente.save(conto.getCliente());
			repocarte.save(conto);
		}

		else if (conto instanceof Prepagata) {
			repocliente.save(conto.getCliente());
			repoprepagata.save(conto);// da testare
		}

	}

	@Override
	@Transactional // apre sessione transazione commit e gestisce rollback
	public void updateConto(Carta conto) {
		String azione = "preleva/versa";
		salvaConto(conto);
		persistMovimento(conto.getCliente(), azione, conto.getSaldo());
	}

	@Override
	 @Transactional
	public void bonifico(Carta carta1, Carta carta2, double saldo) {
		String azione = "bonifico";

		carta1.setSaldo(carta1.getSaldo() - saldo);

		carta2.setSaldo(carta2.getSaldo() + saldo);

		salvaConto(carta1);
		salvaConto(carta2);

		persistMovimento(carta1.getCliente(), azione, saldo);
		persistMovimento(carta2.getCliente(), azione, saldo);

	}

	
	public void persistMovimento(Cliente cliente, String azione, double saldo) {

		Movimento e = new Movimento(cliente, azione, saldo);

	
		repomovimenti.save(e);
		actional,

	}

	@Override
	public List<Movimento> estrattoConto(String cf) {
		List<Movimento> move;
		move = repomovimenti.findByCf(cf);
		return move;
	}

	@Override
	public Iterator<Carta> iterator() {
		throw new UnsupportedOperationException();
	}
}
