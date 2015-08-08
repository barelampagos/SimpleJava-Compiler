import java.util.Vector;

class ASTClasses {

    public ASTClasses() {
	data = new Vector(5);
    }
    
    public ASTClasses(ASTClass classs) {
	data = new Vector(5);
	data.addElement(classs);
    }

    public void addElement(ASTClass classs) {
	data.addElement(classs);
    }

    public ASTClass elementAt(int n) {
	return (ASTClass) data.elementAt(n);
    }


    public int size() {
	return data.size();
    }


    public Object Accept(ASTVisitor V) {
	return (Object) V.VisitClasses(this);
    }

    private Vector data;
}
