class ASTNewArrayExpression extends ASTExpression {

    public ASTNewArrayExpression(String type, ASTExpression elements, int arraydimension, int line) {
	type_ = type;
	elements_ = elements;
	arraydimension_ = arraydimension;
	line_ = line;
    }

    public ASTNewArrayExpression(String type, ASTExpression elements, int line) {
	type_ = type;
	elements_ = elements;
	arraydimension_ = 0;
	line_ = line;
    }

    public String type() {
	return type_;
    }

    public ASTExpression elements() {
	return elements_;
    }

    public int line() {
	return line_;
    }

    public int arraydimension() {
	return arraydimension_;
    }

    public void setarraydimension(int arraydimension) {
	arraydimension_ = arraydimension;
    }

    public void setline(int line) {
	line_ = line;
    }
    
    public void settype(String type) {
	type_ = type;
    }
    
    public void setelements(ASTExpression elements) {
	elements_ = elements;
    }
    
    public Object Accept(ASTVisitor V) {
	return V.VisitNewArrayExpression(this);
    }

    private String type_;
    private ASTExpression elements_;
    private int line_;
    private int arraydimension_;
}
