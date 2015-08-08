public class ASTUnaryOperatorExpression extends ASTExpression {

    public static final int BAD_OPERATOR = 0;
    public static final int NOT = 1;

    public static final String [] names = {"BAD_OPERATOR", "!"};
    
	
    public ASTUnaryOperatorExpression(ASTExpression operand,
				      int operator, 
				      int line) {
	operator_ = operator;
	operand_ = operand;
	line_ = line;
    }

    public ASTUnaryOperatorExpression(ASTExpression operand,
				      String operator, 
				      int line) {
	operand_ = operand;
	if (operator.compareTo("!") == 0) 
	    operator_ = NOT;
	else 
	    operator_ = BAD_OPERATOR;
    }

    public ASTExpression operand() {
	return operand_;
    }

    public int operator() {
	return operator_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_ = line;
    }

    public void setoperand(ASTExpression operand) {
	operand_ = operand;
    }

    
    public void setoperator(int operator) {
	operator_ = operator;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitUnaryOperatorExpression(this);
    }


    private ASTExpression operand_;
    private int operator_;
    private int line_;
}
