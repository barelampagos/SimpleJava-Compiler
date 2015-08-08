public class ASTAssignmentStatement extends ASTStatement {

    public ASTAssignmentStatement(ASTVariable variable, ASTExpression value, int line) {
	variable_ = variable;
	value_ = value;
	line_ = line;
    }

    public ASTVariable variable() {
	return variable_;
    }
    public ASTExpression value() {
	return value_;
    }


    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }


    public void setvariable(ASTVariable variable) {
	variable_ = variable;
    }
    public void setvalue(ASTExpression value) {
	value_ =  value;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitAssignmentStatement(this);
    }
    
    ASTVariable variable_;
    ASTExpression value_;
    int line_;
}

