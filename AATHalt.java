class AATHalt extends AATStatement {

    public Object Accept(AATVisitor V) {
	return V.VisitHalt(this);
    }
}
