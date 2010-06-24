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

import static java.lang.String.*;
import static net.cockamamy.playpen.queuestack.TestUtilities.*;
import static org.testng.Assert.*;

import java.util.*;

import org.testng.annotations.*;

@Test(groups = "performance")
public final class PerformanceTest {

	private static final String PERFORMANCE_PROVIDER = "offer_poll_data";

	@Test(dataProvider = PERFORMANCE_PROVIDER)
	public void testPerformance(Queue<Integer> theElements) {

		assertNotNull(theElements,
				"testOfferPoll(List) requires a non-null list of elements");

		Timer anOfferTimer = new Timer();
		anOfferTimer.start();
		Queue<Integer> aQueue = new QueueStack<Integer>();
		for (Integer anElement : theElements) {

			aQueue.offer(anElement);

		}
		anOfferTimer.stop();

		assertTrue(aQueue.containsAll(theElements));
		assertEquals(aQueue.size(), theElements.size());

		Timer aPollTimer = new Timer();
		aPollTimer.start();
		while (aQueue.isEmpty() == false) {

			aQueue.poll();

		}
		aPollTimer.stop();

		assertTrue(aQueue.isEmpty());
		assertEquals(aQueue.size(), 0);

		System.out
				.println(format(
						"Offer for %1$s elements is %2$s nanoseconds per element and %3$s nanoseconds total.",
						theElements.size(),
						(anOfferTimer.getElasped() / theElements.size()),
						anOfferTimer.getElasped()));
		System.out
				.println(format(
						"Poll for %1$s elements is %2$s nanoseconds per element and %3$s nanoseconds total",
						theElements.size(),
						(aPollTimer.getElasped() / theElements.size()),
						aPollTimer.getElasped()));

	}

	@DataProvider(name = PERFORMANCE_PROVIDER)
	public Object[][] provideOfferPollData() {

		Object[][] theData = new Object[5][1];

		theData[0][0] = createIntegerQueue(1);
		theData[1][0] = createIntegerQueue(10);
		theData[2][0] = createIntegerQueue(100);
		theData[3][0] = createIntegerQueue(1000);
		theData[4][0] = createIntegerQueue(10000);

		return theData;

	}

	private static class Timer {

		private Long myStartTime = null;
		private Long myStopTime = null;

		public void start() {

			assert this.myStartTime == null : "Timer has already been started.";
			assert this.myStopTime == null : "Timer has already been stopped.";

			this.myStartTime = System.nanoTime();

		}

		public void stop() {

			assert this.myStartTime != null : "Timer has not been started";
			assert this.myStopTime == null : "Timer has already been stopped.";

			this.myStopTime = System.nanoTime();

		}

		public Long getElasped() {

			assert this.myStartTime != null : "Timer has not been started.";
			assert this.myStopTime != null : "Timer has not been stopped.";

			return (this.myStopTime - this.myStartTime);

		}

	}

}
