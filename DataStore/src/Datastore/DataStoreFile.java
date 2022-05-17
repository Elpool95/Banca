package Datastore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import Prodotti.Carta;

//@Component
public class DataStoreFile extends DataBaseContiABS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected HashMap<String, Carta> conti = new HashMap<>();

	public DataStoreFile() {
		readFile();
	}

	public void writeFile() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("Data.out"));
			out.writeObject(conti);
			out.close();
		} catch (IOException e) {
			System.out.println("Errore salvataggio file");
			e.printStackTrace();
		}

	}

	@SuppressWarnings({ "unchecked" })
	private void readFile() {

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("Data.out"));
			conti = (HashMap<String, Carta>) in.readObject();
			in.close();

		} // fuori dal try
		catch (FileNotFoundException e) {
			System.out.println("Banca vuota File");
		}
		catch (ClassNotFoundException|IOException e) {
			System.out.println("Errore caricamento file");
			e.printStackTrace();
		}

	}

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


	public void persist() {
		writeFile();
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
