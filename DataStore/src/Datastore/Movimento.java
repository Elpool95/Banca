package Datastore;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import PackageAnagrafica.Cliente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table( name = "movimenti")
public class Movimento {


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_cf")
	private Cliente cliente;

	private String azione;
	private double saldo;
	
	public Movimento() {
	}
	
	public int getId() {
		return id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public Movimento(Cliente cliente, String azione, double saldo) {
		super();
		this.cliente = cliente;
		this.azione = azione;
		this.saldo = saldo;
	}

	@Override
	public String toString() {
		return "Movimento [cliente=" + cliente + ", azione=" + azione + ", saldo=" + saldo + "]";
	}


}
