package eagleview.test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/*
 * Given two Integers a and b, returns the number of integers from range [a,b] that can be 
 * expressed as product of two consecutive integers that is x*(x+1) for some x.
 * like for a=6, b =20 integers are 6(2*3), 12(3*4), 20(4*5)
 */
public class Factors {
	
	public static void main(String[] args) {
		Factors factors = new Factors();
		System.out.println(factors.solution(6, 20));
	}
	
	private int solution(int a , int b) {
		Set<Integer> lst = new HashSet<>();
		
		for(int i=a; i<=b; i++) {
			Integer[] factors = getFactors(i);
			for (int j = 0, k=factors.length-1; j < factors.length && k>=0;) {
				if(k<j) {
					break;
				}
				if(factors[j] * factors[k] == i) {
					if(factors[j]+1 == factors[k]) {
						lst.add(i);
					}
					j++;
					k--;
				}else if (factors[j] * factors[k] < i) {
					j++;
				}else if (factors[j] * factors[k] > i) {
					k--;
				}
			}			
		}
		
		
		return lst.size();
	}
	
	/**
	 * returns factors a given number
	 * @param number
	 * @return
	 */
	private Integer[] getFactors(int number){
		return IntStream.rangeClosed(2, number-1).filter(i -> number%i == 0).boxed().sorted().toArray(Integer[] :: new);
	}

}
