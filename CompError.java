public class CompError {

    private static int numberOfErrors = 0;

    public static void message(int linenum, String errstm) {
	numberOfErrors++;
	System.out.println("Error in line " + linenum + ": "+ errstm);
    }
    
    public static boolean anyErrors() {
	return numberOfErrors > 0;
    }
    
    public static int numberOfErrors() {
	return numberOfErrors;
    }
}

