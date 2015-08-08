class AATMemory extends AATExpression {
    public AATMemory(AATExpression mem) {
	mem_ = mem;
    }

    public AATExpression mem() { 
	return mem_;
    }

    public void setmem(AATExpression mem) {
	mem_ = mem;
    }

    private AATExpression mem_;

    public Object Accept(AATVisitor V) {
	return V.VisitMemory(this);

    }
}
