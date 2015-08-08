class AATConstant extends AATExpression {

    public AATConstant(int value) {
	value_ = value;
    }

    public int value() {
	return value_;
    }

    public void setvalue(int value) {
	value_ = value;
    }

    private int value_;

    public Object Accept(AATVisitor V) {
	return V.VisitConstant(this);
    }
}
