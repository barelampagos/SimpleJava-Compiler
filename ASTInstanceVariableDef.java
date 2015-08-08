public class ASTInstanceVariableDef {

    public ASTInstanceVariableDef(String type, String name, int line) {
	type_ = type;
	name_ = name;
	arraydimension_ = 0;
	line_ = line;
    }


    public ASTInstanceVariableDef(String type, String name, int arraydimension, int line) {
	type_ = type;
	name_ = name;
	arraydimension_ = arraydimension;
	line_ = line;
    }

    public String name() {
	return name_;
    }

    public String type() {
	return type_;
    }
    
    public int line() {
	return line_;
    }

    public int arraydimension() {
	return arraydimension_;
    }


    public void setline(int line) {
	line_ = line;
    }

    public void setname(String name) {
	name_ = name;
    }

    public void settype(String type) {
	type_ = type;
    }

    public void setarraydimension(int arraydimension) {
	arraydimension_ = arraydimension;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitInstanceVariableDef(this);
    }


    private String name_;
    private String type_;
    private int arraydimension_;
    private int line_;
}
