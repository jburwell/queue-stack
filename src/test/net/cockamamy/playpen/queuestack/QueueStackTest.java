/*
 * Copyright (c) 2010, John Burwell
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

import static net.cockamamy.playpen.queuestack.TestUtilities.*;
import static org.testng.Assert.*;

import java.util.*;

import org.testng.annotations.*;

@Test(groups = "unit")
public class QueueStackTest {

	private static final String ELEMENT_PROVIDER = "elements";

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testAddAll(Queue<Integer> theElements, Queue<Integer> aQueue) {

		assertNotNull(theElements);
		assertNotNull(aQueue);

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		assertTrue(aQueue.addAll(theElements));

		assertTrue(aQueue.containsAll(theElements));
		assertEquals(aQueue.size(), theElements.size());

		for (Integer anElement : theElements) {

			assertTrue(aQueue.contains(anElement));
			assertEquals(aQueue.poll(), anElement);
			assertFalse(aQueue.contains(anElement));

		}

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testRemoveAll(Queue<Integer> theElements, Queue<Integer> aQueue) {

		assertNotNull(theElements);
		assertNotNull(aQueue);

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		assertTrue(aQueue.addAll(theElements));

		assertTrue(aQueue.containsAll(theElements));
		assertEquals(aQueue.size(), theElements.size());

		assertTrue(aQueue.removeAll(theElements));

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test
	public void testCopyConstructor(){
		
		Queue<Integer> theElements = createIntegerQueue(10);
		Queue<Integer> aQueue = new QueueStack<Integer>(theElements);

		assertTrue(aQueue.containsAll(theElements));
		assertEquals(aQueue.size(), theElements.size());

		for (Integer anElement : theElements) {

			assertTrue(aQueue.contains(anElement));
			assertEquals(aQueue.poll(), anElement);
			assertFalse(aQueue.contains(anElement));

		}		
		
		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}
	
	@Test
	public void testEmptyPoll() {

		Queue<Integer> aQueueStack = new QueueStack<Integer>();

		assertNull(aQueueStack.poll());

	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void testOfferNull() {
		
		Queue<Integer> aQueueStack = new QueueStack<Integer>();
		
		aQueueStack.offer(null);
		
	}
	
	@Test(expectedExceptions = NoSuchElementException.class)
	public void testEmptyElement() {

		Queue<Integer> aQueue = new QueueStack<Integer>();

		aQueue.element();

	}

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testRemove(Queue<Integer> theElements, Queue<Integer> aQueue) {

		assertNotNull(theElements);
		assertNotNull(aQueue);

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		for (Integer anElement : theElements) {

			assertTrue(aQueue.offer(anElement));

		}

		assertTrue(aQueue.containsAll(theElements));

		for (Integer anElement : theElements) {

			assertTrue(aQueue.contains(anElement));
			assertEquals(anElement, aQueue.remove());
			assertFalse(aQueue.contains(anElement));

		}

	}

	@Test
	public void testEmptyQueue() {

		Queue<Integer> aQueue = new QueueStack<Integer>();

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test(expectedExceptions = NoSuchElementException.class)
	public void testEmptyRemove() {

		Queue<Integer> aQueue = new QueueStack<Integer>();

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		aQueue.remove();

	}

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testPollOffer(Queue<Integer> theElements, Queue<Integer> aQueue) {

		assertNotNull(theElements);
		assertNotNull(aQueue);

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		aQueue.poll();

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		for (Integer anElement : theElements) {

			assertTrue(aQueue.offer(anElement));
			assertTrue(aQueue.contains(anElement));

		}

		assertEquals(aQueue.size(), theElements.size());
		assertTrue(aQueue.containsAll(theElements));

		for (Integer anElement : theElements) {

			assertTrue(aQueue.contains(anElement));
			assertEquals(anElement, aQueue.poll());
			assertFalse(aQueue.contains(anElement));

		}

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testOfferPoll(Queue<Integer> theElements, Queue<Integer> aQueue) {

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		for (Integer anElement : theElements) {

			assertTrue(aQueue.offer(anElement));
			assertTrue(aQueue.contains(anElement));

		}

		assertEquals(aQueue.size(), theElements.size());
		assertTrue(aQueue.containsAll(theElements));

		for (Integer anElement : theElements) {

			assertTrue(aQueue.contains(anElement));
			assertEquals(aQueue.poll(), anElement);
			assertFalse(aQueue.contains(anElement));

		}

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testToArray(Queue<Integer> theElements, Queue<Integer> aQueue) {

		// Verify the inputs and fill the queue
		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		aQueue.addAll(theElements);

		// Verify the behavior of size and containsAll
		assertEquals(aQueue.size(), theElements.size());
		assertTrue(aQueue.containsAll(theElements));

		Object[] anArray = aQueue.toArray();
		assertEquals(anArray.length, aQueue.size());

		for (int i = 0; i < anArray.length; i++) {

			assertEquals(anArray[i], aQueue.poll());

		}

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test(dataProvider = ELEMENT_PROVIDER)
	public void testToArrayByType(Queue<Integer> theElements,
			Queue<Integer> aQueue) {

		// Verify the inputs and fill the queue
		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		aQueue.addAll(theElements);

		// Verify the behavior of size and containsAll
		assertEquals(aQueue.size(), theElements.size());
		assertTrue(aQueue.containsAll(theElements));

		Integer[] anArray = aQueue.toArray(new Integer[aQueue.size()]);
		assertEquals(anArray.length, aQueue.size());

		for (int i = 0; i < anArray.length; i++) {

			assertEquals(anArray[i], aQueue.poll());

		}

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

	}

	@Test
	public void testEquals() {

		Queue<Integer> aQueue = new QueueStack<Integer>();
		Queue<Integer> thatQueue = new QueueStack<Integer>();

		assertFalse(aQueue.equals(null));
		assertTrue(aQueue.equals(aQueue));
		assertEquals(aQueue.hashCode(), aQueue.hashCode());

		aQueue.offer(1);
		thatQueue.offer(1);

		assertEquals(aQueue, thatQueue);
		assertEquals(aQueue.hashCode(), thatQueue.hashCode());

		thatQueue.offer(2);
		assertFalse(aQueue.equals(thatQueue));
		assertFalse(aQueue.hashCode() == thatQueue.hashCode());

	}

	@DataProvider(name = ELEMENT_PROVIDER)
	public Object[][] provideElements() {

		return new Object[][] {

			{ createIntegerQueue(10), new QueueStack<Integer>() }

		};

	}

}
