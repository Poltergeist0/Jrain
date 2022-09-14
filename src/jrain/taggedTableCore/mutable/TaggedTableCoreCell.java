package jrain.taggedTableCore.mutable;

import java.util.Map.Entry;

import jrain.entry.mutable.Pair;

/**
 * @author poltergeist0
 *
 * Cell for use with Tagged Tables.
 * It is composed of a key and a value.
 * Data is ignored when comparing two cells. Only the key is taken into account.
 * So, two cells are identical if their keys are identical regardless of the data.
 * If the data type of the key is a character type, this is case sensitive.
 * The {@code null} value is accepted either as key or value.
 * 
 * @param <TYPE_KEY> is the data type to be used for the key
 * @param <TYPE_DATA> is the data type to be used for the value
 */
public class TaggedTableCoreCell<
	TYPE_KEY extends Object,
	TYPE_DATA extends Object
> 
extends Pair<TYPE_KEY, TYPE_DATA> {
	
	/**
	 * Constructor from separate key and value.
	 * 
	 * @param key is the key
	 * @param value is the value
	 */
	public TaggedTableCoreCell(TYPE_KEY key,TYPE_DATA value){
		super(key,value);
	}
	
	/**
	 * Constructor from entry.
	 * 
	 * @param <U> is the data type of the class that extends {@link Entry}
	 * @param e is the entry containing the key and value
	 */
	public <U extends Entry<TYPE_KEY,TYPE_DATA> > 
	TaggedTableCoreCell(U e) {
		super(e);
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
	public <U extends TaggedTableCoreCell<TYPE_KEY,TYPE_DATA> > 
	TaggedTableCoreCell(U p, boolean deepCopy){
		super(p,deepCopy);
	}
	
	/**
	 * Get a deep copy of the Cell, if the parameter data types support it. 
	 * Otherwise returns a shallow copy.
	 * 
	 * @return a copy of this object
	 */
	@Override
	public TaggedTableCoreCell<TYPE_KEY,TYPE_DATA> deepCopy() {
		return new TaggedTableCoreCell<TYPE_KEY,TYPE_DATA>(this,true);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode(){
		return ((super.getKey() == null) ? 0 : super.getKey().hashCode());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/**
	 * Check if the current cell is identical to a given cell.
	 * Cells are identical if they have the same key.
	 * The value is not taken into account.
	 */
	@Override
	public boolean equals(Object obj){
		if (this == obj) {
			return true;
		}
		if (obj == null || obj.getClass()!= this.getClass()) {
			return false;
		}
		TaggedTableCoreCell<?,?> other = (TaggedTableCoreCell<?,?>) obj;//use unbounded to avoid warning about cast type safety
		if (super.getKey() == null) {
			if (other.getKey() != null) {
				return false;
			}
		} else if (!super.getKey().equals(other.getKey())) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get the separator, used in the toString method to separate the key and 
	 * value, via a method so that it can be overridden in classes that extend 
	 * this one.
	 * 
	 * @return the separator
	 */
	@Override
	public String separatorValue() {
		return SEPARATOR;
	}

	/**
	 * Separator used in the toString method to separate the key and value.
	 */
	public final static String SEPARATOR="#";
	
}
