package jrain.runnable;


import java.util.concurrent.atomic.AtomicBoolean;

import jrain.differentialHistory.immutable.DifferentialHistory;
import jrain.runnable.RunnableStepByStepStatistics.FIELD;

/**
 * @author poltergeist0
 *
 * Class that extends {@link Runnable} to enable running code one step at a time.
 * 
 * Unlike {@link Runnable} it is stopped on construction and requires an explicit
 * call to {@link RunnableStepByStep#go()} to start.
 * 
 * All forms of cycles (like for, while, etc) must be avoided. Instead 
 * of cycles use state machines or some other method to unfold the cycle.
 * 
 * Override the {@link RunnableStepByStep#step()} method to insert the code to be 
 * executed, as opposed to the {@link Runnable#run()} method used by {@link Runnable}.
 * 
 * Call the {@link RunnableStepByStep#step()} method to execute the code when 
 * using a {@link Thread}, as opposed to the {@link Runnable#run()} method 
 * used by {@link Runnable}.
 * 
 * Call the {@link RunnableStepByStep#process()} method to execute the code when 
 * not using a {@link Thread}, as opposed to the {@link Runnable#run()} method 
 * used by {@link Runnable}.
 * 
 * Provides methods {@link RunnableStepByStep#initialize()} and {@link RunnableStepByStep#endStep()}
 * to execute code before and after the main code, respectively.
 * 
 * Objects that extend this class should call the {@link RunnableStepByStep#process()}
 * method instead of {@link RunnableStepByStep#step()}.
 * 
 * Example: Using without a thread. Can only be done in classes that extend {@link RunnableStepByStep}
 * {@code
 * 	public class DoSomething extends RunnableStepByStep{
 * 		private int cnt=0;
 * 		private int max=100;//10s (=100*100ms)
 * 		private long sleepTime=100;//100ms
 * 		public DoSomething(int maximum, long sleep({
 * 			cnt=0;
 * 			max=maximum;
 * 			sleepTime=sleep;
 * 			super.totalSize(max);
 * 			super.processedSize(0);
 * 			super.action("Processing...");
 * 			super.go();//the last line must start the RunnableStepByStep
 * 		}
 * 		public void initialize() throws Exception {//does nothing}
 * 		public void finalize() throws Exception {super.validate();}
 * 		public void step() throws IOException{
 * 			if(cnt>=max){
 * 				super.finish();//when processing ends, the RunnableStepByStep must be ended
 * 			}
 * 			else{
 * 				++cnt;
 * 				super.processedSize(super.processedSize()+1);
 * 				Thread.sleep(sleepTime);//sleep so that main can have time to print something :)
 * 				//an actual class should never have a Thread.sleep call. It is used for this short example only
 * 			}
 * 		}
 * 		//must define a method to call the protected process method when not using a thread
 * 		public void execute(){super.process();}
 * 		public static void main(String[] args) throws Exception {
 * 			DoSomething ds=new DoSomething(100,10);
 * 			while(ds.running()){
 * 				ds.execute();
 * 				if(ds.statusChanged()){
 * 					System.out.print(ds.getStatus()+"\r");
 * 				}
 * 			}
 * 		}
 * 	}
 * }
 * 
 * Example: Using a thread
 * {@code
 * 	//identical to the above example except in the main method
 * 	public class DoSomething extends RunnableStepByStep{
 * 		private int cnt=0;
 * 		private int max=100;//10s (=100*100ms)
 * 		private long sleepTime=100;//100ms
 * 		public DoSomething(int maximum, long sleep({
 * 			cnt=0;
 * 			max=maximum;
 * 			sleepTime=sleep;
 * 			super.totalSize(max);
 * 			super.processedSize(0);
 * 			super.action("Processing...");
 * 			super.go();//the last line must start the RunnableStepByStep
 * 		}
 * 		public void initialize() throws Exception {//does nothing}
 * 		public void finalize() throws Exception {super.validate();}
 * 		public void step() throws IOException{
 * 			if(cnt>=max){
 * 				super.finish();//when processing ends, the RunnableStepByStep must be ended
 * 			}
 * 			else{
 * 				++cnt;
 * 				super.processedSize(super.processedSize()+1);
 * 				Thread.sleep(sleepTime);//sleep so that main can have time to print something :)
 * 			}
 * 		}
 * 		public static void main(String[] args) throws Exception {
 * 			DoSomething ds=new DoSomething(100,10);
 * 			Thread t=new Thread(ds);
 * 			t.start();
 * 			while(ds.running()){
 * 				if(ds.statusChanged()){
 * 					System.out.print(ds.getStatus()+"\r");
 * 				}
 * 			}
 * 			t.join();
 * 		}
 * 	}
 * }
 */
