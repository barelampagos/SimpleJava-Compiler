public class ASTReturnStatement extends ASTStatement {

    public ASTReturnStatement(ASTExpression value, int line) {
	value_ = value;
	line_ = line;
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

    public void setvalue(ASTExpression value) {
	value_ =  value;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitReturnStatement(this);
    }
    
    ASTExpression value_;
    int line_;
}

