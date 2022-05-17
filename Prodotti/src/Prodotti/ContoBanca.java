package Prodotti;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import PackageAnagrafica.Cliente;

@NamedNativeQuery(name = "getContoQuery", query = " DTYPE,iban,saldo,fido,cliente_cf FROM contobanca WHERE cliente_cf = ?", resultClass = ContoBanca.class)
//@MappedSuperclass
@Entity
@Table(name ="contobanca")
public class ContoBanca implements Carta {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	@OneToOne(cascade=CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_cf")
	protected Cliente cliente;
	@Id
	protected String iban;
	protected double saldo;

	// aggiungo al cliente iban e saldo
	public ContoBanca(Cliente cliente, String iban, double saldo) {
		super();
		this.cliente = cliente;
		this.setIban(iban);
		this.saldo = saldo;
	}

	public ContoBanca() {
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
	public String toString() {
		return "ContoBanca [cliente=" + cliente + ", IBAN=" + getIban() + ", saldo=" + saldo + "]";
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}


}
