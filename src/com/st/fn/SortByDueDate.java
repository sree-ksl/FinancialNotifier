package com.st.fn;

import java.util.Comparator;
import java.util.Map;

public class SortByDueDate  implements Comparator<Map<String,String>> {

	@Override
	public int compare(Map<String, String> event1, Map<String, String> event2) {
		
		String  d1 = event1.get("duedate");
		String  d2 = event2.get("duedate");
		return  d1.compareTo(d2);
	}
	
}
