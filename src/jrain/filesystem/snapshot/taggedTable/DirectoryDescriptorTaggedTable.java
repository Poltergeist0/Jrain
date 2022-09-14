package jrain.filesystem.snapshot.taggedTable;

import jrain.filesystem.snapshot.immutable.DirectoryDescriptor;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.mutable.BooleanPoly;
import jrain.polyType.mutable.LocalDateTimePoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

/**
 * @author poltergeist0
 *
 * Methods to convert a {@link DirectoryDescriptor} to a {@link TaggedTable}
 */
public interface DirectoryDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link DirectoryDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=BaseSnapshotsDescriptorTaggedTable.taggedTableColumns();
		a.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagPath,new TaggedTableColumnData<PolyType<?>>(new StringPoly(jrain.filesystem.snapshot.DirectoryDescriptor.defaultPath)),false);
		a.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagCreationDateTime,new TaggedTableColumnData<PolyType<?>>(new LocalDateTimePoly(jrain.filesystem.snapshot.DirectoryDescriptor.defaultCreationDateTime)),false);
		a.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagModificationDateTime,new TaggedTableColumnData<PolyType<?>>(new LocalDateTimePoly(jrain.filesystem.snapshot.DirectoryDescriptor.defaultModificationDateTime)),false);
		a.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagAccessDateTime,new TaggedTableColumnData<PolyType<?>>(new LocalDateTimePoly(jrain.filesystem.snapshot.DirectoryDescriptor.defaultAccessDateTime)),false);
		a.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagReadability,new TaggedTableColumnData<PolyType<?>>(new BooleanPoly(jrain.filesystem.snapshot.DirectoryDescriptor.defaultReadability)),false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link DirectoryDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link DirectoryDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link DirectoryDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(DirectoryDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(BaseSnapshotsDescriptorTaggedTable.taggedTableRows(d),false);
		b.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagPath,new StringPoly(d.getPath()),false);
		b.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagCreationDateTime,new LocalDateTimePoly(d.getCreationDateTime()),false);
		b.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagModificationDateTime,new LocalDateTimePoly(d.getLastModificationDateTime()),false);
		b.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagAccessDateTime,new LocalDateTimePoly(d.getLastAccessDateTime()),false);
		b.add(jrain.filesystem.snapshot.DirectoryDescriptor.tagReadability,new BooleanPoly(d.isReadable()),false);
		return b;
	}
	
	/**
	 * Convert a {@link DirectoryDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link DirectoryDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link DirectoryDescriptor}
	 */
	public static TaggedTable taggedTable(DirectoryDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
