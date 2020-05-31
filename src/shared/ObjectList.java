package shared;

import java.util.Iterator;
import java.util.LinkedList;

import tools.Profiler;

public class ObjectList<T> {

	public int length = 0;
	
	public ListNode next;
	public ListNode prev;
	
	private ListNode first;
	private ListNode last;

	public class ListNode {
		public T objRef;
		public ListNode next;
		public ListNode prev;
		public ListNode(T obj) {
			objRef = obj;
		}
		public void remove() {
			next.prev = prev;
			prev.next = next;
		}
		public void remove(ObjectList<T> list) {
			next.prev = prev;
			prev.next = next;
			
			list.length--;
		}
	}
	public ObjectList() {
		
	}
	public ObjectList(T obj) {
		ListNode item = new ListNode(obj);
		if (first==null) {
			first = item;
		}
		last = item;	
	}

	public void add(T obj) {
		ListNode item = new ListNode(obj);
		if (first==null) {
			first = item;
		}
		if (last != null) {
			item.prev = last;
			last.next = item;
		}
		last = item;
		
		
		length++;
	}
	public void fastAdd(T obj) {
		ListNode item = new ListNode(obj);
		item.prev = last;
		last.next = item;
		last = item;	
		
		
		length++;
	}
	public void fastAddFirst(T obj) {
		ListNode item = new ListNode(obj);
		if (first==null) {
			first = item;
		}
		last = item;
		
		
		length++;
	}


	public void getNext() {
		next = next.next;
	}
	public void restart() {
		next = first;
	}
	public void forEach(Callback<T> callback) {
		restart();
		while(next != null) {
			//System.out.println("??");
			T item = next.objRef;
			callback.fn(item);
			//System.out.println(callback);
			getNext();
		}
	}
	public void link(ObjectList<T> b) {
		
		
		if (b.first != null) {
			if (first == null) {
				first = b.first;
			}
			if (last != null) {
				last.next = b.first;
			}
			else {
				last = b.first;
			}
			length += b.length;
		}
	}
	
	/**
	 * will walk through the given list adding each individial item to the 
	 * @param b
	 */
	public void merge(ObjectList<T> b) {
		b.restart();
		while(b.next != null) {
			T item = b.next.objRef;
			add(item);
			b.getNext();
		}
	}
	
	static public void main(String[] args) {
		int ObjectSize = 10;
		Profiler p = new Profiler();
		p.start("p");
		ObjectList<String> list1 = new ObjectList<>();
		list1.fastAddFirst(new String("0"));		
		for(int i = 1; i < ObjectSize; i++) {
			list1.fastAdd(Integer.toString(i));
		}

		list1.restart();
		while(list1.next != null) {
			String item = list1.next.objRef;
			if (item.equals("900")) {
				list1.next.remove();
			}
			//System.out.println(item);
			list1.getNext();
		}
		p.stop("p");
		p.print("p");
		
		
		p.start("p2");
		LinkedList<String> list2 = new LinkedList<>();
		for(int i = 0; i < ObjectSize; i++) {
			list2.add(Integer.toString(i));
		}
		
		
		Iterator<String> it = list2.iterator();
		while(it.hasNext()) {
			String item = it.next();
			if (item.equals("900")) {
				it.remove();
			}
		}
		p.stop("p2");
		p.print("p2");
		
		// combine test..
		ObjectList<String> list3 = new ObjectList<>();
		list3.fastAddFirst("A");
		list3.fastAdd("B");
		list3.fastAdd("C");
		list3.fastAdd("D");
		
		ObjectList<String> list4 = new ObjectList<>();
		list4.fastAddFirst("e");
		list4.fastAdd("f");
		list4.fastAdd("g");
		list4.fastAdd("h");
		
		list3.link(list4);
		list3.forEach((e) -> {
			System.out.println(e);
		});
		
		System.out.println(list3.first);
		System.out.println(list3.last);
	
		list3.forEach((e) -> {
			if (e.equals("D")) {
				list3.next.remove();
			}
			if (e.equals("C")) {
				list3.next.remove();
			}
			if (e.equals("g")) {
				list3.next.remove();
			}
		});
		
		list3.forEach((e) -> {
			System.out.println(e);
		});
		
	}

}
