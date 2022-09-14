package jrain.filesystem.snapshot.taggedTable;

import java.util.Iterator;

import jrain.filesystem.snapshot.immutable.SnapshotDescriptor;
import jrain.taggedTable.hashes.TaggedTableHashes;
import jrain.taggedTable.mutable.TaggedTable;
import jrain.taggedTable.mutable.TaggedTableColumnData;
import jrain.taggedTable.mutable.TaggedTableHeader;
import jrain.taggedTable.mutable.TaggedTableRow;
import jrain.polyType.mutable.BooleanPoly;
import jrain.polyType.mutable.IntegerPoly;
import jrain.polyType.mutable.PolyType;
import jrain.polyType.mutable.StringPoly;

/**
 * @author poltergeist0
 * 
 * Methods to convert a {@link SnapshotDescriptor} to a {@link TaggedTable}
 */
public interface SnapshotDescriptorTaggedTable{
		
	/**
	 * @return a {@link TaggedTableHeader} with the columns of a {@link SnapshotDescriptor}
	 */
	public static TaggedTableHeader taggedTableColumns(){
		TaggedTableHeader a=BaseSnapshotsDescriptorTaggedTable.taggedTableColumns();
		a.add(jrain.filesystem.snapshot.SnapshotDescriptor.tagSnapshotBasePath, new TaggedTableColumnData<PolyType<?>>(new StringPoly(jrain.filesystem.snapshot.SnapshotDescriptor.defaultBasePath)), false);
		a.add(jrain.filesystem.snapshot.SnapshotDescriptor.tagSnapshotRecursion, new TaggedTableColumnData<PolyType<?>>(new IntegerPoly(jrain.filesystem.snapshot.SnapshotDescriptor.defaultRecursion)), false);
		a.add(TaggedTableHashes.taggedTableColumns(jrain.filesystem.snapshot.SnapshotDescriptor.tagSnapshotCalculate, new BooleanPoly(false)), false);
		return a;
	}
	
	/**
	 * Convert the values of a {@link SnapshotDescriptor} into a {@link TaggedTableRow}
	 * 
	 * @param d is the {@link SnapshotDescriptor} to convert
	 * @return a {@link TaggedTableRow} with the values of the given {@link SnapshotDescriptor}
	 */
	public static TaggedTableRow taggedTableRows(SnapshotDescriptor d){
		TaggedTableRow b = new TaggedTableRow(d);
		b.add(BaseSnapshotsDescriptorTaggedTable.taggedTableRows(d),false);
		b.add(jrain.filesystem.snapshot.SnapshotDescriptor.tagSnapshotBasePath,new StringPoly(d.getBasePath()),true);
		b.add(jrain.filesystem.snapshot.SnapshotDescriptor.tagSnapshotRecursion, new IntegerPoly(d.getRecursion()),true);
		Iterator<String> it = d.hashes().iterator();
		while(it.hasNext()) {
			String a = it.next();
			b.add(jrain.filesystem.snapshot.SnapshotDescriptor.tagSnapshotCalculate+a,new BooleanPoly(true) ,false);
		}
		return b;
	}
	
	/**
	 * Convert a {@link SnapshotDescriptor} into a {@link TaggedTable}
	 * 
	 * @param d is the {@link SnapshotDescriptor} to convert
	 * @return a {@link TaggedTable} with the given {@link SnapshotDescriptor}
	 */
	public static TaggedTable taggedTable(SnapshotDescriptor d){
		TaggedTable t=new TaggedTable(taggedTableColumns());
		t.addRow(taggedTableRows(d), false);
		return t;
	}
}
