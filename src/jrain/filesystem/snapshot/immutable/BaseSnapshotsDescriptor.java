package jrain.filesystem.snapshot.immutable;

import java.util.UUID;

import jrain.identifiable.immutable.Identifiable;

/**
 * @author poltergeist0
 *
 * Base snapshot descriptor.
 * A base snapshot has an identifier, a name and a size fields
 */
public class BaseSnapshotsDescriptor extends Identifiable implements jrain.filesystem.snapshot.BaseSnapshotsDescriptor{
	
	/**
	 * name
	 */
	private final String nm;
	
	/**
	 * size in bytes of space occupied in HDD (sum of files and sub-directories)
	 */
	private final Long sz;
	
	/**
	 * Constructor.
	 * 
	 * @param uuid is an {@link UUID}
	 * @param Name is the name of the descriptor
	 * @param size is the size of the descriptor
	 */
	public BaseSnapshotsDescriptor(
			UUID uuid,
			String Name,
			Long size
			) {
		super(uuid);
		nm=Name;
		sz=size;
	}
	
	/**
	 * Copy/modify constructor.
	 * 
	 * If any of the additional values is not null, they will be used, otherwise
	 * they will be copied from the original object.
	 * 
	 * @param <T> is the type of the descriptor that extends {@link BaseSnapshotsDescriptor}
	 * @param original is the original descriptor
	 * @param uuid if true a new random UUID is used, otherwise the original is used
	 * @param name is the new name or null
	 * @param size if <0 copies size from original otherwise assigns the passed size
	 */
	public <T extends BaseSnapshotsDescriptor> BaseSnapshotsDescriptor(
			T original,
			Boolean copyUUID,
			String name,
			Long size
			){
		super((copyUUID)?original:null);
		nm=(name==null)?original.getDescriptorName():name;
		sz=(size<0)?original.getDescriptorSize():size;
	}
	
	public String getDescriptorName(){return nm;}
	
	public Long getDescriptorSize(){return sz;}
	
}
