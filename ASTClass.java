import java.util.Vector;

class ASTClass {
    public ASTClass(String name, ASTInstanceVariableDefs variabledefs, int line) { 
	name_ = name;
	variabledefs_ = variabledefs;
	line_ = line;
    }

    public ASTInstanceVariableDefs variabledefs() {
	return variabledefs_;
    }

    public int line() {
	return line_;
    }

    public String name() {
	return name_;
    }

    public void setname(String name) {
	name_ = name;
    }

    public void setvariabledefs(ASTInstanceVariableDefs variabledefs) {
	variabledefs_ = variabledefs;
    }

    public void setline(int line) {
	line_ = line;
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitClass(this);
    }

    private String name_;
    private ASTInstanceVariableDefs variabledefs_;
    private int line_;

}
