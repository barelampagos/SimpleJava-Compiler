import java.util.Vector;

public class ASTFunction extends ASTFunctionDefinition {
    public ASTFunction(String type, String name, ASTFormals formals,
		       ASTStatement body, int line) {

	type_ = type;
	name_ = name;
	formals_ = formals;
	body_ = body;
	line_ = line;
    }

    public String type() {
	return type_;
    }
    
    public String name() {
	return name_;
    }

    public ASTFormals formals() {
	return formals_;
    }


    public ASTStatement body() {
	return body_;
    }
    
    public int line() {
	return line_;
    }


    public void setline(int line) {
	line_ = line;
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitFunction(this);
    }

    private String type_;
    private String name_;
    private ASTFormals formals_;
    private ASTStatement body_;
    private int line_;
}
