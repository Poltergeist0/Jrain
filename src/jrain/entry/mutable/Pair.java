package jrain.entry.mutable;

import java.util.Map.Entry;

import jrain.entry.mutable.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * Implements {@link Entry} and DeepCopy.
 * 
 * @param <TYPE_KEY> is the data type of the key
 * @param <TYPE_DATA> is the data type of the value
 */
public class Pair<
	TYPE_KEY extends Object,
	TYPE_DATA extends Object
> 
implements Entry<TYPE_KEY,TYPE_DATA>, DeepCopy<Pair<TYPE_KEY,TYPE_DATA> >{

	/**
	 * @return the key
	 */
	@Override
	public TYPE_KEY getKey(){
		return ky;
	}
	
	/**
	 * Modify the key.
	 * 
	 * @param k is the new key
	 * @return the old key
	 */
	public TYPE_KEY setKey(TYPE_KEY k){
		TYPE_KEY old=ky;
		ky=k;
		return old;
	}
	
	/**
	 * @return the value
	 */
	@Override
	public TYPE_DATA getValue(){
		return vl;
	}
	
	/**
	 * Modify the value.
	 * 
	 * @param v is the new value
	 * @return the old value
	 */
	@Override
	public TYPE_DATA setValue(TYPE_DATA v) {
		TYPE_DATA old=vl;
		vl=v;
		return old;
	}

	/**
	 * Constructor from separate key and value.
	 * 
	 * @param key is the key
	 * @param value is the value
	 */
	public Pair(final TYPE_KEY key, final TYPE_DATA value) {
		ky=key;
		vl=value;
    }

	/**
	 * Constructor from {@link Entry}.
	 * 
	 * @param <U> is the data type that extends {@link Entry}
	 * @param e is the entry containing the key and value
	 */
	public <U extends Entry<TYPE_KEY,TYPE_DATA> > 
	Pair(U e) {
        ky=e.getKey();
        vl=e.getValue();
    }

	/**
	 * Copy constructor.
	 * Can make a shallow or a deep copy of the given object.
	 * If a deep copy is chosen, the key and value of the given object are deep
	 * copied before being assigned to this object, if they support deep copying.
	 * Otherwise, only a reference is copied.
	 * 
	 * @param <U> is the data type of the object that extends this class
	 * @param p is the given object
	 * @param deepCopy if true, makes a deep copy, otherwise makes a shallow copy
	 */
	public <U extends Pair<TYPE_KEY,TYPE_DATA> > 
	Pair(U p,boolean deepCopy){
		ky=(deepCopy)?DeepCopy.deepCopy(p.getKey()):p.getKey();
		vl=(deepCopy)?DeepCopy.deepCopy(p.getValue()):p.getValue();
	}
	
	/**
	 * Get a deep copy of the Pair, if the parameter data types support it. 
	 * Otherwise returns a shallow copy.
	 * 
	 * @return a copy of this object
	 */
	@Override
	public Pair<TYPE_KEY,TYPE_DATA> deepCopy() {
		return new Pair<TYPE_KEY,TYPE_DATA>(this,true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ky == null) ? 0 : ky.hashCode());
		result = prime * result + ((vl == null) ? 0 : vl.hashCode());
		return result;
	}

	/**
	 * Check if the current Pair is identical to a given Pair.
	 * They are identical if they have the same key and value.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass()!= this.getClass()) {
			return false;
		}
		Pair<?,?> other = (Pair<?,?>) obj;//use unbounded to avoid warning about cast type safety
		if (ky == null) {
			if (other.getKey() != null) {
				return false;
			}
		} else if (!ky.equals(other.getKey())) {
			return false;
		}
		//from here keys are identical
		if (vl == null) {
			if (other.getValue() != null) {
				return false;
			}
		} else if (!vl.equals(other.getValue())) {
			return false;
		}
		return true;
	}

	public static <TYPE_KEY extends Object,TYPE_DATA extends Object> 
	StringBuilder toString(StringBuilder s,TYPE_KEY k,TYPE_DATA v, String sep){
		if(k==null) s.append("null");
		else s.append(k.toString());
		if(sep==null) s.append(SEPARATOR);
		else s.append(sep);
		if(v==null) s.append("null");
		else s.append(v.toString());
		return s;
	}
		
	public static <
		TYPE_KEY extends Object & DeepCopy<TYPE_KEY>,
		TYPE_DATA extends Object & DeepCopy<TYPE_DATA>,
		U extends Pair<TYPE_KEY,TYPE_DATA> 
	> 
	StringBuilder toString(StringBuilder s,U c, String sep){
		return toString(s,c.getKey(), c.getValue(),sep);
	}
		
	public StringBuilder toString(StringBuilder s, String sep){
		return toString(s,ky, vl,sep);
	}
		
	public String toString(){
		StringBuilder s=new StringBuilder();
		return toString(s,separatorValue()).toString();
	}

	/**
	 * Get the separator, used in the toString method to separate the key and 
	 * value, via a method so that it can be overridden in classes that extend 
	 * this one.
	 * 
	 * @return the separator
	 */
	public String separatorValue() {
		return SEPARATOR;
	}
		
	/*
	 * Separator used in the toString method to separate the key and value.
	 */
	protected final static String SEPARATOR="=";

	private TYPE_KEY ky;	//key
	private TYPE_DATA vl;	//value

}
