package Datastore;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Prodotti.Carta;
import Prodotti.Prepagata;

//@Repository
public interface RepoPrepagata extends JpaRepository<Prepagata, String> {
	 @Query(value = "SELECT * FROM prepagata WHERE cliente_cf =:cf",nativeQuery = true)
	 Prepagata findByCf(@Param("cf")String cf);

	void save(Carta conto);

	@Modifying
	@Query(value = "DELETE FROM prepagata WHERE cliente_cf =:cf",nativeQuery = true)
	void deleteByCf(@Param("cf")String cf);
	

}
