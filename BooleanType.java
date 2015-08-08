class BooleanType extends Type {

    private BooleanType() {

    }

    public static BooleanType instance() {
	if (instance_ == null) {
	    instance_ = new BooleanType();
	}
	return instance_;
    }

    static private BooleanType instance_;

}
