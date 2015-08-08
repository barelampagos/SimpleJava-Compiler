public class ASTBaseVariable extends ASTVariable {

    public ASTBaseVariable(String name, int line) {
	name_ = name;
	line_ = line;
    }

    public String name() {
	return name_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }


    public void setname(String name) {
	name_ = name;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitBaseVariable(this);
    }

    private String name_;
    private int line_;
}
