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
			T item = next.objRef;
			callback.fn(item);
			getNext();
		}
	}
	public void link(ObjectList<T> b) {
		/*b.restart();
		while(b.next != null) {
			T item = b.next.objRef;
			add(item);
			b.getNext();
		}*/
		if (b.first != null) {
			if (last == null) {
				last = b.first;
			}
			else {
				last.next = b.first;
			}
			length += b.length;
		}
	}
	public void merge(ObjectList<T> b) {
		b.restart();
		while(b.next != null) {
			T item = b.next.objRef;
			add(item);
			b.getNext();
		}/*
		if (b.first != null) {
			if (last == null) {
				last = b.first;
			}
			else {
				last.next = b.first;
			}
			length += b.length;
		}*/
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

		ObjectList<BoundingBox> list3 = new ObjectList<BoundingBox>();
		BoundingBox bb = new BoundingBox();
		list3.fastAddFirst(bb);
		
		for(int i = 0; i < 10; i++ ) { 
			list3.fastAdd(new BoundingBox(0, 0, 1, 1));
		}
		ObjectList<BoundingBox> list4 = new ObjectList<BoundingBox>();
		BoundingBox bb2 = new BoundingBox();
		list4.fastAddFirst(bb2);
		
		for(int i = 0; i < 10; i++ ) { 
			list4.fastAdd(new BoundingBox(0, 0, 1, 1));
		}
		
		list3.forEach((e) -> {
			//System.out.println(e);
		});
		
		
	}

}
