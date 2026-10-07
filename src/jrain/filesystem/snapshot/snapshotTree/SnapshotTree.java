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
package jrain.filesystem.snapshot.snapshotTree;

import java.util.Set;

import jrain.filesystem.snapshot.snapshotTree.immutable.SnapshotNode;
import jrain.identifiable.immutable.Identifiable;
import jrain.treeIdentifiable.immutable.TreeNode;
import jrain.treeIdentifiable.TreeIdentifiable;


/**
 * @author poltergeist0
 *
 * Defines additional methods that a snapshot tree must implement besides the
 * ones from {@link TreeIdentifiable}
 */
public interface SnapshotTree{
	
	/**
	 * This method returns the leaf nodes of the tree that are files
	 * @return
	 */
	public Set<TreeNode<SnapshotNode, Identifiable>> getFiles();

}
