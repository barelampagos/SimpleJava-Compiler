public class ASTArrayVariable extends ASTVariable {

    public ASTArrayVariable(ASTVariable base,
                            ASTExpression index,
			    int line) {
	base_ = base;
	index_ = index;
	line_ = line;
    }

    public ASTVariable base() {
	return base_;
    }

    public ASTExpression index() {
	return index_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }

    public void setbase(ASTVariable base) {
	base_ = base;
    }

    public void setindex(ASTExpression index) {
	index_ = index;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitArrayVariable(this);
    }

    ASTVariable base_;
    ASTExpression index_;
    int line_;
}



