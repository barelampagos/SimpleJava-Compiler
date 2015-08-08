public class ASTClassVariable extends ASTVariable {

    public ASTClassVariable(ASTVariable base,
                            String variable, int line) {
	base_ = base;
	variable_ = variable;
	line_ = line;
    }

    public ASTVariable base() {
	return base_;
    }

    public String variable() {
	return variable_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_ = line;
    }

    public void setbase(ASTVariable base) {
	base_ = base;
    }

    public void setvariable(String variable) {
	variable_ = variable;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitClassVariable(this);
    }

    private ASTVariable base_;
    private String variable_;
    private int line_;
}





