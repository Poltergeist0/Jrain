package jrain.immutable.identifiable.interfaces;

import java.util.UUID;

/**
 * @author poltergeist0
 * 
 * This interface defines the methods required for an object to be Identifiable.
 * 
 * Identifiable objects implement this interface. Identifiable objects should,
 * preferably, extend the Identifiable class (which is defined in its own file).
 * 
 * An Identifiable object has an object type and an identifier associated with 
 * it so that it can be identified in a set of heterogeneous objects, that may 
 * be of different classes without any relation between them (except that they 
 * extend/implement this class).
 * 
 * The Identifiable is composed of a string naming the object type, a separator 
 * and an unique identifier.
 *
 * @param <TYPE_IDENTIFIER> is a class that implements an unique identifier, such
 * as UUID.
 */
public interface Identifiable<TYPE_IDENTIFIER> {
	
	/**
	 * Default separator to separate the object type from the UUID
	 */
	public static final String SEPARATOR=":";
	
	/**
	 * Object type when the object is Identifiable but its type can not be 
	 * determined.
	 * This should never occur but is defined to prevent corner cases or poorly
	 * implemented classes.
	 */
	public static final String OBJECTUNKNOWN="UNKNOWN";
	
	/**
	 * Object type when the object is not Identifiable (does not 
	 * extend/implement the Identifiable class/interface).
	 */
	public static final String OBJECTNOTIDENTIFIABLE="NOTIDENTIFIABLE";
	
	/**
	 * String used in conjunction with the toString() method to represent an
	 * {@link Identifiable}.
	 */
	public static final String IDENTIFIABLETAG="id";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * identifier part of an {@link Identifiable}.
	 */
	public static final String IDENTIFIERTAG="uid";

	/**
	 * String used in conjunction with the toString() method to represent the
	 * object type part of an {@link Identifiable}.
	 */
	public static final String OBJECTTYPETAG="type";
	
	/**
	 * Get the unique identifier part of the Identifiable.
	 * 
	 * @return the unique identifier that identifies the object among objects 
	 * of the same type
	 */
	public TYPE_IDENTIFIER identifier();
	
	/**
	 * Get the name of the object type part of the Identifiable.
	 * The string that identifies the name of the object type (ex: 
	 * customObject) can be the name of the class that implements the object.
	 * 
	 * The static method {@link Identifiable#objectTypeOf} provides an 
	 * implementation for this method.
	 * 
	 * Possible implementation: 
	 * 	String objectType = object.getClass().getSimpleName();
	 * 
	 * @return a string that identifies the object type
	 */
	public String objectType();
	
	public static String build(String objectType, String uid) {
		String o=objectType;
		String u=uid;
		if(o==null) o=OBJECTUNKNOWN;
		if(u==null) u=UUID.randomUUID().toString();
		return o+SEPARATOR+u;
	}
	
	/**
	 * Get the Identifiable.
	 * 
	 * If a class ClassA implements this interface, then this method may return
	 * an instance of ClassA.
	 * 
	 * If a class ClassB extends ClassA, then either an instance of ClassA or 
	 * ClassB may be returned depending on who called it.
	 * 
	 * @param <T> is the type of any object that extends Identifiable.
	 * @return the current Identifiable object
	 */
	public <T extends Identifiable<TYPE_IDENTIFIER>> T identifiable();
	
	/**
	 * Compare an given object that is or extends Identifiable with
	 * the current object to check if they are the same, copies or their 
	 * Identifiable are identical and, in either case, return true.
	 * 
	 * The result must be true even if both Identifiable belong to different 
	 * classes that have no relation to each other, as long as both Identifiable
	 * are identical.
	 * 
	 * This method should return the same result as equals except that it does 
	 * not work with every object type, since the passed object must 
	 * extend/implement the Identifiable class/interface, nor does it check the 
	 * references to verify if the same object is passed.
	 * 
	 * The usefulness of this method when compared to equals is that it can be 
	 * used for situations where the equals method would fail. Such as when the 
	 * object is not completely defined, as an example, when reading the object 
	 * from a stream/file, 
	 * 
	 * @param <T> is the type of any object that extends Identifiable.
	 * @param ID is the Identifiable of the object with type T.
	 * @return true if both Identifiable are identical.
	 */
	public <T extends Identifiable<TYPE_IDENTIFIER>> boolean matches(T ID);
	