public abstract class RunnableStepByStep implements Runnable{
	
	public static long DEFAULT_SLEEP_TIME =100;	//one tenth of a second
	public static long MAX_SLEEP_TIME =691200000;	//8 days
	
	private static enum STATES {SLEEP,STEP,FINALIZE,END}
	
	/****************************
	 * status variables
	 ***************************/
	
	/*
	 * Note: The Atomic_X_ variables probably do not need to be atomic, volatile
	 * could suffice, since most of them are not shared directly but is better 
	 * to play it safe.
	 */
	
	/**
	 * setting to true (by parent program) will signal the end of processing 
	 * without errors and will gracefully end the thread
	 */
	private AtomicBoolean finish;
	
	/**
	 * setting to true (by parent program) will gracefully end the thread
	 */
	private AtomicBoolean aborted;
	
	/**
	 * Internal variable used to distinguish between running (true) and waiting (false) state.
	 * Can be set by the parent program to hold execution
	 */
	private AtomicBoolean active;
	
	/**
	 * internal variable used to indicate that the thread is currently processing information
	 * it extends variable active in the sense that the thread may be running but not processing
	 */
	private AtomicBoolean processing;
	
	/**
	 * variable used to signal that the information (after all processing has completed)
	 * is valid (if no error occurred) or invalid (if any error occurred)
	 */
	private AtomicBoolean valid;
	
	/**
	 * variable that holds the error message
	 */
	private String err;
		
	/**
	 * variable that holds the current progress status
	 */
//	private AtomicReference<RunnableStepByStepStatistics> progress;
	private DifferentialHistory<RunnableStepByStepStatistics> progress;
	
	
	/****************************
	 * other internal variables
	 ***************************/
	
	/**
	 * variable that holds the sleep time
	 */
	private final long sleepTime;
	
	/**
	 * internal state. Commands what code is being executed
	 */
	private STATES state=STATES.SLEEP;
	
	/**
	 * internal variable. Thread will run while it is true
	 */
	private AtomicBoolean run;
	
	/**
	 * setting to true will immediately end the thread after the current step,
	 * without executing any more instructions (no cleanup)
	 */
	private AtomicBoolean kill;
	
	/**
	 * Lock used to notify all waiting threads on the lock.
	 * Notification is performed at the end of every step and sleep period.
	 */
	private volatile Object lock;
	
	/**
	 * Internal flag that indicates that the object is being run inside a thread
	 * object.
	 * Automatically set when the run method is called.
	 * When this flag is false, the Thread.sleep() call in the SLEEP state is 
	 * disabled.
	 */
	private volatile boolean thr;
	
	/****************************
	 * status methods
	 ***************************/
	
	/**
	 * @return true if processing has been marked as completed without errors by
	 * the parent object.
	 * Otherwise returns false.
	 */
	public boolean finished(){return (finish.get())?true:false;}
	
	/**
	 * @return true if the thread has been aborted by the user or because of an error.
	 * Otherwise returns false.
	 */
	public boolean aborted(){return (aborted.get())?true:false;}
	
	/**
	 * @return true if the thread is running and not sleeping. Returns false if the thread
	 * is not running or is sleeping.
	 * The object may be stopped/paused (active returns false) but still running (waiting for command to resume).
	 */
	public boolean active(){return (active.get())?true:false;}
	
