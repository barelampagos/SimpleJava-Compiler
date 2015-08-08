class ASTBooleanLiteral extends ASTExpression {

    public ASTBooleanLiteral(boolean value, int line) {
	value_ = value;
	line_ = line;
    }

    public boolean value() {
	return value_;
    }


    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }


    public void setvalue(boolean value) {
	value_ = value;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitBooleanLiteral(this);
    }

    boolean value_;
    int line_;
}
