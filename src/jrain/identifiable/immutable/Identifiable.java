/*******************************************************************************
 * Copyright (C) 2026 poltergeist0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Any libraries this program depends on have their own Licenses.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * LICENSE file for more details.
 ******************************************************************************/
package jrain.identifiable.immutable;

import java.util.Map.Entry;

import jrain.entry.mutable.Pair;

import java.util.UUID;

/**
 * @author poltergeist0
 * 
 * This class provides the basis for all objects that are Identifiable.
 *
 * An Identifiable object has an object type and an identifier associated with 
 * it so that it can be identified in a set of heterogeneous objects, that may 
 * be of different classes without any relation between them (except that they 
 * extend/implement this class).
 *  
 * Identifiable objects must be of a class type that extends this class.
 * 
 * The identifier is composed of a {@link String} naming the object type, a separator 
 * and a time based {@link UUID}.
 */
public class Identifiable 
implements jrain.identifiable.Identifiable<UUID>,
Comparable<Identifiable>
{
	
	
	/**
	 * Universally Unique IDentifier (just in case it wasn't already explicit :)
	 * cof, cof...useless cofmment... cof :P
	 */
	private final UUID uuid;
	
	/**
	 * Name of the object type without any template parameters.
	 */
	private final String objtype;
	
	/**
	 * Copy constructor for all objects that extend/implement the Identifiable 
	 * class/interface and need to copy identifiers instead of recreating or 
	 * creating a new one.
	 * 
	 * If the passed Identifiable is null, a random {@link UUID} is used and the object
	 * type is set to unknown.
	 * 
	 * @param <T> is the type of the class that extends {@link Identifiable}
	 * @param i is an object that extends {@link Identifiable}
	 */
	public <T extends Identifiable> Identifiable(T i){
		if(i==null){
			uuid=UUID.randomUUID();
			objtype=OBJECTUNKNOWN;
		}
		else{
			uuid=i.identifier();
			objtype=i.objectType();
		}
	}
	
	/**
	 * Constructor for all objects (except the null object) that 
	 * extend/implement the Identifiable class/interface.
	 * 
	 * Must be called in the constructor of said object using super. In this
	 * case, the object type is correctly assigned the name of the class that
	 * called super.
	 * 
	 * This constructor should not be called outside the constructor or the 
	 * object type will be lost.
	 * 
	 * If the passed {@link UUID} is null, a random {@link UUID} is used.
	 * 
	 * Examples:
	 * {@code
	 * 	Identifiable id=new Identifiable((UUID)null);
	 * }
	 * 
	 * @param u is an {@link UUID} or null
	 */
	public Identifiable(UUID u){
		if(u==null) uuid=UUID.randomUUID();
		else uuid=u;
		objtype=jrain.identifiable.Identifiable.objectTypeOf(this);
	}
	
	/**
	 * Constructor for all objects (except the null object) that 
	 * extend/implement the Identifiable class/interface and explicitly want a 
	 * randomly assigned {@link UUID}.
	 * 
	 * Must be called in the constructor of said object using super. In this
	 * case, the object type is correctly assigned the name of the class that
	 * called super.
	 * 
	 * This constructor should not be called outside the constructor or the 
	 * object type will be lost.
	 * 
	 * Alias of {@code Identifiable((UUID)null)} used to avoid verbosity so 
	 * that no casting of null, or the null itself, has to be written.
	 * 
	 * Examples:
	 * {@code
	 * 	Identifiable id=new Identifiable();
	 * }
	 */
	public Identifiable(){
		uuid=UUID.randomUUID();
		objtype=jrain.identifiable.Identifiable.objectTypeOf(this);
	}
	
	/**
	 * Constructor for all objects that contain an Identifiable but do not
	 * extend/implement the Identifiable class/interface and want a randomly
	 * assigned UUID.
	 * 
	 * Must be called with an instance of said object.
	 * 
	 * Examples:
	 * {@code
	 *  String s="qwerty";
	 * 	Identifiable id=new Identifiable(s);//object type will be String
	 * }
	 * 
	 * @param <T> is the type of the object
	 * @param obj is the object
	 */
	public <T extends Object> Identifiable(T obj){
		UUID u = jrain.identifiable.Identifiable.getIdentifier(obj);//attempt to get uuid from obj
		if(u==null) u=UUID.randomUUID();
		uuid=u;
		objtype=jrain.identifiable.Identifiable.objectTypeOf(obj);
	}
	
	/**
	 * Constructor for all objects that contain an Identifiable but do not
	 * extend/implement the Identifiable class/interface and want a specific
	 * {@link UUID}.
	 * 
	 * If the passed {@link UUID} is null, an attempt is made to get an {@link UUID}
	 * from the given object and, if that fails, a random {@link UUID} is used.
	 * 
	 * Examples:
	 * {@code
	 *  String s="qwerty";
	 * 	Identifiable id=new Identifiable((UUID)null,s);
	 * }
	 * @param <T> is the type of the object
	 * @param id is the specific UUID
	 * @param obj is the object
	 */
	public <T extends Object> Identifiable(UUID id,T obj){
		if(id==null) {
			UUID u = jrain.identifiable.Identifiable.getIdentifier(obj);//attempt to get uuid from obj
			if(u==null) u=UUID.randomUUID();
			uuid=u;
		}
		else uuid=id;
		objtype=jrain.identifiable.Identifiable.objectTypeOf(obj);
	}
	
	/**
	 * Constructor to build an Identifiable from a string representation of
	 * an Identifiable (example: created with the toString() method).
	 * 
	 * @param str is the {@link String} containing the Identifiable
	 */
	public Identifiable(String ID){
		Entry<WELL_FORMED, Entry<String, UUID>> i = wellFormed(ID);
		objtype=i.getValue().getKey();
		uuid=i.getValue().getValue();
	}
		
	/**
	 * Get the {@link UUID} part of the identifier.
	 * 
	 * @return the {@link UUID} that identifies the object among objects of the same type
	 */
	public UUID identifier(){return uuid;}
	
	/**
	 * Get the name of the object type part of the identifier.
	 * The string that identifies the name of the object type (ex: 
	 * customObject) can be the name of the class that implements the object.
	 * 
	 * @return a {@link String} that identifies the object type
	 */
	public String objectType(){return objtype;}
	
	/**
	 * Get this Identifiable.
	 * Useful to get the Identifiable from objects that extend Identifiable.
	 * 
	 * @return the current Identifiable object
	 */
	@SuppressWarnings("unchecked")//do not get this one!? Identifiable<UUID> to Identifiable<UUID> generates warning!?
	public Identifiable identifiable(){return this;}
	
	/**
	 * Compare a given object that is or extends Identifiable with
	 * the current object to check if they are the same, copies or their 
	 * Identifiable are identical and, in either case, return true.
	 * 
	 * The result must be true even if both Identifiable belong to different 
	 * classes that have no relation to each other, as long as both Identifiable
	 * are identical.
	 * 
	 * This method should return the same result as equals except that it does 
	 * not work with every object type, since the passed object must 
	 * extend/implement the Identifiable class/interface, nor does it check if 
	 * the same object is passed.
	 * 
	 * The usefulness of this method when compared to equals is that it can be 
	 * used for situations where the equals method would fail. Such as when the 
	 * object is not completely defined, as an example, when reading the object 
	 * from a stream/file.
	 * 
	 * @param <T> is the type of any object that extends Identifiable.
	 * @param ID is the Identifiable of the object with type T.
	 * @return true if both Identifiable are equal.
	 */
	public <T extends jrain.identifiable.Identifiable<UUID>> boolean matches(T ID){
		if(ID!=null){
			//can not work directly with the variables because of encapsulating objects that also extend Identifiable
			if(ID.objectType().equals(objectType()) && ID.identifier().equals(identifier()))return true;
		}
		return false;
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 * @return the hash of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((objtype == null) ? 0 : objtype.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * Compare an object, that may not extend/implement the Identifiable 
	 * class/interface, with the current object.
	 * 
	 * If the passed object does not extend/implement the Identifiable 
	 * class/interface, this method returns false.
	 * 
	 * This method requires that the current object and the given object are of
	 * the same class.
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj is the object to compare.
	 * @return true if both Identifiable are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Identifiable)) {
			return false;
		}
		Identifiable other = (Identifiable) obj;
		if (objtype == null) {
			if (other.objtype != null) {
				return false;
			}
		} else if (!objtype.equals(other.objtype)) {
			return false;
		}
		if (uuid == null) {
			if (other.uuid != null) {
				return false;
			}
		} else if (!uuid.equals(other.uuid)) {
			return false;
		}
		return true;
	}

	/**
	 * Get a {@link String} representation of the Identifiable object.
	 * 
	 * It is the concatenation of the object type {@link String} with a separator and 
	 * the {@link UUID}.
	 * 
	 * @return a {@link String} that represents the Identifiable object.
	 */
	public String toString(){return objectType()+SEPARATOR+uuid;}

	@Override
	public int compareTo(Identifiable other) {
		if (objtype == null) {
			if (other.objtype != null) {
				return -1;
			}
		} else if (!objtype.equals(other.objtype)) {
			return objtype.compareTo(other.objtype);
		}
		if (uuid == null) {
			if (other.uuid != null) {
				return -1;
			}
		} else if (!uuid.equals(other.uuid)) {
			return uuid.compareTo(other.uuid);
		}
		return 0;
	}

	/**
	 * @author poltergeist0
	 *
	 * Enum with the different possible results of evaluating a {@link String} that
	 * represents an {@link Identifiable}
	 */
	public enum WELL_FORMED{
		TRUE, //if the string is well formed
		FALSE_EMPTY, //if the string is empty
		FALSE_SEPARATOR_ONLY, //if the string contains only the separator (both the name of the data type and the uuid are empty)
		FALSE_NO_SEPARATOR_UNKNOWN_OBJECT, //if only the uuid is present and valid
		FALSE_NO_SEPARATOR_INVALID_UUID, //if only the name of the data type is present
		FALSE_MULTIPLE_SEPARATORS, //if the string contains more than one separator
		FALSE_NO_OBJECT_TYPE, //if the part of the string containing the name of the data type is empty
		FALSE_NO_OBJECT_TYPE_INVALID_UUID, //if the part of the string containing the name of the data type is empty and the uuid is invalid
		FALSE_NO_UUID, //if the part of the string containing the UUID is empty
		FALSE_INVALID_UUID //if the part of the string containing the UUID is an invalid UUID
		};
	
	/**
	 * Check if a string representation of an Identifiable is well formed.
	 * 
	 * This method returns an {@link Entry} where:
	 * - the key is a {@link WELL_FORMED} enum indicating if any errors occurred
	 * - the value is an {@link Entry} with whatever information was possible to read,
	 * with the name of the data type as the key and the {@link UUID} as the value
	 * 
	 * For general use, prefer the method {@link Identifiable#isWellFormed(String)}
	 * which simply returns true or false.
	 * 
	 * @param identifiable is a string representation of an Identifiable 
	 * @return a pair with the result of the evaluation
	 */
	public static Entry<WELL_FORMED,Entry<String,UUID>> wellFormed(String identifiable){
		String tp=OBJECTUNKNOWN;
		UUID id=null;
		WELL_FORMED r=WELL_FORMED.FALSE_EMPTY;
		if(identifiable==null || identifiable.length()<=0) {//empty
			tp="";
			id=UUID.randomUUID();
		}
		else {
			int first = identifiable.indexOf(SEPARATOR);
			if(first==-1) {//no separator
				try {//check if only uuid was given
					id=UUID.fromString(identifiable);//failure point here
					tp=OBJECTUNKNOWN;
					//has UUID but not object type
					r=WELL_FORMED.FALSE_NO_SEPARATOR_UNKNOWN_OBJECT;
				}
				catch (IllegalArgumentException e) {
					//assume it has object type due to not having a recognizable UUID
					//there is no way for this method to check if the object type is valid
					tp=identifiable;
					id=UUID.randomUUID();
					r=WELL_FORMED.FALSE_NO_SEPARATOR_INVALID_UUID;
				}
			}
			else {//contains separator
				int last=identifiable.lastIndexOf(SEPARATOR);
				if(first!=last) {//more than one separator
					tp=identifiable.substring(0,first);
					try {//make one attempt to read an uuid
						id=UUID.fromString(identifiable.substring(last+1,identifiable.length()));
					}
					catch (IllegalArgumentException e) {
						id=UUID.randomUUID();
					}
					r=WELL_FORMED.FALSE_MULTIPLE_SEPARATORS;
				}
				else {//single separator
					if(first==0) {//object type is empty
						tp=OBJECTNOTIDENTIFIABLE;
						if(last==identifiable.length()-1) {//UUID is empty
							id=UUID.randomUUID();
							r=WELL_FORMED.FALSE_SEPARATOR_ONLY;
						}
						else {//UUID is not empty
							try {//make one attempt to read an uuid
								id=UUID.fromString(identifiable.substring(last+1,identifiable.length()));
								r=WELL_FORMED.FALSE_NO_OBJECT_TYPE;
							}
							catch (IllegalArgumentException e) {
								id=UUID.randomUUID();
								r=WELL_FORMED.FALSE_NO_OBJECT_TYPE_INVALID_UUID;
							}
						}
					}
					else {//object type is not empty
						tp=identifiable.substring(0,first);
						if(last==identifiable.length()-1) {//UUID is empty
							id=UUID.randomUUID();
							r=WELL_FORMED.FALSE_NO_UUID;
						}
						else {//UUID is not empty
							try {//make one attempt to read an uuid
								id=UUID.fromString(identifiable.substring(last+1,identifiable.length()));
								r=WELL_FORMED.TRUE;
							}
							catch (IllegalArgumentException e) {
								id=UUID.randomUUID();
								r=WELL_FORMED.FALSE_INVALID_UUID;
							}
						}
					}
				}
			}
		}
		return new Pair<>(r, new Pair<String,UUID>(tp, id));
	}
	
	/**
	 * Check if a string representation of an Identifiable is well formed.
	 * 
	 * @param identifiable is a string representation of an Identifiable 
	 * @return true if it is well formed
	 */
	public static boolean isWellFormed(String identifiable){
		if(wellFormed(identifiable).getKey()==WELL_FORMED.TRUE) return true;
		return false;
	}

}
