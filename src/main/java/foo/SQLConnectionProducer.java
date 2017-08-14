package foo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class SQLConnectionProducer {

    static CompletableFuture<SQLConnection> future = new CompletableFuture<>();

    // @Produces
    // private SQLConnection getSQL() throws InterruptedException{
    // // simulate expensive instance creation
    // Thread.sleep(100);
    // return new SQLConnectionImpl("Voila!");
    // }

    @Produces
    private CompletionStage<SQLConnection> getAsyncSQL() {
        return future;
    }

}
