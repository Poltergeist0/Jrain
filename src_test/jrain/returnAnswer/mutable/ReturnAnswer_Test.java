package jrain.returnAnswer.mutable;

public class ReturnAnswer_Test {

	public static void main(String[] args) {
		ReturnAnswer<String> a=new ReturnAnswer<String>(true,"true");
		if(a.is()) {
			System.out.println(";)");
		}
		a=new ReturnAnswer<String>(false,"true");
		if(a.is()) {
			System.out.println(":o");
		}
	}

}
