public class TypeEnvironment {

    static final int TABLESIZE = 503;

    public TypeEnvironment() {
	htable = new HashTable(TABLESIZE);
	htable.insert("int", IntegerType.instance());
	htable.insert("boolean", BooleanType.instance());
	htable.insert("void", VoidType.instance());

    }

    public Type find(String key) {
	return (Type) htable.find(key);
    }

    public int size() {
	return htable.numelements();
    }

    public void insert(String key, Type type) {
	htable.insert(key,type);
    }

    public void beginScope() {
	htable.beginScope();
    }
    
    public void endScope() {
	htable.endScope();
    }

    private HashTable htable;

}
