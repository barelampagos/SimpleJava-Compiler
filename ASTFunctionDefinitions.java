import java.util.Vector;

class ASTFunctionDefinitions {

    public ASTFunctionDefinitions() {
	data = new Vector(5);
    }
    
    public ASTFunctionDefinitions(ASTFunctionDefinition functiondefinition) {
	data = new Vector(5);
	data.addElement(functiondefinition);
    }

    public void addElement(ASTFunctionDefinition functiondefinition) {
	data.addElement(functiondefinition);
    }

    public ASTFunctionDefinition elementAt(int n) {
	return (ASTFunctionDefinition) data.elementAt(n);
    }


    public int size() {
	return data.size();
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitFunctionDefinitions(this);
    }

    private Vector data;
}
