class AATReturn extends AATStatement {


    public Object Accept(AATVisitor V) {
	return V.VisitReturn(this);
    }


}
