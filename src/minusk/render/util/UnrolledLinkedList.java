package minusk.render.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;

public class UnrolledLinkedList<E> implements List<E>, Queue<E> {
	private static final int DEFAULT_NODE_CAPACITY = 5;
	
	private ULLNode first, last;
	private int size;
	private final int nodeCapacity;
	private transient boolean hasMod = false;
	
	/**
	 * Creates a new <code>UnrolledLinkedList</code> with the default node capacity (5).
	 */
	public UnrolledLinkedList() {
		this(DEFAULT_NODE_CAPACITY);
	}
	
	/**
	 * Creates a new <code>UnrolledLinkedList</code> with a specified node capacity.
	 * 
	 * @param nodeCapacity The user-specified node capacity
	 */
	public UnrolledLinkedList(int nodeCapacity) {
		if (nodeCapacity <= 0)
			throw new IllegalArgumentException("nodeCapacity must be > 0");
		
		this.nodeCapacity = nodeCapacity;
		clear();
	}

	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (index >= size || index < 0)
			throw new IndexOutOfBoundsException(""+index);
		
		int current = 0;
		for (ULLNode node = first;; current += node.size, node = node.next) {
			if (current + node.size <= index)
				continue;
			return (E) node.elements[index-current];
		}
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		// TODO Implement addAll(int index, Collection c)
		return false;
	}

	@Override
	public E set(int index, E element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, E element) {
		// TODO Implement add(int index, E element)
	}

	@Override
	public E remove(int index) {
		// TODO Implement remove(int index)
		return null;
	}

	@Override
	public int indexOf(Object o) {
		// TODO Implement indexOf(Object o)
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		// TODO Implement lastIndexOf(Object o)
		return 0;
	}

	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return new ListItr<E>(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr<E>();
	}

	@Override
	public Object[] toArray() {
		// TODO Implement toArray()
		return null;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		// TODO Implement toArray(T[] a)
		return null;
	}

	@Override
	public boolean add(E e) {
		// TODO Implement add(E e)
		return true;
	}

	@Override
	public boolean remove(Object o) {
		// TODO Implement remove(Object o)
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		// TODO Implement containsAll(Collection c)
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		// TODO Implement addAll(Collection c)
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		// TODO Implement removeAll(Collection c)
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Implement retainAll(Collection c)
		return false;
	}

	@Override
	public void clear() {
		first = new ULLNode();
		last = first;
		size = 0;
	}

	@Override
	public boolean offer(E e) {
		return add(e);
	}

	@Override
	public E remove() {
		// TODO Implement remove()
		return null;
	}

	@Override
	public E poll() {
		if (isEmpty())
			return null;
		return remove();
	}

	@Override
	public E element() {
		// TODO Implement element()
		return null;
	}

	@Override
	public E peek() {
		if (isEmpty())
			return null;
		return element();
	}
	
	private class Itr<T> implements Iterator<T>{
		private ULLNode currentNode = first;
		private int indexInNode;
		@Override
		public boolean hasNext() {
			return currentNode != null;
		}
		@Override
		public T next() {
			@SuppressWarnings("unchecked")
			T element = (T) currentNode.elements[indexInNode++];
			if (indexInNode == currentNode.size) {
				currentNode = currentNode.next;
				indexInNode = 0;
			}
			
			return element;
		}
	}
	
	private class ListItr<T> implements ListIterator<T> {
		private int indexInNode = 0;
		private int indexInList = 0;
		private ULLNode node;
		
		private ListItr(int index) {
			for (node = first;; indexInList += node.size, node = node.next) {
				if (indexInList + node.size <= index)
					continue;
				indexInNode = index - indexInList;
				indexInList = index;
				break;
			}
		}
		
		@Override
		public boolean hasNext() {
			return size >= indexInList;
		}

		@Override
		public T next() {
			@SuppressWarnings("unchecked")
			T element = (T) node.elements[indexInNode++];
			indexInList++;
			if (indexInNode == node.size) {
				indexInNode = 0;
				node = node.next;
			}
			return element;
		}

		@Override
		public boolean hasPrevious() {
			return indexInList > 0;
		}

		@SuppressWarnings("unchecked")
		@Override
		public T previous() {
			indexInNode--;
			indexInList--;
			if (indexInNode == 0) {
				node = node.last;
				indexInNode = node.size-1;
			}
			return (T) node.elements[indexInNode];
		}

		@Override
		public int nextIndex() {
			return indexInList;
		}

		@Override
		public int previousIndex() {
			return indexInList-1;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void set(T e) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void add(T e) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class ULLNode {
		private int size;
		private ULLNode next;
		private ULLNode last;
		private final Object[] elements = new Object[nodeCapacity];
	}
}
