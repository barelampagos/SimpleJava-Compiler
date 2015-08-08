public class VariableEnvironment {

    static final int TABLESIZE = 503;

    public VariableEnvironment() {
	htable = new HashTable(TABLESIZE);
    }

    public VariableEntry find(String key) {
	return (VariableEntry) htable.find(key);
    }

    public void insert(String key, VariableEntry entry) {
	htable.insert(key,entry);
    }

    public int size() {
	return htable.numelements();
    }

    public void beginScope() {
	htable.beginScope();
    }

    public void endScope() {
	htable.endScope();
    }

    private HashTable htable;

}
