import java.util.Vector;

class ASTFormals {

    public ASTFormals() {
	data = new Vector(5);
    }
    
    public ASTFormals(ASTFormal formal) {
	data = new Vector(5);
	data.addElement(formal);
    }

    public void addElement(ASTFormal formal) {
	data.addElement(formal);
    }

    public ASTFormal elementAt(int n) {
	return (ASTFormal) data.elementAt(n);
    }


    public int size() {
	return data.size();
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitFormals(this);
    }

    private Vector data;
}
