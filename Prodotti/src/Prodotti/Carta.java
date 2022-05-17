package Prodotti;

import java.io.Serializable;



import PackageAnagrafica.Cliente;

public interface Carta extends Serializable {

	public Cliente getCliente();

	public double getSaldo();

	public void setSaldo(double ris);

	public double getFido();

	public void setFido(double ris);

	public String getIban();

	public void setCliente(Cliente cliente);

}
