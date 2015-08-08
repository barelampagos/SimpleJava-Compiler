import java.util.Vector;

class ASTProgram {
    public ASTProgram(ASTClasses classes, ASTFunctionDefinitions functiondefinitions, int line) { 
	classes_ = classes;
	functiondefinitions_ = functiondefinitions;
	line_ = line;
    }

    public ASTClasses classes() {
	return classes_;
    }

    public ASTFunctionDefinitions functiondefinitions() {
	return functiondefinitions_;
    }

    public int line() {
	return line_;
    }

    public void setclasses(ASTClasses classes) {
	classes_ = classes;
    }

    public void setfunctiondefinitions(ASTFunctionDefinitions functiondefinitions) {
	functiondefinitions_ = functiondefinitions;
    }

    public void setline(int line) {
	line_ = line;
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitProgram(this);
    }


    private ASTClasses classes_;
    private ASTFunctionDefinitions functiondefinitions_;
    private int line_;
}
