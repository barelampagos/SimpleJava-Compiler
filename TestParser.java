public class TestParser {

    public static void main(String args[]) {
	simplejava parser;
	if (args.length < 1) {
	    System.out.print("Usage: java TestParser <filename>");
	    return;
	} 
	try {  
	    parser = new simplejava(new java.io.FileInputStream(args[0]));
	} catch (java.io.FileNotFoundException e) {
	    System.out.println("File " + args[0] + " not found."); 
	    return;
	} 
	try {
	    ASTProgram prog = parser.program();
	    ASTPrintTree pt = new ASTPrintTree();
	    System.out.println("Parsing Successful");
	    prog.Accept(pt);
	} catch (ParseException e) {
	    System.out.println(e.getMessage());
	    System.out.println("Parsing Failed");
	}
    }


}
