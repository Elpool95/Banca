package starter;



import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableJpaRepositories("Datastore")
@ComponentScan(basePackages = {"starter","Datastore","PackageAnagrafica","Prodotti"})
@EntityScan({"Datastore","Prodotti","PackageAnagrafica"})
@SpringBootApplication
public class Main {
	
	public static void main(String[] args) {
		
		SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);

		builder.headless(false);

		ConfigurableApplicationContext context = builder.run(args);
		BeanFactory factory = context;
		Banca intesa = factory.getBean(Banca.class);


		Finestra finestra = new Finestra(intesa);
		finestra.show();

	}

}
