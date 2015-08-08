import java.util.Vector;

class ASTInstanceVariableDefs {
    
    public ASTInstanceVariableDefs() {
	data = new Vector(5);
    }

    public ASTInstanceVariableDefs(ASTInstanceVariableDef variabledef) {
	data = new Vector(5);
	data.addElement(variabledef);
    }
    
    public void addElement(ASTInstanceVariableDef variabledef) {
	data.addElement(variabledef);
    }

    public ASTInstanceVariableDef elementAt(int n) {
	return (ASTInstanceVariableDef) data.elementAt(n);
    }

    public int size() {
	return data.size();
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitInstanceVariableDefs(this);
    }

    private Vector data;
}
