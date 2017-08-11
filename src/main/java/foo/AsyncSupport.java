package foo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.enterprise.inject.Instance;

public class AsyncSupport {

    public static <T> CompletionStage<T> getAsync(Instance<T> instance) {
        return getAsync(instance, null);
    }

    public static <T> CompletionStage<T> getAsync(Instance<T> instance, Executor executor) {
        return executor != null ? CompletableFuture.supplyAsync(() -> instance.get(), executor) : CompletableFuture.supplyAsync(() -> instance.get());

    }

}
