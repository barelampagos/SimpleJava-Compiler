class VariableEntry {

    public VariableEntry(Type type) {
	type_ = type;
    }

    public VariableEntry(Type type, int offset) {
	type_ = type;
	offset_ = offset;
    }

    public Type type() {
	return type_;
    }

    public int offset() {
	return offset_;
    }

    public void settype(Type type) {
	type_ = type;
    }

    public void setoffset(int offset) {
	offset_ = offset;
    }

    private Type type_;
    private int offset_;
}