	/**
	 * @return the hash of the object
	 */
	public int hashCode();
	
	/**
	 * Compare an object, that may not extend/implement the Identifiable 
	 * class/interface, with the current object.
	 * 
	 * If the passed object does not extend/implement the Identifiable 
	 * class/interface, this method returns false.
	 * 
	 * This method requires that the current object and the given object are of
	 * the same class to return true.
	 * 
	 * @param obj is the object to compare.
	 * @return true if both Identifiable are identical.
	 */
	public boolean equals(Object obj);
	
	/**
	 * Get a string representation of the Identifiable object.
	 * 
	 * It is the concatenation of the object type string with a separator and 
	 * the unique identifier.
	 * 
	 * @return a string that represents the Identifiable object.
	 */
	public String toString();
	
	/**
	 * Check if a given class extend/implement the Identifiable 
	 * class/interface.
	 * 
	 * @param <T> is the type of the class
	 * @param obj is the class
	 * @return true if the class extend/implement the Identifiable class/interface
	 */
	public static <T> boolean isIdentifiable(Class<T> obj){return Identifiable.class.isAssignableFrom(obj);}
	
	/**
	 * Get the string representation of the object type of a given class even 
	 * if it does not extend/implement the Identifiable class/interface.
	 * 
	 * The object type is the String as returned by the {@link Identifiable#objectType()} method
	 * 
	 * @param <T> is the type of the class
	 * @param obj is the class
	 * @return a string representing the object type
	 */
	public static <T> String objectTypeOf(Class<T> obj){return obj.getSimpleName();}
	
	/**
	 * Check if a given object extend/implement the Identifiable 
	 * class/interface.
	 * 
	 * @param <T> is the type of the object
	 * @param obj is the object
	 * @return true if the object extend/implement the Identifiable 
	 * class/interface
	 */
	public static <T> boolean isIdentifiable(T obj){return Identifiable.class.isInstance(obj);}
	
	/**
	 * Get the identifier of a generic object that may or may not extend/implement
	 * the Identifiable class/interface.
	 * 
	 * Returns null if the object does not extend/implement the Identifiable 
	 * class/interface.
	 * 
	 * @param <T> is the type of the object
	 * @param <TYPE_IDENTIFIER> is the type of the identifier
	 * @param obj is the object
	 * @return the identifier of the object or null.
	 */
	public static <T ,TYPE_IDENTIFIER > TYPE_IDENTIFIER getIdentifier(T obj){
		if(isIdentifiable(obj)) {
			@SuppressWarnings("unchecked")
			Identifiable<TYPE_IDENTIFIER> a = (Identifiable<TYPE_IDENTIFIER>) obj;
			return a.identifier();
		}
		return null;
	}

	/**
	 * Get the string representation of the object type of a given object even 
	 * if it does not extend/implement the Identifiable class/interface.
	 * 
	 * The object type is the String as returned by the {@link Identifiable#objectType()} method
	 * 
	 * @param <T> is the type of the object
	 * @param obj is the object
	 * @return a string representing the object type
	 */
	public static <T,TYPE_IDENTIFIER> String objectTypeOf(T obj){
		if(isIdentifiable(obj)) {
			@SuppressWarnings("unchecked")
			Identifiable<TYPE_IDENTIFIER> a=(Identifiable<TYPE_IDENTIFIER>) obj;
			if(a.objectType()!=null) {
				return a.objectType();
			}
			//else uninitialized object
		}
		return objectTypeOf(obj.getClass());
	}
	
}
