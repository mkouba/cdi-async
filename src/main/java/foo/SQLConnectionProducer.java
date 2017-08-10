package foo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.enterprise.inject.Produces;

public class SQLConnectionProducer {
	
	CompletableFuture<SQLConnection> future;
	
	@Produces
	private CompletionStage<SQLConnection> getSQL(){
		CompletableFuture<SQLConnection> future = new CompletableFuture<SQLConnection>();
		// simulate the future being resolved later by saving it and resolving it from the main method
		// later
		this.future = future;
		return future;
	}
}
