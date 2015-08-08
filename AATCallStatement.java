import java.util.Vector;

class AATCallStatement extends AATStatement {

    public AATCallStatement(Label label, Vector actuals) {
	label_ = label;
	actuals_ = actuals;
    }

    public Vector actuals() {
	return actuals_;
    }

    public Label label() {
	return label_;
    }

    public void setactuals(Vector actuals) {
	actuals_ = actuals;
    }

    public void setlabel(Label label) {
	label_ = label;
    }

    public Object Accept(AATVisitor V) {
	return V.VisitCallStatement(this);
    }


    private Label label_;
    private Vector actuals_;
}
