package Datastore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Prodotti.Carta;

//@Component
public class DataStoreMap extends DataBaseContiABS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected HashMap<String, Carta> conti = new HashMap<>();

	@Override
	public Carta getConto(String cf) {
		return conti.get(cf);
	}

	@Override
	public void chiudiConto(String cf) {
		conti.remove(cf);
	}

	public double getDisponibilita(String cf) {
		for (Carta conto : conti.values()) {
			if (cf.equals(conto.getCliente().getCf())) {
				return conto.getSaldo();
			}
		}
		return 0;
	}

	@Override
	public void salvaConto(Carta conto) {
		conti.put(conto.getCliente().getCf(), conto);
	}

	@Override
	public Iterator<Carta> iterator() {
		return conti.values().iterator();
	}



	@Override
	public void updateConto(Carta conto) {

		throw new UnsupportedOperationException();

	}

	@Override
	public List<Movimento> estrattoConto(String cf) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void bonifico(Carta carta1, Carta carta2, double saldo) {
		throw new UnsupportedOperationException();	
	}

}
