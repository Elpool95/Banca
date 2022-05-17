package PackageAnagrafica;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@SecondaryTable(name = "cliente+conto", pkJoinColumns = @PrimaryKeyJoinColumn(name = "cf"))
//@Column(name = "sesame_seeds", table = "allergens")
//@Component
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name ="cliente")
public class Cliente implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Autowired
	private String cf;
	@Autowired
	private String nome;
	@Autowired
	private String cognome;
	@Autowired
	public String sceltaB;// il cliente sceglie la banca dove aprire conto

	// @embedded embendable significa che non viene creato un tabella ma l'oggetto
	// aggiunge le colonne

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public String getSceltaB() {
		return sceltaB;
	}

	public void setSceltaB(String sceltaB) {
		this.sceltaB = sceltaB;
	}

	public Cliente(String nome, String cognome, String cf, String sceltaB) {
		this.nome = nome;
		this.cognome = cognome;
		this.cf = cf;
		this.sceltaB = sceltaB;
	}

	public Cliente() {

	}

	@Override
	public String toString() {
		return "Cliente [nome=" + nome + ", cognome=" + cognome + ", cf=" + cf + ", sceltaB=" + sceltaB + "]";
	}

}
