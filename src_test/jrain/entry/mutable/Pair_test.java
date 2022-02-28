package jrain.entry.mutable;

import java.util.ArrayList;

public class Pair_test {

	public static void main(String[] args) {
		Pair<ArrayList<Integer>, ArrayList<Integer>> p = new Pair<>(new ArrayList<Integer>(), new ArrayList<Integer>());
		p.getKey().add(1);
		System.out.println(p);
	}

}
