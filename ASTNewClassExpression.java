class ASTNewClassExpression extends ASTExpression {

    public ASTNewClassExpression(String type, int line) {
	type_ = type;
	line_ = line;
    }

    public String type() {
	return type_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_ = line;
    }
    
    public void settype(String type) {
	type_ = type;
    }
    
    public Object Accept(ASTVisitor V) {
	return V.VisitNewClassExpression(this);
    }

    private String type_;
    private int line_;
}
