package Datastore;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//@Repository
public interface RepoMovimenti extends JpaRepository<Movimento, String> {

	@Query(value = "SELECT id,azione,saldo,cliente_cf FROM movimenti  WHERE cliente_cf =:cf", nativeQuery = true)
	List<Movimento> findByCf(@Param("cf") String cf);


}
