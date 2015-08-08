public class TestAssembly {

    public static void main(String args[]) {
	simplejava parser;
	if (args.length < 1) {
	    System.out.print("Usage: java TestAssembly <filename>");
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
	    AATPrintTree pat = new AATPrintTree();
	    SemanticAnalyzer sa = new SemanticAnalyzer();
	    AATInterpreter intr;
	    System.out.println("Parsing Successful");
	    System.out.println("=================");
	    System.out.println("Printing AST ...");
	    System.out.println("=================");
	    prog.Accept(pt);
	    AATStatement assem = (AATStatement) prog.Accept(sa);
	    if (!CompError.anyErrors()) {
		System.out.println("=================");
		System.out.println("Printing AAT ...");
		System.out.println("=================");
		assem.Accept(pat); 
		intr = new AATInterpreter(assem);
		System.out.println("========================");
		System.out.println("Starting Interpreter ...");
		System.out.println("========================");
		intr.run();
		System.out.println("========================");
		System.out.println("Interpreter Done!");
		System.out.println("========================");

	    } else {
		System.out.println("Semantic Errors Detected.  No AAT Created");
	    }
	} catch (ParseException e) {
	    System.out.println(e.getMessage());
	    System.out.println("Parsing Failed");
	}
    }


}
