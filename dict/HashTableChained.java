/* HashTableChained.java */

package dict;
import list.*;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  DO NOT CHANGE ANY PROTOTYPES IN THIS FILE.
 **/

public class HashTableChained implements Dictionary {

	private DList[] defTable;
	public int collisions = 0;
	private int size = 0;

	/** 
	 *  Construct a new empty hash table intended to hold roughly sizeEstimate
	 *  entries.  (The precise number of buckets is up to you, but we recommend
	 *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
	 **/

	public static boolean isPrime(int n) {
		int divisor = 2;
		while (divisor < n) {        
			if (n % divisor == 0) {     
				return false;             
			}                           
			divisor++;                  
		}
		return true;
	}

	public HashTableChained(int sizeEstimate) {
		int n = sizeEstimate;
		while (!isPrime(n)) {
			n++;
		}
		defTable = new DList[n];
	}

	/** 
	 *  Construct a new empty hash table with a default size.  Say, a prime in
	 *  the neighborhood of 100.
	 **/

	public HashTableChained() {
		defTable = new DList[101];
	}

	/**
	 *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
	 *  to a value in the range 0...(size of hash table) - 1.
	 *
	 *  This function should have package protection (so we can test it), and
	 *  should be used by insert, find, and remove.
	 **/

	int compFunction(int code) {
		int temp = code % defTable.length;
		if (temp > 0) {
			return temp;
		} else {
			return (-1) * temp;
		}
	}

	/** 
	 *  Returns the number of entries stored in the dictionary.  Entries with
	 *  the same key (or even the same key and value) each still count as
	 *  a separate entry.
	 *  @return number of entries in the dictionary.
	 **/

	public int size() {
//		int total = 0;
//		for(DList d : defTable) {
//			if (d != null) {
//				total = total + d.length();
//			} 
//		}
//		return total;
		return size;
	}

	/** 
	 *  Tests if the dictionary is empty.
	 *
	 *  @return true if the dictionary has no entries; false otherwise.
	 **/

	public boolean isEmpty() {
		if (size == 0) {
			return true;
		} else {
			return false;
		}
//		for(DList d : defTable) {
//			if (d != null) {
//				return false;
//			}
//		}
//		return true;
	}

	/**
	 *  Create a new Entry object referencing the input key and associated value,
	 *  and insert the entry into the dictionary.  Return a reference to the new
	 *  entry.  Multiple entries with the same key (or even the same key and
	 *  value) can coexist in the dictionary.
	 *
	 *  This method should run in O(1) time if the number of collisions is small.
	 *
	 *  @param key the key by which the entry can be retrieved.
	 *  @param value an arbitrary object.
	 *  @return an entry containing the key and value.
	 **/

    public Entry insert(Object key, Object value) {
        if(size/defTable.length >= 1){
        	this.resize();
        }
		Entry newEntry = new Entry();
		newEntry.key = key;
		newEntry.value = value;
        int index = this.compFunction(key.hashCode());
		if (defTable[index] == null) {
			defTable[index] = new DList();
		} 
		defTable[index].insertBack(newEntry);
		size++;
		return newEntry;
	}
	
    public void resize(){
        int newSize = defTable.length*2;
        while(!isPrime(newSize)){
            newSize++;
        }
        DList[] holder = defTable;
        defTable = new DList[newSize];
        for(DList dlist: holder){
            if(dlist != null){
                DListNode current = (DListNode) dlist.front();
                while(current.isValidNode()){
                	Entry entry;
                	try {
                		entry = (Entry) current.item();
                		this.insert(entry.key, entry.value);
                        current = (DListNode) current.next();
                	} catch (InvalidNodeException e) {
                		e.printStackTrace();
                	}
                }
            }
        }
    }
	
	/** 
	 *  Search for an entry with the specified key.  If such an entry is found,
	 *  return it; otherwise return null.  If several entries have the specified
	 *  key, choose one arbitrarily and return it.
	 *
	 *  This method should run in O(1) time if the number of collisions is small.
	 *
	 *  @param key the search key.
	 *  @return an entry containing the key and an associated value, or null if
	 *          no entry contains the specified key.
	 **/

	public Entry find(Object key) {
		try {
			if (defTable[compFunction(key.hashCode())] != null) {
				ListNode TempNode = defTable[compFunction(key.hashCode())].front();
				while (TempNode != null)
					if (((Entry) TempNode.item()).key.equals(key)) {
						return (Entry) TempNode.item();
					} else {
						TempNode = TempNode.next();
					}
			} else {
				return null;
			}
		} catch (InvalidNodeException e) {
			return null;		
		}
		return null;
	}

	/** 
	 *  Remove an entry with the specified key.  If such an entry is found,
	 *  remove it from the table and return it; otherwise return null.
	 *  If several entries have the specified key, choose one arbitrarily, then
	 *  remove and return it.
	 *
	 *  This method should run in O(1) time if the number of collisions is small.
	 *
	 *  @param key the search key.
	 *  @return an entry containing the key and an associated value, or null if
	 *          no entry contains the specified key.
	 */

	public Entry remove(Object key) {
		try {
			if (find(key) == null) {
				return null;
			} else {
				ListNode TempNode = defTable[compFunction(key.hashCode())].front();
				while (!(((Entry) TempNode.item()).key.equals(key))) {
					TempNode = TempNode.next();
				}
				Entry TempEntry = (Entry) TempNode.item();
				TempNode.remove();
				size--;
				return TempEntry;
			} 
		}
		catch (InvalidNodeException e) {
			return null;		
		}
	}

	/**
	 *  Remove all entries from the dictionary.
	 */
	public void makeEmpty() {
		defTable = new DList[defTable.length];
		size = 0;
	}
	
	public String toString() {
		String result = "[  ";
		for (DList d : defTable) {
			if (d == null) {
				result = result + 0 + "  ";
			} else {
				result = result + d.length() + "  ";
			}
		}
		return result + "]";
	} 
	
	public static void main(String[] args) {
		HashTableChained table = new HashTableChained(5);
		table.insert(1,1);
		table.insert(2,2);
		table.insert(3,3);
		table.insert(4,4);
		table.insert(5,5);
		System.out.println(table.defTable.length);
		System.out.println(table);
		table.insert(6,6);
		System.out.println(table.defTable.length);
		System.out.println(table);
	}

}
