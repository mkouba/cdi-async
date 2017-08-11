package foo;

public class SQLConnectionImpl implements SQLConnection {

    private final String foo;

    public SQLConnectionImpl(String foo) {
        this.foo = foo;
    }

    @Override
    public String toString() {
        return "SQLConnectionImpl [foo=" + foo + "]";
    }

}
