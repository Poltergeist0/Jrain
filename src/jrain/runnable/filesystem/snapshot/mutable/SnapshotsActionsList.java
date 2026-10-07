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
package jrain.runnable.filesystem.snapshot.mutable;

//import jrain.runnable.filesystem.snapshot.mutable.SnapshotsAction.ACTIONTYPE;
import jrain.immutableList.utils.ImmutableListUtils;
import jrain.immutableMap.utils.ImmutableMapUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public class SnapshotsActionsList {
	
//	private ImmutableList<SnapshotsAction> acts=null;
	private ImmutableMap<SnapshotsAction.ACTIONTYPE, ImmutableList<SnapshotsAction> > acts=null;
	
	/**
	 * constructor to add or remove action
	 * If original is null a new list of actions is created with just the passed action
	 * 
	 * @param original is the original list of actions
	 * @param action is the action to add to the list
	 * @param add if true adds the given action else deletes it. If trying to delete and original is null an empty list is created
	 */
	public SnapshotsActionsList(SnapshotsActionsList original,SnapshotsAction action,boolean add){
//		if(add){
//			if(original!=null){
//				if(action!=null)acts=ImmutableListUtils.add(original.acts, action);
//				else acts=original.acts;
//			}
//			else{
//				if(action!=null) acts=ImmutableListUtils.add(ImmutableList.<SnapshotsAction>of(), action);
//				else acts=ImmutableList.<SnapshotsAction>of();
//			}
//		}
//		else{
//			if(original!=null)acts=ImmutableListUtils.remove(original.acts, action);
//			else acts=ImmutableList.<SnapshotsAction>of();
//		}
		if(add){
			if(original!=null){
				if(action!=null) {
					acts=ImmutableMapUtils.replace(original.acts, action.ActionType(), action.ActionType(), ImmutableListUtils.add(original.acts.get(action.ActionType()), action));
				}
				else acts=original.acts;
			}
			else{
				if(action!=null) {
					acts=ImmutableMapUtils.add(ImmutableMap.<SnapshotsAction.ACTIONTYPE, ImmutableList<SnapshotsAction> >of(), action.ActionType(), ImmutableListUtils.add(ImmutableList.<SnapshotsAction>of(), action));
				}
				else acts=ImmutableMap.<SnapshotsAction.ACTIONTYPE, ImmutableList<SnapshotsAction> >of();
			}
		}
		else{
			if(original!=null) {
				//the following is actually a remove from the list but the data must be replaced on the map
				acts=ImmutableMapUtils.replace(original.acts, action.ActionType(), action.ActionType(), ImmutableListUtils.remove(original.acts.get(action.ActionType()), action));
			}
			else acts=ImmutableMap.<SnapshotsAction.ACTIONTYPE, ImmutableList<SnapshotsAction> >of();
		}
	}
	
//	public ImmutableList<SnapshotsAction> actions(){return acts;}
	public ImmutableList<SnapshotsAction> actions(){
		ImmutableList<SnapshotsAction> a=ImmutableList.<SnapshotsAction>of();
		ImmutableList<SnapshotsAction> b = acts.get(SnapshotsAction.ACTIONTYPE.COPY);
		a=ImmutableListUtils.concatenate(a,0,a.size(), b,0,b.size());
		b=acts.get(SnapshotsAction.ACTIONTYPE.RECYCLE);
		a=ImmutableListUtils.concatenate(a, 0,a.size(), b,0,b.size());
		b=acts.get(SnapshotsAction.ACTIONTYPE.DELETE);
		a=ImmutableListUtils.concatenate(a, 0,a.size(), b,0,b.size());
		return a;
	}
	
}
