class AATRegister extends AATExpression {

    public AATRegister(Register register) {
	register_ = register;
    }

    public Register register() {
	return register_;
    }

    public void setregister(Register register) {
	register_ = register;
    }

    private Register register_;

    public Object Accept(AATVisitor V) {
	return V.VisitRegister(this);
    }
}
