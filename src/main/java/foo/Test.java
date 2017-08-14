package foo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        try (WeldContainer container = new Weld().addExtension(new AsyncExtension()).initialize()) {

            BlockingQueue<Object> synchronizer = new LinkedBlockingQueue<>();

            // Obtain Instance but the actual bean instance creation happens async
            // Note that container.select() may be also blocking
            AsyncSupport.getAsync(container.select(MyBean.class)).thenAccept((myBean) -> {
                // MyBean is ready to use
                myBean.test();
                synchronizer.add(myBean);
            });

            System.out.println("Continue with non-blocking code...");

            SQLConnectionProducer.future.complete(new SQLConnectionImpl("Test!"));

            if (synchronizer.poll(2000, TimeUnit.SECONDS) == null) {
                throw new IllegalStateException("Something bad happened");
            }
        }
    }
}
