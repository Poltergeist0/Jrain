package jrain.taggedTableCore.mutable;

import jrain.deepCopy.DeepCopy;

/**
 * @author poltergeist0
 *
 * @param <TYPE_DATA> is the data type of the column data
 */
public interface TaggedTableCoreListHeader<TYPE_DATA extends Object> 
extends DeepCopy<TaggedTableCoreListHeader<TYPE_DATA> >{
	

	/**
	 * Modify the column data
	 * 
	 * @param defaultValue is the new value for the column data
	 * @return the previous value for the column data
	 */
	public TYPE_DATA value(TYPE_DATA defaultValue);
	
	/**
	 * @return the value for the column data
	 */
	public TYPE_DATA value();
	
	public String toString();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode();

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj);

}
