package jrain.thread.synchronizable;


public class Synchronizable_Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("unlikely-arg-type")
	public static void main(String[] args) throws Exception {
		String s="qwerty";
		StringSynchronizable ss=new StringSynchronizable(s);
		System.out.println(ss.equals(s));
		System.out.println(ss.equals("qaz"));
	}

}
