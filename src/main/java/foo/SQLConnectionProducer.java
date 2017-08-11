package foo;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class SQLConnectionProducer {

    @Produces
    private SQLConnection getSQL() throws InterruptedException{
        // simulate expensive instance creation
        Thread.sleep(100);
        return new SQLConnectionImpl("Voila!");
    }
}
