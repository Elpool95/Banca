package Datastore;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import Prodotti.Carta;


public interface DataBaseContiInterface extends Iterable<Carta>,Serializable{

	Carta getConto(String cf) throws ClassNotFoundException, IOException;

	void chiudiConto(String cf);

	double getDisponibilita(String cf);

	void salvaConto(Carta conto);

	void aggiungiConto(Carta conto);

	void updateConto(Carta conto);

	void bonifico(Carta carta1, Carta carta2, double saldo);
	
	List<Movimento> estrattoConto(String cf);

}
