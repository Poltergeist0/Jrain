package jrain.filesystem.snapshot.taggedTable;

import jrain.filesystem.snapshot.snapshotTree.SnapshotNode.DescriptorType;
import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link SnapshotNode} to a {@link TaggedTable}
 */
public interface SnapshotNodeTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link SnapshotNode}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=SnapshotsGroupDescriptorTaggedTable.taggedTableColumns();
		a.add(SnapshotDescriptorTaggedTable.taggedTableColumns(), false);
		a.add(FileDescriptorTaggedTable.taggedTableColumns(), false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link SnapshotNode} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link SnapshotNode} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link SnapshotNode}
	 */
	public static TaggedTableRow taggedTableRows(SnapshotNode d){
		if(d.getDescriptorType()==DescriptorType.SNAPSHOTSGROUP){
			return SnapshotsGroupDescriptorTaggedTable.taggedTableRows(d.getSnapshotsGroupDescriptor());
		}
		if(d.getDescriptorType()==DescriptorType.SNAPSHOT){
			return SnapshotDescriptorTaggedTable.taggedTableRows(d.getSnapshotDescriptor());
		}
		if(d.getDescriptorType()==DescriptorType.DIRECTORY){
			return DirectoryDescriptorTaggedTable.taggedTableRows(d.getDirectoryDescriptor());
		}
		return FileDescriptorTaggedTable.taggedTableRows(d.getFileDescriptor());
	}
	
	/**
	 * Convert a {@link SnapshotNode} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link SnapshotNode} to convert
	 * @return a {@link TaggedTable} with the given {@link SnapshotNode}
	 */
	public static TaggedTable taggedTable(SnapshotNode d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
