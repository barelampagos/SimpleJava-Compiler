public class ASTEmptyStatement extends ASTStatement {
    
    public ASTEmptyStatement(int line) {
	line_ = line;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }

    private int line_;

    public Object Accept(ASTVisitor V) {
	return V.VisitEmptyStatement(this);
    }

}
