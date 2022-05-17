package Datastore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import PackageAnagrafica.Cliente;
import Prodotti.Carta;
import Prodotti.ContoBanca;
import Prodotti.Prepagata;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//@Component
//@Primary
public class DataStoreORM extends DataBaseContiABS {
	/**
	 *  
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger log = LogManager.getLogger(DataStoreORM.class);
	private SessionFactory sf;
	private Session session;

	public DataStoreORM() {
		sf = new Configuration().configure().buildSessionFactory();// funziona
		session = sf.openSession();
	}

	public void closeSession() {
		try {
			session.close();
		} catch (Exception e) {
			log.error("Chiusura fallita");
			e.printStackTrace();
		}
	}

	@Override
	public Carta getConto(String cf) throws HibernateException {
		try {
			try {
				String hq2 = "select DTYPE,iban,saldo,fido,cliente_cf from contobanca where cliente_cf =:cf";
				ContoBanca conto1 = session.createNativeQuery(hq2, ContoBanca.class).setParameter("cf", cf)
						.getSingleResult();

				if (conto1 != null) {
					return conto1;
				}
			} catch (Exception e) {
				log.info("ContoBanca o CartadiCredito non presente");
			}
			try {
				String hql = "select idCard,saldo,cliente_cf from prepagata  where cliente_cf =:cf";
				Prepagata conto = session.createNativeQuery(hql, Prepagata.class).setParameter("cf", cf)
						.getSingleResult();

				if (conto != null) {
					return conto;
				}
			} catch (Exception e) {
				log.info("prepagata non presente");

			}
		} catch (Exception e) {
			log.error("getConto in errore");
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public void chiudiConto(String cf) {

		Transaction transaction = session.beginTransaction();

		session.delete(session.find(Carta.class, cf));

		transaction.commit();

	}

	// dato un CF restituisce il saldo disponibile
	public double getDisponibilita(String cf) {
		double saldo = 0;

		Carta conto = session.find(Carta.class, cf);
		saldo = conto.getSaldo();

		return saldo;
	}

	@Override
	public void salvaConto(Carta conto) {
		try {
			Transaction transaction = session.beginTransaction();
			session.persist(conto);
			transaction.commit();
			// session2.buildLockRequest(LockOptions.NONE).lock(message2);
		} catch (Exception e) {
			log.error("insert in tabella non funzionante");
			e.printStackTrace();
		}
	}

	public void persistMovimento(Cliente cliente, String azione, double saldo) {

		// Transaction transaction = session.beginTransaction();
		Movimento e = new Movimento(cliente, azione, saldo);
		e.setCliente(cliente);
		e.setAzione(azione);
		e.setSaldo(saldo);

		session.persist(e);

		// transaction.commit();

	}

	public void updateConto(Carta conto) {
		String azione = "preleva/versa";

		Transaction transaction = session.beginTransaction();

		session.update(conto);

		persistMovimento(conto.getCliente(), azione, conto.getSaldo());
		transaction.commit();

	}

	@Override
	public Iterator<Carta> iterator() {
		return null;
	}

	@Override
	public void bonifico(Carta conto1, Carta conto2, double ris) {
		String azione = "bonifico";
		Transaction transaction = null;
		try {

			transaction = session.beginTransaction();

			conto1.setSaldo(conto1.getSaldo() - ris);

			conto2.setSaldo(conto2.getSaldo() + ris);

			session.update(conto1);
			session.update(conto2);

			persistMovimento(conto1.getCliente(), azione, ris);
			persistMovimento(conto2.getCliente(), azione, ris);

			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
				log.error("rollback");
			}
			log.error("Bonifico exception");
			e.printStackTrace();
		}finally {
			session.close();
		}

	}

	@Override
	public List<Movimento> estrattoConto(String cf) {
		List<Movimento> move = new ArrayList<>();
		try {

			String hql = "SELECT id,azione,saldo,cliente_cf FROM movimenti  WHERE cliente_cf = :cf";

			move = session.createNativeQuery(hql, Movimento.class).setParameter("cf", cf).list();// .getResultList();

		} catch (Exception e) {
			log.error("estratto in error");
			e.printStackTrace();
		}
		return move;
	}
}
