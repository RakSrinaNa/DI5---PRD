package fr.mrcraftcod.simulator.utils;

import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/**
 * Created a list where we can only add new items.
 * <p>
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 2018-11-07.
 *
 * @author Thomas Couchoud
 * @since 2018-11-07
 */
public class UnreadableQueue<T> implements Queue<T>{
	private final Queue<T> queue;
	
	/**
	 * Constructor.
	 *
	 * @param queue The queue to make unreadable.
	 */
	public UnreadableQueue(final Queue<T> queue){
		this.queue = queue;
	}
	
	@Override
	public int size(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isEmpty(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean contains(final Object o){
		throw new UnsupportedOperationException();
	}
	
	@NotNull
	@Override
	public Iterator<T> iterator(){
		throw new UnsupportedOperationException();
	}
	
	@NotNull
	@Override
	public Object[] toArray(){
		throw new UnsupportedOperationException();
	}
	
	@NotNull
	@Override
	public <T1> T1[] toArray(@NotNull final T1[] a){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean remove(final Object o){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean containsAll(@NotNull final Collection<?> c){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean addAll(@NotNull final Collection<? extends T> c){
		return queue.addAll(c);
	}
	
	@Override
	public boolean removeAll(@NotNull final Collection<?> c){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean retainAll(@NotNull final Collection<?> c){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clear(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean add(final T t){
		return queue.add(t);
	}
	
	@Override
	public boolean offer(final T t){
		return queue.offer(t);
	}
	
	@Override
	public T remove(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public T poll(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public T element(){
		throw new UnsupportedOperationException();
	}
	
	@Override
	public T peek(){
		throw new UnsupportedOperationException();
	}
}
