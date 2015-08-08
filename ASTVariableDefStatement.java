public class ASTVariableDefStatement extends ASTStatement {

    public ASTVariableDefStatement(String type, String name, int line) {
	type_ = type;
	name_ = name;
	arraydimension_ = 0;
	init_ = null;
	line_ = line;
    }


    public ASTVariableDefStatement(String type, String name, ASTExpression init, int line) {
	type_ = type;
	name_ = name;
	arraydimension_ = 0;
	init_ = init;
	line_ = line;
    }


    public ASTVariableDefStatement(String type, String name, int arraydimension, int line) {
	type_ = type;
	name_ = name;
	arraydimension_ = arraydimension;
	init_ = null;
	line_ = line;
    }

    public ASTVariableDefStatement(String type, String name, int arraydimension, ASTExpression init, int line) {
	type_ = type;
	name_ = name;
	arraydimension_ = arraydimension;
	init_ = init;
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

    public ASTExpression init() {
	return init_;
    }

    public void setline(int line) {
	line_ = line;
    }

    public void setname(String name) {
	name_ = name;
    }
    public void setinit(ASTExpression init) {
	init_ = init;
    }

    public void settype(String type) {
	type_ = type;
    }

    public void setarraydimension(int arraydimension) {
	arraydimension_ = arraydimension;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitVariableDefStatement(this);
    }


    private String name_;
    private String type_;
    private int arraydimension_;
    private int line_;
    private ASTExpression init_;
}
