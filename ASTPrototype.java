import java.util.Vector;

class ASTPrototype extends ASTFunctionDefinition {

    public ASTPrototype(String type, String name, ASTFormals formals, int line) {
	type_ = type;
	name_ = name;
	formals_ = formals;
	line_ = line;
    }

    public String name() {
	return name_;
    }
    public String type() {
	return type_;
    }

    public ASTFormals formals() {
	return formals_;
    }

    public int line() {
	return line_;
    }

    public void setname(String name) {
	name_ = name;
    }
   
    public void setformals(ASTFormals formals) {
	formals_ = formals;
    }

    public void setline(int line) {
	line_ = line;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitPrototype(this);
    }

    private String type_;
    private String name_;
    private ASTFormals formals_;
    private int line_;

}




