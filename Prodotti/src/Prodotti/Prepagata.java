package Prodotti;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import PackageAnagrafica.Cliente;

//@Setter

//@Getter
//@Immutable as shown above,so that it cannot be persisted or updated accidentally.
@Entity
@Table(name ="prepagata")
public class Prepagata  implements  Carta {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long idCard;
	@OneToOne(cascade=CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_cf")
	private Cliente cliente;
	private double saldo;
	
	@Override
	public String toString() {
		return "Prepagata [cliente=" + cliente + ", saldo=" + saldo + "]";
	}

	//aggiungo al cliente iban e saldo 
	public Prepagata(Cliente cliente, double saldo) {
		super();
		this.cliente = cliente;
		this.saldo = saldo;
	}
	public Prepagata() {
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	@Override
	public void setFido(double ris) {

		throw new UnsupportedOperationException();

	}

	@Override
	public double getFido() {

		return 0;
	}


	@Override
	public String getIban() {
		throw new UnsupportedOperationException();
	}

}
