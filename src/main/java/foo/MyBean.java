package foo;

import javax.inject.Inject;

public class MyBean {

	@Inject
	private SQLConnection sqlConnection;
	
	public void test() {
		System.err.println("Testing my connection: "+ sqlConnection);
	}

}
