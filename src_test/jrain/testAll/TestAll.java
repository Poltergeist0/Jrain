package jrain.testAll;

import java.util.ArrayList;
import java.util.List;

import jrain.testAll.TestAll.TestInstantiator.GenericImplementer;

public class TestAll {

////	static interface Qwe<T>{
////		T v();
////	}
//	static abstract class Base<T,U,V>{
//		T tt;
//		U uu;
//		Base(T t, U u){tt=t;uu=u;}
//		public T k() {return tt;}
//		public V v() {return uu;}
////		public V v() {return from(uu);}
////		abstract V v() ;
////		protected abstract V from(U a);
////		public W giveMegiveMegiveMe(){return this;}
//	}
//	
//	static class HalfData<T>{
//		T tt;
//		HalfData(T t){tt=t;}
//		T g() {return tt;}
//	}
//	static class A<T> extends Base<Integer,T,T>{
//		A(Integer u,T t) {super(u,t);}
//		public T v() {return super.V();}
////		protected T from(T a) {return a;}
//	}
//	static class B1<T> extends Base<T,HalfData<T>,T>{
//		B1(T t, HalfData<T> u) {super(t, u);}
////		T v() {HalfData<T> a=super.vv();return a.g();}
////		protected T from(HalfData<T> a) {return a.g();}
////		B1<T> giveMegiveMegiveMe(){return super.giveMegiveMegiveMe();}
//		public T v() {return super.v;
//		}
//	}
////	static class B2<T,U extends HalfData<T>> extends Base<T,U>{
////		B2(T t, U u) {super(t, u);}
////	}
////	static class B2B<T> extends B2<T,HalfData<T>>{
////		B2B(T t, T u) {super(t, new HalfData<>(u));}
//////		T v() {return super.v().g();}
////	}
	
	static class QW<T extends Object>{
		T t;
		QW(T tt){t=tt;}
		public String toString() {return "QW "+t.toString();}
		T get(){return t;}
	}

	static class QWA<T extends Object> extends QW<T>{
		QWA(T tt) {super(tt);}
		public String toString() {return "QWA "+super.toString();}
	}
	
	static class DT<T extends Object>{
		T t;
		DT(T tt){t=tt;}
		public String toString() {return "DT "+t.toString();}
	}

	static class QWB<T extends Object> extends QWA<DT<T>>{
		QWB(T tt) {super(new DT<T>(tt));}
		public String toString() {return "QWB "+super.toString();}
	}

	static class Head<T extends Object>{
		T t;
		public Head(T tt){t=tt;System.out.println("Called Head");}
		public Head(){t=null;System.out.println("Called Head");}
		public static <T> Head<T> getInstance() {System.out.println("Calling inst Head");return new Head<>();}
		public static <T> Head<T> getInstance(T tt) {System.out.println("Calling inst Head");return new Head<>(tt);}
		public String toString() {return "Head "+t.toString();}
	}

	static class Rws<T extends Object>{
		T t;
		public Rws(T tt){t=tt;System.out.println("Called Rws");}
		public Rws(){t=null;System.out.println("Called Rws");}
		public static <T> Rws<T> getInstance() {System.out.println("Calling inst Rws");return new Rws<>();}
		public static <T> Rws<T> getInstance(T tt) {System.out.println("Calling inst Rws");return new Rws<>(tt);}
		public String toString() {return "Rws "+t.toString();}
	}

	static class QWE<K extends Object, D extends Object, H extends Head<K>, R extends Rws<D>>{
		H h;
		R r;
		QWE(H k, R d){
			h=k;
			r=d;
		}
		public void setH(K k) {h.t=k;}
		public void setD(D d) {r.t=d;}
		public String toString() {return "QWE "+h.toString()+":"+r.toString();}
		Head<K> get(){return h;}
	}

	static class Head2<T extends Object> extends Head<T>{
		int c;
		public Head2(T tt){super(tt);c=1;System.out.println("Called Head2");}
		public Head2(){super();c=0;System.out.println("Called Head2");}
		public static <T> Head2<T> getInstance() {System.out.println("Calling inst Head2");return new Head2<>();}
		public static <T> Head2<T> getInstance(T tt) {System.out.println("Calling inst Head2");return new Head2<>(tt);}
		public String toString() {return "Head2 "+t.toString();}
	}

	static class Rws2<T extends Object> extends Rws<T>{
		boolean b;
		public Rws2(T tt){super(tt);b=true;System.out.println("Called Rws2");}
		public Rws2(){super();b=false;System.out.println("Called Rws2");}
		public static <T> Rws2<T> getInstance() {System.out.println("Calling inst Rws2");return new Rws2<>();}
		public static <T> Rws2<T> getInstance(T tt) {System.out.println("Calling inst Rws2");return new Rws2<>(tt);}
		public String toString() {return "Rws2 "+t.toString();}
	}

	static class QWAS<K extends Object, D extends Object> extends QWE<K, D, Head2<K>, Rws2<D>>{
		QWAS(K k, D d) {super(new Head2<>(k),new Rws2<>(d));System.out.println("Called QWAS");}
		public String toString() {return "QWAS "+super.toString();}
	}
//	
//	static class DT<T extends Object>{
//		T t;
//		DT(T tt){t=tt;}
//		public String toString() {return "DT "+t.toString();}
//	}
//
//	static class QWB<T extends Object> extends QWA<DT<T>>{
//		QWB(T tt) {super(new DT<T>(tt));}
//		public String toString() {return "QWB "+super.toString();}
//	}

