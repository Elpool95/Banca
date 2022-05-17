package Datastore;

import java.io.IOException;
import java.io.Serializable;


import Prodotti.Carta;

public abstract class DataBaseContiABS implements DataBaseContiInterface, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void aggiungiConto(Carta conto) {
		try {
			if (getConto(conto.getCliente().getCf()) == null)
				salvaConto(conto);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

}
