import java.util.Vector;

class FunctionEntry {

    public FunctionEntry(Type result, Vector formals) {
	result_ = result;
	formals_ = formals;
	startlabel_ = null;
	endlabel_ = null;
    }


    public FunctionEntry(Type result, Vector formals, Label startlabel, Label endlabel) {
	result_ = result;
	formals_ = formals;
	startlabel_ = startlabel;
	endlabel_ = endlabel;
    }

    public Type result() {
	return result_;
    }

    public Vector formals() {
	return formals_;
    }

    public Label startlabel() {
	return startlabel_;
    }
    public Label endlabel() {
	return endlabel_;
    }


    public void setresult(Type result) {
	result_ = result;
    }

    public void setformals(Vector formals) {
	formals_ = formals;
    }

    public void setstartlabel(Label startlabel) {
       startlabel_ = startlabel;
    }

    public void setendlabel(Label endlabel) {
       endlabel_ = endlabel;
    }

    private Type result_;
    private Vector formals_;
    private Label startlabel_;
    private Label endlabel_;
}
