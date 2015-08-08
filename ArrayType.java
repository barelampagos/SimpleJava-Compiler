class ArrayType extends Type {

    public ArrayType(Type type) {
	type_ = type;
    }

    public Type type() {
	return type_;
    }

    public void settype(Type type) {
	type_ = type;
    }

    private Type type_;
}
