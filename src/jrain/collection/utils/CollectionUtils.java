package jrain.collection.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code Collection}
 */
public class CollectionUtils {


	/**
	 * Get the keys of all {@link Entry} stored in a {@link Collection}.
	 * May contain duplicates.
	 * 
	 * @param <TYPE_KEY> is the data type of the keys of the {@link Entry}
	 * @param <TYPE_DATA> is the data type of the values of the {@link Entry}
	 * @param <TYPE_LIST> is the data type of the {@link Collection}
	 * @param lst is the {@link Collection} of {@link Entry}
	 * @return a new {@link ArrayList} with the keys
	 */
	public static <TYPE_KEY,TYPE_DATA, TYPE_LIST extends Collection<Entry<TYPE_KEY,TYPE_DATA> > > ArrayList<TYPE_KEY> getKeys(TYPE_LIST lst){
		ArrayList<TYPE_KEY> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getKey());
		}
		return a;
	}

	/**
	 * Get the values of all {@link Entry} stored in a {@link Collection}.
	 * May contain duplicates.
	 * 
	 * @param <TYPE_KEY> is the data type of the keys of the {@link Entry}
	 * @param <TYPE_DATA> is the data type of the values of the {@link Entry}
	 * @param <TYPE_LIST> is the data type of the {@link Collection}
	 * @param lst is the {@link Collection} of {@link Entry}
	 * @return a new {@link ArrayList} with the values
	 */
	public static <TYPE_KEY,TYPE_DATA, TYPE_LIST extends Collection<Entry<TYPE_KEY,TYPE_DATA> > > ArrayList<TYPE_DATA> getValues(TYPE_LIST lst){
		ArrayList<TYPE_DATA> a=new ArrayList<>();
		Iterator<Entry<TYPE_KEY, TYPE_DATA>> it = lst.iterator();
		while(it.hasNext()) {
			Entry<TYPE_KEY, TYPE_DATA> e = it.next();
			a.add(e.getValue());
		}
		return a;
	}

}
