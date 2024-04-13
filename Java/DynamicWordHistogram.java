class LookupTable<K, V> {
	class Entry {
		K key;
		V value;
		Entry next;

		Entry(K k, V v) {
			key = k;
			value = v;
		}
	}

	Entry start;

	LookupTable() {
		start = null;
	}

	void enter(K key, V value) {
		// associate value with key in the table
		// either adds a new (key, value)-pair or updates the value in an existing pair
		Entry e = start;
		while (e != null && !e.key.equals(key))
			e = e.next;
		if (e != null)  // e.key.equals(key)
			e.value = value;  // replace/update value
		else {  // e == null: create and prepend new entry
			e = new Entry(key, value);
			e.next = start;
			start = e;
		}
	}

	V lookup(K key) {
		// return the value associated with key or null
		// TODO implement this method
		Entry e = start;                      //Geht ganz entry durch, gibt es zu dem mitgegebenen key ein value wird dieser returned
		while (e != null) { 					// Ansonsten wird null returned
			if (e.key.equals(key)) {
				return e.value;
			}
			e = e.next;
		}
		return null;
	}

	boolean containsKey(K key) {
		Entry e = start;                  // Geht ganz entry durch, kommt der geforderte key vor wird true ausgegeben sonst false
		while (e != null) {
			if (e.key.equals(key)) {
				return true;
			}
			e = e.next;
		}
		return false;
	}

	boolean containsValue(V value) {
		Entry e = start;
		while (e != null) {                   // Geht ganz entry durch, kommt der geforderte value vor wird true ausgegeben sonst false
			if (e.value.equals(value)) {
				return true;
			}
			e = e.next;
		}
		return false;
	}

	void remove(K key) {
		Entry tmp = start;
		Entry prev = null;
		if (tmp != null && tmp.key.equals(key)) {      //Befindet sich der zu entfernende schlüssel wird der start Wert von entry um einen weiter nach hinten verschoben
			start = tmp.next;                          //Und der alte start Entry gelöscht
		}
		else {
			while (tmp != null && !tmp.key.equals(key)) {   // Gehe liste durch solange Eintrag nicht leer und nicht gleich dem gesuchten key ist
				prev = tmp;									// Und speichert den vorherigen Wert
				tmp = tmp.next;                             // Wird der passende key gefunden wird dieser aus der Kette entfernt indem wir prev.next = tmp.next setzen
			}
			if (tmp == null) {
				return;
			}
			prev.next = tmp.next;
		}
	}
}

public class DynamicWordHistogram {
	static LookupTable<String, Integer> wordHistogram = new LookupTable<>();

	public static void main(String[] args) {
			addWord("Hallo");
			addWord("Servus");
			addWord("Moin");
			addWord("Moin");
			removeWord("Hallo");
			Out.println(containsWord("Servus"));
			Out.println(containsWord("Hallo"));
			Out.println(getWordFrequency("Moin"));
	}

	static void addWord (String word){
		if(!wordHistogram.containsKey(word)) {     // Ist das Wort noch nicht enthalten, so wird es hinzugefügt und die Häufigkeit auf 1 gesetzt
			wordHistogram.enter(word, 1);
		}
		else if(wordHistogram.containsKey(word)){    // Kam das wort schon vor erhöhe count um 1 und lösche alten Eintrag
			int count = wordHistogram.lookup(word)+1;  // Füge dann das Wort mit der neunen Häufigkeit hinzu
			wordHistogram.remove(word);
			wordHistogram.enter(word, count);
		}
	}

	static void removeWord (String word) {     // ISt das gescuhte word enthalten, so wird dieses removed
		if(wordHistogram.containsKey(word)){
			wordHistogram.remove(word);
		}
	}

	static int getWordFrequency (String word){       // Guckt die frequency von word mit lookup nach und returned diese
		int wordFrequency = 0;
		if(wordHistogram.containsKey(word)) {
			wordFrequency = wordHistogram.lookup(word);
		}
		return wordFrequency;
	}

	static boolean containsWord (String word){   // Guckt ob das word in WordHistogram vorkommt und setzt contains auf true
		boolean contains = false;                 // Ansonsten wird false returned
		if(wordHistogram.containsKey(word)){
			contains = true;
		}
		return contains;
	}
}
// Sind Hallo und hallo gleich?