package jrain.arrayList.utils;

import java.util.ArrayList;

/**
 * @author poltergeist0
 *
 * Provides several static methods to work with {@code ArrayList}
 */
public class ArrayListUtils {

	/**
	 * Create a new {@link ArrayList} and add a single value.
	 * 
	 * @param <TYPE_DATA> is the data type of the value to add
	 * @param d is the value to add
	 * @return a new {@link ArrayList} with the value
	 */
	public static <TYPE_DATA> ArrayList<TYPE_DATA> init(final TYPE_DATA d) {
		ArrayList<TYPE_DATA> m=new ArrayList<TYPE_DATA>();
		m.add(d);
		return m;
	}

}