	/**
	 * @return true if the thread has started and not yet ended, false otherwise
	 */
	public boolean running(){return (run.get())?true:false;}
		
	/**
	 * @return true if the step method is being executed. False otherwise
	 */
	public boolean processing(){return (processing.get())?true:false;}
		
	/**
	 * @return true if the thread finished successfully and the results are valid. If the
	 * thread finishes with an error, this will return false. This will return false
	 * from instantiation of the object till processing has ended. Only then it may
	 * become true.
	 */
	public boolean valid(){return (valid.get())?true:false;}
	
	/**
	 * Method used to set valid to true after all processing is done and the processed
	 * information results are considered valid.
	 * Will be ignored if any error message was set.
	 * NOTE: this operation can only be performed once in the finalize method and can 
	 * not be undone (can not set valid to false) so use with caution and only after 
	 * making sure the data is valid.
	 */
	protected void validate(){
		if(err==null && state==STATES.FINALIZE) valid.set(true);
	}
	
	/**
	 * @return the error message or null if no error occurred
	 */
	public String errorMessage() {return err;}
	
	/**
	 * Set the error message.
	 * 
	 * Can only be set once and will automatically invalidate any results
	 * 
	 * @param msg is the message
	 */
	protected void setErrorMessage(String msg) {
		if(err==null) {
			err=msg;
			valid.set(false);
		}
	}
		
	public DifferentialHistory<RunnableStepByStepStatistics>.HistoryMarker marker(){
		return progress.marker();
	}
	
	protected RunnableStepByStepStatistics status(){
		return progress.last();
	}
	
	/**
	 * Method used to set the status.
	 * 
	 * @param stat is the new status
	 */
	protected void status(final RunnableStepByStepStatistics stat){
		progress.set(stat);
	}
	
	protected void statusModify(
			final long value,
			final FIELD field,
			final boolean setOrIncrement,
			final String action
			){
		progress.set(new RunnableStepByStepStatistics(progress.last(), value, field, setOrIncrement, action));
	}
	
	protected void statusAdd(
			final RunnableStepByStepStatistics differences
			){
		progress.set(new RunnableStepByStepStatistics(progress.last(), differences));
	}
	
	/**
	 * End the thread at the end of the current processing step.
	 * It will NOT perform cleanup
	 */
	public void kill(){
		kill.set(true);
	}
	
	/**
	 * Signal the thread that there is nothing more to process and it can end 
	 * without error.
	 * It will perform any cleanup
	 */
	public void finish(){
		finish.set(true);
	}
	
	/**
	 * Abort the thread at the end of the current processing step.
	 * Differs from {@link finish()} in semantics only, as this signals that an
	 * error occurred.
	 * It will perform any cleanup.
	 */
	public void abort(){
		aborted.set(true);
	}
	
	/**
	 * Pause the {@link RunnableStepByStep} momentarily. 
	 * If it is running inside a thread, it will sleep for sleepTime at a time 
	 * before checking for new commands.
	 * Otherwise, it will just do nothing (NOP).
	 */
	public void pause(){
		active.set(false);
	}
	
	/**
	 * Resume the {@link RunnableStepByStep} after a pause
	 */
	public void go(){
		active.set(true);
	}
	
	/**
	 * Instantiate the object with the default sleep time.
	 * 
	 * @throws Exception if the initialize method throws
	 */
	public RunnableStepByStep() throws Exception{
		this(DEFAULT_SLEEP_TIME);
	}
	
