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
package jrain.runnable.filesystem.snapshot.xml.immutable;

import jrain.differentialHistory.immutable.DifferentialHistory;
import jrain.runnable.RunnableStepByStepStatistics;

public class SnapshotTreeXML_Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
//		File f=new File("d:/temp/snapshot.snapshot");
//		FileReader fo=new FileReader(f);
//		char[] b=new char[(int)f.length()];
//		fo.read(b);
//		String s=new String(b);
//		fo.close();
//		System.out.print(s);
//		
//		String ss="";
//		SnapshotTreeXML sx=new SnapshotTreeXML(s);
//		SnapshotTree dir=sx.getSnapshot();
//		SnapshotTreeXML sxx=new SnapshotTreeXML(dir);
//		ss=sxx.getXML();
//		System.out.print(ss);
//		System.out.println(ss.equals(s));
		SnapshotTreeInputXML st=new SnapshotTreeInputXML("testFiles/deleteMe.snapshots");
		DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker marker = st.marker();
		Thread t=new Thread(st);
		t.start();
//		fcc.go();
		while(st.running()) {// && t.isAlive()){
//		while(t.isAlive()){
//			if(st.statusChanged()){
//				System.out.print(st.getStatus()+"\r");
//			}
			if(marker.changed()){
				System.out.print(marker.getAndUpdate()+"\r");
			}
//			System.out.println("running="+fcc.running());
		}
//		if(st.statusChanged()){
//			System.out.print(st.getStatus()+"\r");
//		}
		if(marker.changed()){
			System.out.print(marker.getAndUpdate()+"\r");
		}
		t.join();
		System.out.println(st.valid());
		System.out.println(st.getSnapshot());
		
		SnapshotTreeOutputXML out=new SnapshotTreeOutputXML(st.getSnapshot(), System.out);
		marker = out.marker();
		t=new Thread(out);
		t.start();
		while(out.running()) {
//			if(out.statusChanged()){
//				System.out.print(out.getStatus()+"\r");
//			}
			if(marker.changed()){
				System.out.print(marker.getAndUpdate()+"\r");
			}
		}
//		if(out.statusChanged()){
//			System.out.print(out.getStatus()+"\r");
//		}
		if(marker.changed()){
			System.out.print(marker.getAndUpdate()+"\r");
		}
		t.join();
		System.out.println();
		System.out.println(out.valid());
		System.out.println("END :)");
	}

}
