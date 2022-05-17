package Datastore;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import PackageAnagrafica.Cliente;
import Prodotti.Carta;
import Prodotti.CartaDiCredito;
import Prodotti.ContoBanca;
import Prodotti.Prepagata;

//@Component
//@Primary
public class DataStoreDB extends DataBaseContiABS {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String USER_ID = "root";
	private static final String PASSWORD = "secret";

	private static Connection conn;

	public DataStoreDB() throws IOException {
		loadDriver();
		try {
		     conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Banca",
			 USER_ID, PASSWORD);
		/*	Properties props = new Properties();
			FileInputStream file = new FileInputStream("properties.bat");
			props.load(file);
			MysqlDataSource mysqlDS = new MysqlDataSource();
			mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
			mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
			mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));

			DataSource ds = mysqlDS;
			conn = ds.getConnection();*/

		} catch (SQLException e) {
			System.out.println("Connessione al DB non riuscita");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	public void loadDriver() {
		try {
			Class<?> driverMysql = Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver non presente");
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			System.out.println("Chiusura fallita");
			e.printStackTrace();
		}
	}

	@Override
	public Carta getConto(String cf) {

		try {

			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Banca.datastore WHERE codice_fiscale =?");
			stmt.setString(1, cf);
			ResultSet r = stmt.executeQuery();
			while (r.next()) {
				String codicef = r.getString("codice_fiscale");
				String nome = r.getString("nome");
				String cognome = r.getString("cognome");
				String sceltaB = r.getString("sceltaB");
				String iban = r.getString("iban");
				double saldo = r.getDouble("saldo");
				double fido = r.getDouble("fido");

				Cliente c = createClient(codicef, nome, cognome, sceltaB);

				if (fido > 0) {
					return new CartaDiCredito(c, iban, saldo, fido);
				}
				if (iban == null) {
					return new Prepagata(c, fido);
				}
				return new ContoBanca(c, iban, saldo);
			}
		} catch (SQLException e) {
			System.out.println("Sintassi SQL getConto errato");
			e.printStackTrace();
		}
		return null;

	}

	private Cliente createClient(String codicef, String nome, String cognome, String sceltaB) {
		Cliente c = new Cliente();
		c.setCf(codicef);
		c.setNome(nome);
		c.setCognome(cognome);
		c.setSceltaB(sceltaB);
		return c;
	}

	@Override
	public void chiudiConto(String cf) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM Banca.datastore WHERE codice_fiscale =?");
			stmt.setString(1, cf);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// dato un CF restituisce il saldo disponibile
	public double getDisponibilita(String cf) {
		double saldo = 0;
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT saldo FROM Banca.datastore WHERE codice_fiscale =?");
			stmt.setString(1, cf);

			ResultSet r = stmt.executeQuery();
			while (r.next()) {
				saldo = r.getDouble("saldo");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return saldo;
	}

	@Override
	public void salvaConto(Carta conto) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO Banca.datasore " + "(codice_fiscale,nome,cognome,sceltaB,saldo) VALUES (?,?,?,?,?)");
			stmt.setString(1, conto.getCliente().getCf());
			stmt.setString(2, conto.getCliente().getNome());
			stmt.setString(3, conto.getCliente().getCognome());
			stmt.setString(4, conto.getCliente().getSceltaB());
			stmt.setDouble(5, conto.getSaldo());

			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void persistMovimento(String cf, String azione, double saldo) {
		try {
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO Banca.movimenti (codice_fiscale,azione,saldo) VALUES (?,?,?)");
			stmt.setString(1, cf);
			stmt.setString(2, azione);
			stmt.setDouble(3, saldo);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeConnection();
	}

	public void updateConto(Carta conto) {
		String azione = "preleva/versa";
		try {
			conn.setAutoCommit(false);

			PreparedStatement stmt = conn
					.prepareStatement("UPDATE Banca.datastore SET saldo=? WHERE codice_fiscale =? ");
			stmt.setDouble(1, conto.getSaldo());
			stmt.setString(2, conto.getCliente().getCf());
			stmt.executeUpdate();

			persistMovimento(conto.getCliente().getCf(), azione, conto.getSaldo());

			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
					System.out.println("Rollback effettauto");
				} catch (Exception e1) {
					System.out.println("impossibile rollback");
					System.out.println("Eccezione in updateConto");
					e1.printStackTrace();
				}
			}
		} finally {
			if (conn != null)
				System.out.println("Operazione OK");
			
		}
	}

	@Override
	public Iterator<Carta> iterator() {
		return null;
	}
/*
	@Override
	public void bonifico(String cf1, String cf2, double ris) {
		String azione = "bonifico";

		try {
			conn.setAutoCommit(false);

			PreparedStatement stmt = conn
					.prepareStatement("UPDATE Banca.datastore SET saldo=? WHERE codice_fiscale =? ");
			stmt.setDouble(1, getConto(cf1).getSaldo() - ris);
			stmt.setString(2, cf1);
			stmt.executeUpdate();// reset dell'update

			stmt.setDouble(1, getConto(cf2).getSaldo() + ris);
			stmt.setString(2, cf2);
			stmt.executeUpdate();

			persistMovimento(cf1, azione, ris);
			persistMovimento(cf2, azione, ris);

			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
					System.out.println("Rollback effettauto");
				} catch (Exception e1) {
					System.out.println("impossibile rollback");
					System.out.println("Eccezione in Bonifico");
					e1.printStackTrace();
				}
			}
		} finally {
			if (conn != null)
				System.out.println("bonifico OK");
			
		}
	}
*/
	@SuppressWarnings("unused")
	@Override
	public List<Movimento> estrattoConto(String cf) {

		List<Movimento> move = new ArrayList<Movimento>();

		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Banca.movimenti WHERE codice_fiscale =?");

			stmt.setString(1, cf);
			ResultSet r = stmt.executeQuery();
/*
			while (r.next()) {
				Movimento e = extractEntity(r);
				move.add(e);
			}*/

		} catch (SQLException e) {
			System.out.println("Errore in lista movimenti");
			e.printStackTrace();
		}
		return move;
	}

	/*private Movimento extractEntity(ResultSet r) throws SQLException {
		String codicef = r.getString("codice_fiscale");
		String azione = r.getString("azione");
		double saldo = r.getDouble("saldo");

		Movimento e = new Movimento(codicef, azione, saldo);
		return e;
	}*/

	@Override
	public void bonifico(Carta carta1, Carta carta2, double saldo) {
		
	}
}