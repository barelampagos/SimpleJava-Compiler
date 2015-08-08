class AATConditionalJump extends AATStatement {

    public AATConditionalJump(AATExpression test, Label label) {
	label_ = label;
	test_ = test;
    }

    public Label label() {
	return label_;
    }

    public AATExpression test() {
	return test_;
    }

    public void settest(AATExpression test) {
	test_ = test;
    }

    public void setlabel(Label label) {
	label_ = label;
    }

    public Object Accept(AATVisitor V) {
	return V.VisitConditionalJump(this);
    }

    private Label label_;
    private AATExpression test_;


}
