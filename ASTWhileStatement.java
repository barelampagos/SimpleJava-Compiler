public class ASTWhileStatement extends ASTStatement {
    public ASTWhileStatement(ASTExpression test, ASTStatement body, int line) {
	test_ = test;
	body_ = body;
	line_ = line;
    }

    public ASTExpression test() {
	return test_;
    }

    public ASTStatement body() {
	return body_;
    }

    public int line() {
	return line_;
    }

    public void setline(int line) {
	line_ = line;
    }


    public void settest(ASTExpression test) {
	test_ = test;
    }

    public void setbody(ASTStatement body) {
	body_ = body;
    }

    public Object Accept(ASTVisitor V) {
	return V.VisitWhileStatement(this);
    }

    private ASTExpression test_;
    private ASTStatement body_;
    private int line_;
}