	public static class SupertypeMethod {
	    public static void mai(String[] args) {

	        Integer integer = null;
	        Number number = null;

	        List<Number> numberList = new ArrayList<Number>();
	        numberList.add(1);
	        numberList.add(3.1);
	        numberList.add(5);
	        numberList.add(7.8);
	        System.out.println("num="+numberList);
	        List<Integer> integerList = new ArrayList<>();
	        integerList.add(1);
	        integerList.add(3);
	        integerList.add(5);
	        integerList.add(7);
	        System.out.println("int="+integerList);

	        // Always works:
	        integer = fooTrivial(integer);
	        number = fooTrivial(number);
	        number = fooTrivial(integer);

	        numberList = withList(numberList);
	        //numberList = withList(integerList); // Does not work

	        // Both work:
	        numberList = withListAndBound(numberList);
	        numberList = withListAndBound(integerList);
	        System.out.println("num="+numberList);
	        
	        Qwerrt<Integer> qwerrtList=new Qwerrt<>();
	        qwerrtList.add(1);
	        qwerrtList.add(3);
	        qwerrtList.add(5);
	        qwerrtList.add(7);
	        System.out.println("qwerrt="+qwerrtList);

//	        qwerrtList=integerList;
//	        qwerrtList=WithAll(integerList);
	        List<Integer> integerList2=WithAll(qwerrtList);
	        System.out.println("int2="+integerList2);
	        System.out.println("int3="+(new What<Integer>()).WithAll(qwerrtList));
	    }

	    public static <T, U extends T> T fooTrivial(U u) {
	        return u;
	    }

	    public static <T, U extends T> List<T> withListAndBound(List<U> u) {
	        List<T> result = new ArrayList<T>();
	        result.add(u.get(0));
	        return result;
	    }

	    public static <T> List<T> withList(List<T> u) {
	        List<T> result = new ArrayList<T>();
	        result.add(u.get(0));
	        return result;
	    }
	    
	    public static class Qwerrt<T> extends ArrayList<T>{
			private static final long serialVersionUID = -675147931586256164L;
			public Qwerrt() {super();}
	    }

		public static <T, U extends T, W extends List<U> > List<T> WithAll(W u) {
	    	List<T> res=new ArrayList<T>();
	    	res.add(u.get(0));
	    	return res;
	    }
		public static class What<T>{
			public <U extends T, W extends List<U> > List<T> WithAll(W u) {
		    	List<T> res=new ArrayList<T>();
		    	res.add(u.get(0));
		    	return res;
		    }
		}
	}
	
	public TestAll() {}
	
	public static class TestInstantiator{
		public interface Instantiable<T> {
			public T instance();
		}
		public interface Instantiable2 {
			public static <T> T instance() {return null;}
		}
//		public static interface InstI<T> extends Instantiable<T>{
//			public T instance(int a);
//		}
		public static class TestInstance{
//			public static class Inst implements InstI<TestInstance>{
//				@Override
//				public TestInstance instance() {
//					return new TestInstance();
//				}
//				@Override
//				public TestInstance instance(int a) {
//					return new TestInstance(a);
//				}			
//			}
	
			int i=17;
			
			public TestInstance() {}
			public TestInstance(int a) {i=a;}
			
			public String toString() {return Integer.toString(i);}
		}
		public static class GenericInstanceCaller<T> implements Instantiable<T>{
			T var;
//			public class Instan implements Instantiable<T>{
				public T instance() {return null;}
//			}
			
			public GenericInstanceCaller() {
//				var=new T();//error: can not instantiate type T since exact type is unknown
				var=instance();
			}
			public String toString() {return var.toString();}
		}
		public static class GenericImplementer extends GenericInstanceCaller<TestInstance>{
//			public class Ins extends GenericInstanceCaller<TestInstance>.Instan{
				public TestInstance instance() {return new TestInstance();}
//			}
//			public String toString() {return 
		}
	}

	public static void main(String[] args) {
//		HalfData<String> hd=new HalfData<String>("qwertty");
//		B1<String> b1=new B1<String>("qaz", hd);
////		B2B<String> b2b=new B2B<String>("qxv", "poiyy");
////		A<String> a=b;
////		b2b.
		QW<DT<String>> b=new QWB<String>(new String("az"));
		System.out.println(b);
		System.out.println(b.get());
		QW<String> q=new QWA<>(new String("az"));
		System.out.println(q);
		System.out.println(q.get());
		
		Head2<String> hd=new Head2<String>("headache");
		System.out.println(hd);
		Head<String> hdd=hd;
		System.out.println(hdd);
		
		QWE<String,String,Head<String>,Rws<String>> w=new QWE<String,String,Head<String>,Rws<String>>(new Head<String>("qaz"),new Rws<String>("ter"));
		System.out.println(w);
		
		QWAS<String, String> ws = new QWAS<String,String>("qazzzz","teeeeer");
		System.out.println(ws);
		System.out.println(ws.get());
		
		SupertypeMethod.mai(args);
		
		GenericImplementer ti=new TestInstantiator.GenericImplementer();
		System.out.println("TestInstantiator = "+ti);	
	}

}
