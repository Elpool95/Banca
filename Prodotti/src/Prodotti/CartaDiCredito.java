package Prodotti;



import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import PackageAnagrafica.Cliente;

import javax.persistence.InheritanceType;


@Entity
@Table(name ="contobanca")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CartaDiCredito extends ContoBanca { // is-a Carta di credito è un CB

	public CartaDiCredito() {

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double fido;

	@Override
	public double getFido() {
		return fido;
	}

	@Override
	public void setFido(double fido) {
		this.fido = fido;
	}

	public CartaDiCredito(Cliente cliente, String iban, double saldo, double fido) {
		super(cliente, iban, saldo);
		this.fido = fido;
	}

	@Override
	public String toString() {
		return "CartaDiCredito [cliente=" + cliente + ", IBAN=" + iban + ", saldo=" + saldo + ", fido=" + fido + "]";
	}

}
