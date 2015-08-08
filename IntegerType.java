public class IntegerType extends Type {

    private IntegerType() {

    }

    public static IntegerType instance() {
	       if (_instance == null) {
	           _instance = new IntegerType();
	       }
	        return _instance;
    }

    static private IntegerType _instance;

}
