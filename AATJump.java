class AATJump extends AATStatement {

    public AATJump(Label label) {
	label_ = label;
    }

    public Label label() {
	return label_;
    }

    public void setlabel(Label label) {
	label_ = label;
    }

    private Label label_;

    public Object Accept(AATVisitor V) {
	return V.VisitJump(this);
    }
}
