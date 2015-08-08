class VoidType extends Type {

    private VoidType() {

    }

    public static VoidType instance() {
	if (instance_ == null) {
	    instance_ = new VoidType();
	}
	return instance_;
    }
    
    static private VoidType instance_;
}
