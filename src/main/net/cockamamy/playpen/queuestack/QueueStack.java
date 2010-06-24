/*
 * Copyright (c) 2008-2010, John Burwell
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are 
 * permitted provided that the following conditions are met:
 * 
 *    * Redistributions of source code must retain the above copyright notice, this list of 
 *      conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice, this list of 
 *      conditions and the following disclaimer in the documentation and/or other materials 
 *      provided with the distribution.
 *    * Neither the name of the John Burwell nor the names of its contributors may be used to 
 *      endorse or promote products derived from this software without specific prior written 
 *      permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR 
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY 
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR 
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
package net.cockamamy.playpen.queuestack;

import java.util.*;

/**
 * 
 * An implementation of the {@linkplain Queue} interface using only stacks
 * internally. In order to maintain performant for both <code>poll</code> and
 * <code>offer</code> operations, two stacks are maintained internally -- poll
 * and offer. When being offerred elements, all elements in poll stack are
 * flipped onto the offer stack, and the new element is pushed onto the offer
 * stack. When being polled, all elements in the offer stack are flipped onto
 * the poll stack, and the head element is popped off of the poll stack. At any
 * given time, all elements are completely contained in either the offer (in
 * order of insertion) or poll (in reverse order of insertion) stack. This
 * approach is tuned to be performant when a large number of sequential offer
 * operations are followed by a large number of poll operations.
 * 
 * <b>N.B.</b> This class is not thread-safe, and is unbounded.
 * 
 * @author John Burwell
 * 
 * @param <E>
 *            The type of element contained in this queue
 * 
 * @since 1.0.0
 * 
 */
public final class QueueStack<E> implements Queue<E> {

	private Stack<E> myOfferElements;

	private Stack<E> myPollElements;

	/**
	 * 
	 * Default constructor -- initializes an empty queue
	 * 
	 * @since 1.0.0
	 * 
	 */
	public QueueStack() {

		super();

		this.clear();

	}

	/**
	 * Copy constructor per the {@linkplain Collection} specification
	 * 
	 * @param theElements
	 *            The elements to add to the newly constructed collection.
	 *            
	 * @since 1.0.0
	 * 
	 */
	public QueueStack(Collection<E> theElements) {

		this();

		this.addAll(theElements);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#element()
	 */
	public E element() {

		this.flipToPoll();

		if (this.myPollElements.isEmpty() == true) {

			throw new NoSuchElementException();

		}

		return this.myOfferElements.peek();

	}

	/**
	 * 
	 * Pops the contents of the passed stack, <code>aStack</code>, and pushes
	 * them onto a new stack causing the elements in the new stack to be in the
	 * reverse order of the elements in the passed stack, <code>aStack</code>.
	 * 
	 * @param aStack
	 *            The stack to be flipped
	 * 
	 * @return A stack containing all of the elements of the passed stack,
	 *         <code>aStack</code>, in reverse order.
	 * 
	 * @since 1.0.0
	 * 
	 */
	private Stack<E> flip(Stack<E> aStack) {

		Stack<E> aFlippedStack = new Stack<E>();

		if (aStack != null) {

			while (aStack.isEmpty() == false) {

				aFlippedStack.push(aStack.pop());

			}

		}

		return aFlippedStack;

	}

	/**
	 * 
	 * Switches the active element stack to offer by popping all of the elements
	 * off of the poll stack and onto the offer stack.
	 * 
	 * @since 1.0.0
	 * 
	 */
	private void flipToOffer() {

		if (this.myOfferElements == null) {

			this.myOfferElements = flip(this.myPollElements);

		}

		this.myPollElements = null;

	}

	/**
	 * 
	 * Switches the active element stack to poll by popping all of the elements
	 * off of the offer stack and onto the poll stack.
	 * 
	 * <b>N.B.</b> The <code>myPollElements</code> is guaranteed to be non-null
	 * following the execution of this method.
	 * 
	 * @since 1.0.0
	 * 
	 */
	private void flipToPoll() {

		if (this.myPollElements == null) {

			this.myPollElements = flip(this.myOfferElements);

		}

		this.myOfferElements = null;

	}

	/**
	 * 
	 * The stack that current contains the elements of the queue. If both stacks
	 * are null, then an empty queue is returned. This method should only be
	 * used operations that do not require correct ordering of elements.
	 * 
	 * @return The stack that currently contains the elements of this queue.
	 * 
	 * @since 1.0.0
	 * 
	 */
	private Stack<E> getElements() {

		if (this.myOfferElements != null) {

			return this.myOfferElements;

		}

		if (this.myPollElements != null) {

			return this.myPollElements;

		}

		return new Stack<E>();

	}

	// BEGIN: Queue implementation
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	public boolean offer(E anObject) {

		if (anObject == null) {

			throw new IllegalArgumentException(
					"nulls can not be inserted into a Queue.");

		}

		this.flipToOffer();

		this.myOfferElements.push(anObject);

		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#peek()
	 */
	public E peek() {

		this.flipToPoll();

		return this.myPollElements.peek();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#poll()
	 */
	public E poll() {

		this.flipToPoll();

		if (this.myPollElements != null
				&& this.myPollElements.isEmpty() == false) {

			return this.myPollElements.pop();

		}

		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Queue#remove()
	 */
	public E remove() {

		this.flipToPoll();

		if (this.myPollElements.isEmpty() == true) {

			throw new NoSuchElementException();

		}

		return this.myPollElements.pop();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(E anObject) {

		return this.offer(anObject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends E> aCollection) {

		boolean aResult = true;

		for (E anElement : aCollection) {

			aResult = this.offer(anElement);

			if (aResult == false) {

				break;

			}

		}

		return aResult;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#clear()
	 */
	public void clear() {

		this.getElements().clear();
		this.myOfferElements = new Stack<E>();
		this.myPollElements = null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object anObject) {

		return this.getElements().contains(anObject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> aCollection) {

		return this.getElements().containsAll(aCollection);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {

		return this.getElements().isEmpty();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<E> iterator() {

		this.flipToPoll();

		return this.myPollElements.iterator();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object anObject) {

		return this.getElements().remove(anObject);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> aCollection) {

		return this.getElements().removeAll(aCollection);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> aCollection) {

		return this.getElements().retainAll(aCollection);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#size()
	 */
	public int size() {

		return this.getElements().size();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {

		this.flipToOffer();

		return this.myOfferElements.toArray();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Collection#toArray(T[])
	 */
	public <T> T[] toArray(T[] anArray) {

		this.flipToOffer();

		return this.myOfferElements.toArray(anArray);

	}

	// END: Queue implementation

	// BEGIN: Object implementation
	@Override
	public boolean equals(Object thatObject) {

		if (thatObject != null
				&& this.getClass().equals(thatObject.getClass()) == true) {

			QueueStack<?> thatQueueStack = (QueueStack<?>) thatObject;

			return Arrays.equals(this.toArray(), thatQueueStack.toArray());

		}

		return false;
	}

	@Override
	public int hashCode() {

		int aHashCode = 37;

		aHashCode += (aHashCode * 17) * this.getElements().hashCode();

		return aHashCode;

	}

	@Override
	public String toString() {

		return this.getElements().toString();

	}
	// END: Object implementation

}
