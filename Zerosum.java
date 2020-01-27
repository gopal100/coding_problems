package eagleview.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * for an array A with N numbers find number of fragments that will result sum value of 0.
 */
public class Zerosum {
	
	public static void main(String[] args) {
		int[] arr = {2,-2, 3,0, 4,-7};
		Zerosum zerosum = new Zerosum();
		System.out.println(zerosum.solution(arr));
	}

	private int solution(int[] A) {
		int res = 0;
		if(A.length > 1000000000) {
			res = -1;
			return res;
		}
		List<PairofNumbers> pairs = new ArrayList<PairofNumbers>();
		Map<Integer, List<Integer>> map = new HashMap<>();		
		int sum = 0;
		for (int i = 0; i < A.length; i++) {
			sum += A[i];
			if(sum == 0) {
				pairs.add(new PairofNumbers(0, i));
			}
			List<Integer> lst = new ArrayList<Integer>();
			if(map.containsKey(sum)) {
				lst = map.get(sum);
				for (int j = 0; j <lst.size(); j++) {
					pairs.add(new PairofNumbers(lst.get(j)+1, i));
				}
			}
			lst.add(i);
			map.put(sum, lst);
		}		
		
		res = pairs.size();
		return res;
	}
}

class PairofNumbers{
	int startIdx, lastIdx;

	public PairofNumbers(int startIdx, int lastIdx) {
		super();
		this.startIdx = startIdx;
		this.lastIdx = lastIdx;
	}
}
