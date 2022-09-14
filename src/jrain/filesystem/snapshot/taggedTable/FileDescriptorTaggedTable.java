package jrain.filesystem.snapshot.taggedTable;

import jrain.filesystem.snapshot.immutable.FileDescriptor;
import jrain.taggedTable.hashes.TaggedTableHashes;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link FileDescriptor} to a {@link TaggedTable}
 */
public interface FileDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link FileDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=DirectoryDescriptorTaggedTable.taggedTableColumns();
		a.add(TaggedTableHashes.taggedTableColumns(), false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link FileDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link FileDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link FileDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(FileDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(DirectoryDescriptorTaggedTable.taggedTableRows(d),false);
		b.add(TaggedTableHashes.taggedTableRows(d.getHashes()),false);
		return b;
	}
	
	/**
	 * Convert a {@link FileDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link FileDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link FileDescriptor}
	 */
	public static TaggedTable taggedTable(FileDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
