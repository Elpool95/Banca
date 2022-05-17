package Datastore;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


import Prodotti.Carta;
import Prodotti.ContoBanca;


//@Repository
public interface RepoCarte extends JpaRepository<ContoBanca, String> {
	
	 @Query(value = "SELECT * FROM contobanca WHERE cliente_cf = ?1",nativeQuery = true)
	 ContoBanca findByCf(String cf);
	 //ContoBanca findByCf(@Param("cf")String cf);

	void save(Carta conto);

	@Modifying
	@Query(value = "DELETE FROM contobanca WHERE cliente_cf =?1",nativeQuery = true)
	void deleteByCf(String cf);

}
