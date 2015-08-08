import java.util.Vector;

class AATCallExpression extends AATExpression {

    public AATCallExpression(Label label, Vector actuals) {
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

    private Label label_;
    private Vector actuals_;

    public Object Accept(AATVisitor V) {
	return V.VisitCallExpression(this);
    }
}
