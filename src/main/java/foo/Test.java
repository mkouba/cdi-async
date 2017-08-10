package foo;

import javax.enterprise.inject.spi.CDI;

import org.jboss.weld.environment.se.Weld;

public class Test {
	public static void main(String[] args) {
		Weld weld = new Weld();
		weld.initialize();

		SQLConnectionProducer producer = CDI.current().select(SQLConnectionProducer.class).get();
		// Get the bean, but the future is not yet resolved, so CDI should not return my bean,
		// it should return a CompletionStage<MyBean> which I can register on to wait for my bean
		MyBean bean = CDI.current().select(MyBean.class).get();
		
		// The async value only gets resolved after I'm done asking CDI for my bean
		producer.future.complete(new SQLConnectionImpl("Voila !"));
		
		// now I can test it, but I need CDI to tell me my bean is ready
		bean.test();
	}
}
