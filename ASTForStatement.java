public class ASTForStatement extends ASTStatement {

    public ASTForStatement(ASTStatement initialize, ASTExpression test,
			   ASTStatement increment, ASTStatement body, int line) {

	initialize_ = initialize;
	test_ = test;
	increment_ = increment;
	body_ = body;
	line_ = line;
    }

    public ASTStatement initialize() {
	return initialize_;
    }

    public ASTExpression test() {
	return test_;
    }

    public ASTStatement increment() {
	return increment_;
    }
    public ASTStatement body() {
	return body_;
    }
    
    public int line() {
	return line_;
    }

    public void setinitialize(ASTStatement initialize) {
	initialize_ = initialize;
    }

    public void settest(ASTExpression test) {
	test_ = test;
    }

    public void setincrement(ASTStatement increment) {
	increment_ = increment;
    }
    public void setbody(ASTStatement body) {
	body_ = body;
    }
    
    public void setline(int line) {
	line_= line;
    }


    public Object Accept(ASTVisitor V) {
	return V.VisitForStatement(this);
    }

    ASTStatement initialize_;
    ASTExpression test_;
    ASTStatement increment_;
    ASTStatement body_;
    int line_;
}
