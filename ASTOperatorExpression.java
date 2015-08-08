class ASTOperatorExpression extends ASTExpression {

    public static final int BAD_OPERATOR = 0;
    public static final int PLUS = 1;
    public static final int MINUS = 2;
    public static final int MULTIPLY = 3;
    public static final int DIVIDE = 4;
    public static final int AND = 5;
    public static final int OR = 6;
    public static final int EQUAL = 7;
    public static final int NOT_EQUAL = 8;
    public static final int LESS_THAN = 9;
    public static final int LESS_THAN_EQUAL = 10;
    public static final int GREATER_THAN = 11;
    public static final int GREATER_THAN_EQUAL = 12;

    public static final String [] names = {"BAD_OPERATOR", "+","-","*","/","&&","||",
					   "==","!=","<","<=",">",">="}; 
    
	

    public ASTOperatorExpression(ASTExpression left,
				 ASTExpression right,
				 int operator, int line) {
	left_ = left;
	right_ = right;
	operator_ = operator;
	line_ = line;
    }

    public ASTOperatorExpression(ASTExpression left,
				 ASTExpression right,
				 String operator, int line) {
	left_ = left;
	right_ = right;
	line_ = line;
	if (operator.compareTo("+") == 0) 
	    operator_ = PLUS;
	else if (operator.compareTo("-") == 0) 
	    operator_ = MINUS;
	else if (operator.compareTo("*") == 0) 
	    operator_ = MULTIPLY;
	else if (operator.compareTo("/") == 0) 
	    operator_ = DIVIDE;
	else if (operator.toUpperCase().compareTo("&&") == 0) 
	    operator_ = AND;
	else if (operator.toUpperCase().compareTo("||") == 0) 
	    operator_ = OR;
	else if (operator.compareTo("==") == 0) 
	    operator_ = EQUAL;
	else if (operator.compareTo("<") == 0) 
	    operator_ = LESS_THAN;
	else if (operator.compareTo("<=") == 0) 
	    operator_ = LESS_THAN_EQUAL;
	else if (operator.compareTo(">") == 0) 
	    operator_ = GREATER_THAN;
	else if (operator.compareTo(">=") == 0) 
	    operator_ = GREATER_THAN_EQUAL;
	else if (operator.compareTo("!=") == 0) 
	    operator_ = NOT_EQUAL;
	else 
	    operator_ = BAD_OPERATOR;
    }

    public ASTExpression left() {
	return left_;
    }

    public int line() {
	return line_;
    }

    public void setlinenun(int line) {
	line_ = line;
    }

    public ASTExpression right() {
	return right_;
    }
    
    public int operator() {
	return operator_;
    }


    public void setleft(ASTExpression left) {
	left_ = left;
    }

    public void setright(ASTExpression right) {
	right_ = right;
    }
    
    public void setoperator(int operator) {
	operator_ = operator;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitOperatorExpression(this);
    }


    ASTExpression left_;
    ASTExpression right_;
    int operator_;
    private int line_;
}
