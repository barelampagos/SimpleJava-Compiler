class ASTVariableExpression extends ASTExpression {

    public ASTVariableExpression(ASTVariable variable, int line) {
	variable_ = variable;
	line_ = line;
    }

    public ASTVariable variable() {
	return variable_;
    }

    public void setvariable(ASTVariable variable) {
	variable_ = variable;
    }
    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_ = line;
    }

    
    public Object Accept(ASTVisitor V) {
	return V.VisitVariableExpression(this);
    }

    private ASTVariable variable_;
    private int line_;
}
