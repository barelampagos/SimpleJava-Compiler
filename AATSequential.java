class AATSequential extends AATStatement {

    public AATSequential(AATStatement left, AATStatement right) {
	left_ = left;
	right_ = right;
    }

    public AATStatement left() {
	return left_;
    }

    public AATStatement right() {
	return right_;
    }

    public void setleft(AATStatement left) {
	left_ = left;
    }

    public void setright(AATStatement right) {
	right_ = right;
    }

    public Object Accept(AATVisitor V) {
	return V.VisitSequential(this);
    }
   
    private AATStatement left_;
    private AATStatement right_;

}
