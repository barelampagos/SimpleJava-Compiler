class ClassType extends Type {

    public ClassType(VariableEnvironment variables) {
	variables_ = variables;
    }

    public VariableEnvironment variables() {
	return variables_;
    }

    public void setvariables(VariableEnvironment variables) {
	variables_ = variables;
    }

    private VariableEnvironment variables_;
}
