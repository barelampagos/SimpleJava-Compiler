class Label {

    protected String label;
    private static HashTable labelhash;
    Integer last;
    int next;
    
    public Label() {
	if (labelhash == null) {
	    labelhash = new HashTable(199);
	}
	last = (Integer) labelhash.find("LABEL");
	if (last == null) {
	    next = 1;
	    labelhash.insert("LABEL", new Integer(1));
	    label = "LABEL1";
	} else {
	    next = last.intValue() + 1;
	    labelhash.delete("LABEL");
	    labelhash.insert("LABEL", new Integer(next));
	    label = "LABEL" + next;
	}
    }
    
    
    public Label(String s) {
	if (labelhash == null) {
	    labelhash = new HashTable(199);
	}
	last = (Integer) labelhash.find(s);
	if (last == null) {
	    next = 1;
	    labelhash.insert(s, new Integer(1));
	    label = s + 1;
	} else {
	    next = last.intValue() + 1;
	    labelhash.delete(s);
	    labelhash.insert(s, new Integer(next));
	    label = s + next;
	}
	
    }
    

    public static Label AbsLabel(String s) {
	Label abslab = new Label();
	abslab.label = s;
	return abslab;
    }

    
    public String toString() {
	return label;
    }
    
}
