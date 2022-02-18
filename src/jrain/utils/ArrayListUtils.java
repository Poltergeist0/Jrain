package jrain.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

//import java.util.ArrayList;
//import java.util.Iterator;

//import unifiedLibrary.interfaces.DeepCopy;

public class ArrayListUtils {

//	public static <
//		TYPE_DATA,
//		V extends DeepCopy<TYPE_DATA>
//	> 
//	ArrayList<TYPE_DATA> deepCopy(ArrayList<TYPE_DATA> p, V id) {
//		ArrayList<TYPE_DATA> m=new ArrayList<TYPE_DATA>();
//		Iterator<TYPE_DATA> it = p.iterator();
//		while(it.hasNext()) {
//			TYPE_DATA e = it.next();
//			m.add(id.deepCopy(e));
//		}
//		return m;
//	}

	public static <TYPE_DATA> ArrayList<TYPE_DATA> init(final TYPE_DATA d) {
	ArrayList<TYPE_DATA> m=new ArrayList<TYPE_DATA>();
	m.add(d);
	return m;
}

	public static <TYPE_KEY,TYPE_DATA> ArrayList<TYPE_KEY> getKeys(ArrayList<Entry<TYPE_KEY,TYPE_DATA> > lst){
		ArrayList<TYPE_KEY> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getKey());
		}
		return a;
	}

	public static <TYPE_KEY,TYPE_DATA> ArrayList<TYPE_DATA> getValues(ArrayList<Entry<TYPE_KEY,TYPE_DATA> > lst){
		ArrayList<TYPE_DATA> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getValue());
		}
		return a;
	}

}
