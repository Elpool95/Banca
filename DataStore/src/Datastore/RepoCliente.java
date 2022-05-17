package Datastore;



import org.springframework.data.jpa.repository.JpaRepository;

import PackageAnagrafica.Cliente;


//@Repository
public interface RepoCliente extends JpaRepository<Cliente, String> {

	
}
