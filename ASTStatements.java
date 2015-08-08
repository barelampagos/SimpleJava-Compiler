import java.util.Vector;

class ASTStatements extends ASTStatement {

    public ASTStatements() {
	data = new Vector(5);
    }
    
    public ASTStatements(ASTStatement statement) {
	data = new Vector(5);
	data.addElement(statement);
    }

    public void addElement(ASTStatement statement) {
	data.addElement(statement);
    }

    public ASTStatement elementAt(int n) {
	return (ASTStatement) data.elementAt(n);
    }


    public int size() {
	return data.size();
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitStatements(this);
    }

    private Vector data;
}
