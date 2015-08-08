class AATEmpty extends AATStatement {

    public Object Accept(AATVisitor V) {
	return V.VisitEmpty(this);
    }

}
