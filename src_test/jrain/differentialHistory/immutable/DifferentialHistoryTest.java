package jrain.differentialHistory.immutable;


//import jrain.runnable.RunnableStepByStepStatistics;

//TODO convert this class to unit test
public class DifferentialHistoryTest {

	private static void testView(DifferentialHistory<Integer> h) {
		h.set(3);
		System.out.println(h);
		DifferentialHistory<Integer>.HistoryMarker v2=h.marker();
		System.out.println(h);
		h.set(7);
		System.out.println(h);
	}
	
	public static void main(String[] args) throws InterruptedException {
		DifferentialHistory<Integer> h = new DifferentialHistory<Integer>();
		h.set(13);
		System.out.println(h);
		DifferentialHistory<Integer>.HistoryMarker v1=h.marker();
		System.out.println(h);
		System.out.println(v1.changed());
		h.set(14343);
		System.out.println(h.toString());
		System.out.println(v1.changed());
		try(DifferentialHistory<Integer>.HistoryMarker v134=h.marker();) {
			System.out.println(h);
		}
		System.out.println(h);
		h.set(-3);
		System.out.println(h);
		DifferentialHistory<Integer>.HistoryMarker vm3=h.marker();
		System.out.println(h);
		vm3.close();
//		vm3=null;
		System.out.println(h);
		h.set(1);
		System.out.println(h);
		testView(h);
		v1.getAndUpdate();
//		Thread.sleep(1000);
//		System.gc();
//		Thread.sleep(1000);
//		System.gc();
//		Thread.sleep(1000);
		h.set(31);
		System.out.println(h);
		DifferentialHistory<Integer>.HistoryMarker v3=h.marker();
		System.out.println(h);
		h.set(71);
		
		//TODO Create threaded test in different test unit
		//*******************************
//		DifferentialHistory<RunnableStepByStepStatistics> rh=new DifferentialHistory<>();
//		rh.set(new RunnableStepByStepStatistics(3, 100, "start"));
//		System.out.println(rh);
//		DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker rhm1 = rh.marker();
//		System.out.println(rh);
//		rh.set(new RunnableStepByStepStatistics(5, 100, null));
//		System.out.println(rh);
//		rh.set(new RunnableStepByStepStatistics(5, 100, "processing"));
//		System.out.println(rh);
//		rh.set(new RunnableStepByStepStatistics(5, 200, "processing"));
//		System.out.println(rh);
//		System.out.println(rhm1.changed());
//		rhm1.getAndUpdate();
//		System.out.println(rh);
//		rh.set(new RunnableStepByStepStatistics(10, 500, "end"));
//		System.out.println(rh);
	}
}
