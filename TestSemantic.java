public class TestSemantic {

    public static void main(String args[]) {
	simplejava parser;
	if (args.length < 1) {
	    System.out.print("Usage: java TestSemantic <filename>");
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
	    SemanticAnalyzer sa = new SemanticAnalyzer();
	    System.out.println("Parsing Successful");
	    prog.Accept(pt);
	    prog.Accept(sa);
	    if (!CompError.anyErrors()) 
		System.out.println("No Semantic Errors");
	} catch (ParseException e) {
	    System.out.println(e.getMessage());
	    System.out.println("Parsing Failed");
	}
    }


}
