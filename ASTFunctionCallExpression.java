import java.util.Vector;

public class ASTFunctionCallExpression extends ASTExpression {

    public ASTFunctionCallExpression(String name, int line) {
	data = new Vector(5);
	name_ = name;
	line_ = line;
    }


    public ASTFunctionCallExpression(String name, ASTExpression formal, int line) {
	data = new Vector(5);
	name_ = name;
	line_ = line;
	data.addElement(formal);
    }

    public String name() {
	return name_;
    }

    public void setname(String name) {
	name_ = name;
    }

    public void addElement(ASTExpression e) {
	data.addElement(e);
    }

    public int size() {
	return data.size();
    }

    public ASTExpression elementAt(int n) {
	return (ASTExpression) data.elementAt(n);
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }
    


    public Object Accept(ASTVisitor V) {
	return V.VisitFunctionCallExpression(this);
    }

    
    private String name_;
    private Vector data;
    private int line_;
}