	/**
	 * Instantiate the object with a given sleep time. The maximum sleep time is 8 days
	 * 
	 * @param newSleepTime
	 * @throws Exception 
	 */
	public RunnableStepByStep(final long newSleepTime) throws Exception{
		run=new AtomicBoolean(true);
		kill=new AtomicBoolean(false);
		active=new AtomicBoolean(false);
		finish=new AtomicBoolean(false);
		aborted=new AtomicBoolean(false);
		valid=new AtomicBoolean(false);
		processing=new AtomicBoolean(false);
		progress=new DifferentialHistory<>();
		progress.set(new RunnableStepByStepStatistics());
		state=STATES.SLEEP;
		if(newSleepTime>0 && newSleepTime<MAX_SLEEP_TIME) sleepTime=newSleepTime;
		else sleepTime=DEFAULT_SLEEP_TIME;
		lock=null;
		err=null;
		thr=false;
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run(){
		thr=true;
		while(run.get()){
			process();
		}
		active.set(false);
		processing.set(false);
	}
	
	private void evaluateStateConditions() {
		if(kill.get()){//kill takes precedence
			state=STATES.END;
		}
		else if(aborted.get() && state!=STATES.END) {
			state=STATES.FINALIZE;
		}
		else if(finish.get() && state!=STATES.END) state=STATES.FINALIZE;
		else if(!active.get() && state!=STATES.END) state=STATES.SLEEP;//pause
	}
	
	private void stateSleep() {
		try {
			if(thr)Thread.sleep(sleepTime);
			if(active.get()) {
				state=STATES.STEP;//resume
			}
			if(lock!=null) {//notify just is case any thread is waiting when it should not. Example, thread requested lock just before state changing to SLEEP and will wait forever without this notification
				synchronized (lock) {
					lock.notifyAll();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void stateStep() {
		try {
			processing.set(true);
			step();
			processing.set(false);
			if(lock!=null) {
				synchronized (lock) {
					lock.notifyAll();
				}
			}
			if(!active.get()) state=STATES.SLEEP;//pause
		} catch (Exception e) {
			e.printStackTrace();
			valid.set(false);
			state=STATES.END;
		}
	}
	
	private void stateFinalize() {
		try {
			endStep();
		} catch (Exception e) {
			e.printStackTrace();
			valid.set(false);
		}
		state=STATES.END;
	}
	
	private void stateEnd() {
		/*execution will only get here after thread has been finished, killed or aborted, 
		 * so a finish/kill/abort command is required after processing has finished to turn 
		 * the object inactive
		 */
		if(lock!=null) {
			synchronized (lock) {
				lock.notifyAll();
				lock=null;
			}
		}
		run.set(false);	//terminate thread
		active.set(false);
		processing.set(false);
	}
	
	/**
	 * Objects that extend this class should call this method instead of step() when
	 * running outside of a Thread object. All this method does is set the flag 
	 * processing to true, call step() and set the flag processing to false.
	 */
	protected void process(){
		evaluateStateConditions();
		switch (state) {
		case SLEEP:stateSleep();break;
		case STEP:stateStep();break;
		case FINALIZE:stateFinalize();break;
		default://state==END
			stateEnd();break;
		}
	}
	
	/**
	 * Override this method with code that has to be executed at the instantiation of the
	 * thread
	 * 
	 * @throws Exception
	 */
	protected abstract void initialize() throws Exception;
	
	/**
	 * Override this method with the code to be executed. Since this code is 
	 * supposed to run "step by step" all forms of cycles (like for, while, etc) 
	 * must be avoided. Instead of cycles use state machines and a global counter
	 * where the counter is initiated in one state, is incremented in a second state
	 * where the body of the cycle is also executed. The second state (or a third state)
	 * must have the exit condition which switches to another state after the ones that
	 * hold the "cycle"
	 * 
	 * @throws Exception
	 */
	protected abstract void step() throws Exception;
	
	/**
	 * Override this method with code that has to be executed at the termination of the
	 * thread
	 * 
	 * @throws Exception
	 */
	protected abstract void endStep() throws Exception;
	
	/**
	 * Get a lock so that a notification is generated instead of actively waiting
	 * and checking if the status has changed.
	 * 
	 * @return the lock
	 */
	public Object getLock() {
		if(lock==null) lock=new Object();
		return lock;
	}
}
