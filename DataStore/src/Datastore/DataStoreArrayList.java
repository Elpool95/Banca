package Datastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Prodotti.Carta;

//@Component
public class DataStoreArrayList extends DataBaseContiABS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected ArrayList<Carta> conti = new ArrayList<>();


	
	public double getDisponibilita(String cf) {

		for (Carta conto : conti) {
			if (cf.equals(conto.getCliente().getCf())) {
				return conto.getSaldo();
			}
		}
		return 0;
	}

	public Carta getConto(String cf) {
		for (Carta conto : conti) {
			if (cf.equals(conto.getCliente().getCf())) {
				return conto;

			}
		}
		return null;
	}

	public void chiudiConto(String cf) {
		for (Carta conto : conti) {
			if (cf.equals(conto.getCliente().getCf())) {
				conti.remove(conto);
			}
		}				
	}

	public void salvaConto(Carta conto) {
		conti.add(conto);

	}

	@Override
	public Iterator<Carta> iterator() {
		return conti.iterator();
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

