package jrain.filesystem.snapshot.taggedTable;

import jrain.filesystem.snapshot.immutable.BaseSnapshotsDescriptor;
import jrain.taggedTable.identifiable.TaggedTableIdentifiable;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.mutable.LongPoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link BaseSnapshotsDescriptor} to a {@link TaggedTable}
 */
public interface BaseSnapshotsDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link BaseSnapshotsDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=TaggedTableIdentifiable.taggedTableColumns();
		a.add(jrain.filesystem.snapshot.BaseSnapshotsDescriptor.tagName,new TaggedTableColumnData<PolyType<?>>(new PolyType<String>(jrain.filesystem.snapshot.BaseSnapshotsDescriptor.defaultName)),false);
		a.add(jrain.filesystem.snapshot.BaseSnapshotsDescriptor.tagSize,new TaggedTableColumnData<PolyType<?>>(new PolyType<Long>(jrain.filesystem.snapshot.BaseSnapshotsDescriptor.defaultSize)),false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link BaseSnapshotsDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link BaseSnapshotsDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link BaseSnapshotsDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(BaseSnapshotsDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(TaggedTableIdentifiable.asTaggedTableRow(d.identifiable()),false);//add parent values
		b.add(jrain.filesystem.snapshot.BaseSnapshotsDescriptor.tagName,new StringPoly(d.getDescriptorName()),true);
		b.add(jrain.filesystem.snapshot.BaseSnapshotsDescriptor.tagSize,new LongPoly(d.getDescriptorSize()),true);
		return b;
	}
	
	/**
	 * Convert a {@link BaseSnapshotsDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link BaseSnapshotsDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link BaseSnapshotsDescriptor}
	 */
	public static TaggedTable taggedTable(BaseSnapshotsDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
