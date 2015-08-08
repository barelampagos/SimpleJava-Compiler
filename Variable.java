class Variable {


    public Variable(String name, Type type) {
	name_ = name;
	type_ = type;
    }

    public Type type() {
	return type_;
    }

    public String name() {
	return name_;
    }

    public void setType(Type type) {
	type_ = type;
    }

    public void setName(String name) {
	name_ = name;
    }

    private String name_;
    private Type type_;


}
