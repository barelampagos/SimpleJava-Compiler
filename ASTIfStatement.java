public class ASTIfStatement extends ASTStatement {

    public ASTIfStatement(ASTExpression test, ASTStatement thenstatement,  
                          ASTStatement elsestatement, int line) {
	test_ = test;
	thenstatement_ = thenstatement;
	elsestatement_ = elsestatement;
	line_ = line;
    }

    public ASTExpression test() {
	return test_;
    }

    public ASTStatement thenstatement() {
	return thenstatement_;
    }
    public ASTStatement elsestatement() {
	return elsestatement_;
    }

    public void settest(ASTExpression test) {
	test_ = test;
    }

    public void setthenstatement(ASTStatement thenstatement) {
	thenstatement_ = thenstatement;
    }

    public void setelsestatement(ASTStatement elsestatement) {
	elsestatement_ = elsestatement;
    }


    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_= line;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitIfStatement(this);
    }

    ASTExpression test_;
    ASTStatement thenstatement_;
    ASTStatement elsestatement_;
    int line_;
}


