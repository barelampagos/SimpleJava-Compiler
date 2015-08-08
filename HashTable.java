public class HashTable {
    
    public HashTable(int size) {
	int i;
	hsize = size;
	table = new HashElem[hsize];
	for (i=0; i<size;i++)
	    table[i] = null;
	marker = "StackMarker";    
	numelements_ = 0;
    }
    
    public void insert(String key, Object data) {
	int hashval = hash(key);
	table[hashval] = new HashElem(key, data, table[hashval]);
	numelements_++;
	stack = new StackElem(key, stack);
    }
    
    public int numelements() {
	return numelements_;
    }
    
    public Object find(String key) {
	HashElem tmp = table[hash(key)];
	while (tmp!= null && key.compareTo(tmp.key) != 0)
	    tmp = tmp.next;
	if (tmp == null)
	    return null;
	return tmp.data;
    }
    
    public void beginScope() {
	stack = new StackElem(marker,stack);
    }
    
    
    public void endScope() {
	while (stack != null && stack.key != marker) {
	    delete(stack.key);
	    stack = stack.next;
	}
	if (stack != null)
	    stack = stack.next;
    }
    
    public void delete(String key) {
	int index;
	HashElem tmp;
	
	index = hash(key);
	if (table[index] != null) {
	    if (key.compareTo(table[index].key) == 0) {
		table[index] = table[index].next;
		numelements_--;
	    } else {
		for (tmp = table[index]; (tmp.next != null &&
					  key.compareTo(tmp.next.key) != 0); tmp = tmp.next);
		if (tmp.next != null) {
		    tmp.next = tmp.next.next;
		    numelements_--;
		}
		
	    }
	}
    }
    
    private int hash(String key) {
	long h = 0;
	long g;
	int i;
	
	for(i=0; i<key.length(); i++) {
	    h = h << 4  + (int) key.charAt(i);
	    g = h & 0xF0000000L;
	    if (g != 0)
		h ^= g >>> 24;
	    h &= ~g;
	}
	return (int) (h % hsize);
    }
    
    private HashElem table[];
    private int hsize;
    
    
    private StackElem stack;
    private String marker;
    
    
    private class StackElem {
	public String key;
	public StackElem next;
	
	public StackElem(String k, StackElem n) {
	    key = k;
	    next = n;
	}
    }
    
    private int numelements_;
    
    
    private class HashElem {
	public Object data;
	public String key;
	public HashElem next;
	
	public HashElem(String k, Object d, HashElem n) {
	    data = d;
	    key = k;
	    next = n;
	}
	
    }
}
