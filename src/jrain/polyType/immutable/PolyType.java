package jrain.polyType.immutable;

import jrain.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * Class for polymorphic data types.
 * 
 * Allows using a heterogeneous set of data types in places they are not normally
 * allowed, such as in standard collections.
 * 
 * Does not allow to set data after construction.
 * 
 * Check {@link jrain.polyType.mutable.PolyType} for the mutable 
 * version.
 * 
 * Example:
 * {@code
 * PolyTypeBase<Integer> i=new PolyTypeBase<>(new Integer(13));
 * PolyTypeBase<String> s=new PolyTypeBase<>(new String("qwerty"));
 * ArrayList<PolyTypeBase<?>> arr=new ArrayList<>();
 * arr.add(i);
 * arr.add(s);//allowed :)
 * }
 * 
 * @param <T> is the type of the data to store
 */
public class PolyType<T> implements DeepCopy<PolyType<T> >{
	private T var; //data
	
	/**
	 * Constructor for a given value
	 * 
	 * @param value is the data to store
	 */
	public PolyType(T value) { var=value;}
	
	/**
	 * Copy constructor
	 * 
	 * @param poly is the original object to be copied
	 * @param deepCopy if true makes a deep copy of the object, if possible. False makes a shallow copy
	 */
	public PolyType(PolyType<T> poly,boolean deepCopy) { var=((deepCopy)?DeepCopy.deepCopy(poly.var):poly.var);}
	
	/**
	 * Get the data stored.
	 * 
	 * @return the data stored
	 */
	public T get() { return var;}
	
	/**
	 * Protected method that can be used by mutable classes extending {@link PolyType}
	 * to set the data stored.
	 */
	protected void set(T value) { var=value;}
	
	public String toString() {
		if(var==null) return "null";
		return var.toString();
	}
	
	@Override
	public int hashCode() {
		if(var==null) return 0;
		return var.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
//		System.out.println(var+".equals("+obj+"){ polytype.class="+var.getClass()+" ; obj.class="+obj.getClass());
//		System.out.flush();
		if (this == obj) return true;
		if (!(obj instanceof PolyType)) {//obj is not PolyType
			//check to see if it is type T
			return var.equals(obj);
		}
		return var.equals(((PolyType<T>)obj).var);
	}

	@Override
	public PolyType<T> deepCopy() {
		return new PolyType<T>(this,true);
	}

}
