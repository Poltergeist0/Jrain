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
		SnapshotTreeInputXML st=new SnapshotTreeInputXML("/home/poltergeist0/Desktop/deleteMe.snapshots");
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
