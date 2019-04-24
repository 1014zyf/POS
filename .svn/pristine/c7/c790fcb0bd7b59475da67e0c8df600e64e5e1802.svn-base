package core.externallib;

import java.util.HashSet;

public class ThreadLocalSet<E> {

	private ThreadLocal<E> _threadLocal = null;
	private HashSet<E> _hashSet = null;
	
	public ThreadLocalSet() {
		_threadLocal = new ThreadLocal<E>();
		_hashSet = new HashSet<E>();
	}

	public E get() {
		return _threadLocal.get();
	}
	
	public void set(E element) {
		synchronized (_hashSet) {
			_hashSet.add(element);
		}
		_threadLocal.set(element);
	}
	
	public void remove() {
		synchronized (_hashSet) {
			_hashSet.remove(_threadLocal.get());
		}
		_threadLocal.remove();
	}
	
	@SuppressWarnings("unchecked")
	public HashSet<E> getSetCopy() {
		synchronized (_hashSet) {
			HashSet<E> copySet = new HashSet<E>();
			copySet.addAll(_hashSet);
			return copySet;
		}
	}
}
