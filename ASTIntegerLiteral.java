class ASTIntegerLiteral extends ASTExpression {

    public ASTIntegerLiteral(int value, int line) {
	value_ = value;
	line_ = line;
    }

    public int value() {
	return value_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }

    public void setvalue(int value) {
	value_ = value;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitIntegerLiteral(this);
    }

    private int value_;
    private int line_;
}
