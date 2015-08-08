public class sjc {

    public static void main(String args[]) {
	simplejava parser;
	if (args.length < 1) {
	    System.out.print("Usage: java sjc <filename>");
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
	    CodeGenerator cg = new CodeGenerator(args[0] + ".s");
	    System.out.println("Parsing Successful");
	    prog.Accept(pt);
	    AATStatement assem = (AATStatement) prog.Accept(sa);
	    if (!CompError.anyErrors()) {
		assem.Accept(pat); 
		assem.Accept(cg);
		cg.GenerateLibrary();
	    }
	} catch (ParseException e) {
	    System.out.println(e.getMessage());
	    System.out.println("Parsing Failed");
	}
    }


}
