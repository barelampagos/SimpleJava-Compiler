class AATMove extends AATStatement {

    public AATMove(AATExpression lhs, AATExpression rhs) {
	lhs_ = lhs;
	rhs_ = rhs;
    }

    public AATExpression lhs() {
	return lhs_;
    }

    public AATExpression rhs() {
	return rhs_;
    }

    public void setlhs(AATExpression lhs) {
	lhs_ = lhs;
    }

    public void setrhs(AATExpression rhs) {
	rhs_ = rhs;
    }

    public Object Accept(AATVisitor V) {
	return V.VisitMove(this);
    }
   
    private AATExpression lhs_;
    private AATExpression rhs_;

}
