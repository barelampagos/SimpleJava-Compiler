class AATOperator extends AATExpression {

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
    public static final int NOT = 13;

    public static final String [] names = {"BAD_OPERATOR", "+","-","*","/","&&","||",
					   "==","!=","<","<=",">",">=", "!"}; 
    
	

    public AATOperator(AATExpression left,
		       AATExpression right,
		       int operator) {
	left_ = left;
	right_ = right;
	operator_ = operator;
    }

    public AATExpression left() {
	return left_;
    }

    public AATExpression right() {
	return right_;
    }

    public int operator() {
	return operator_;
    }


    public void setleft(AATExpression left) {
	left_ = left;
    }

    public void setright(AATExpression right) {
	right_ = right;
    }

    public void setoperator(int operator) {
	operator_ = operator;
    }


    public Object Accept(AATVisitor V) {
	return V.VisitOperator(this);
    }

    private AATExpression left_;
    private AATExpression right_;
    private int operator_;
}
