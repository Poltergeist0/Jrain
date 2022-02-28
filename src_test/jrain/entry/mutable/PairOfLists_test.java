package jrain.entry.mutable;

import java.util.ArrayList;

public class PairOfLists_test {

	public static void main(String[] args) {
		PairOfLists<Integer,Integer,ArrayList<Integer>, ArrayList<Integer>> p = new PairOfLists<>(new ArrayList<Integer>(), new ArrayList<Integer>());
		p.getKey().add(1);
		p.getValue().add(5);
		ArrayList<Integer> a=new ArrayList<>();
		a.add(7);
		a.add(13);
		a.add(4);
		p.add(a, null);
		System.out.println(p);
		p.remove(3, null);
		p.remove(7, null);
		System.out.println(p);
		PairOfLists<Integer,Integer,ArrayList<Integer>, ArrayList<Integer>> q= new PairOfLists<>(new ArrayList<Integer>(), new ArrayList<Integer>());
		q.add(p);
		System.out.println(q);
	}

}
