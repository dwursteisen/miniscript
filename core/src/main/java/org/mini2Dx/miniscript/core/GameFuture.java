/**
 * The MIT License (MIT)
 * 
 * Copyright (c) 2016 Thomas Cashman
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.mini2Dx.miniscript.core;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.mini2Dx.miniscript.core.exception.ScriptSkippedException;

/**
 * Represents a task that will complete in-game at a future time
 */
public abstract class GameFuture {
	private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
	private final AtomicBoolean futureSkipped = new AtomicBoolean(false);
	private final AtomicBoolean scriptSkipped = new AtomicBoolean(false);
	private final AtomicBoolean completed = new AtomicBoolean(false);

	/**
	 * Constructor
	 * 
	 * @param gameScriptingEngine
	 *            The {@link GameScriptingEngine} this future belongs to
	 */
	public GameFuture(GameScriptingEngine gameScriptingEngine) {
		gameScriptingEngine.submitGameFuture(this);
	}

	/**
	 * Update the {@link GameFuture}
	 * 
	 * @param delta
	 *            The amount of time (in seconds) since the last frame
	 * @return True if the {@link GameFuture} has completed
	 */
	protected abstract boolean update(float delta);

	/**
	 * Called when the {@link GameFuture} is skipped
	 */
	protected abstract void onFutureSkipped();

	/**
	 * Called when the entire script is skipped
	 */
	protected abstract void onScriptSkipped();

	void evaluate(float delta) {
		if (update(delta)) {
			complete();
		}
	}

	private void complete() {
		completed.set(true);
		synchronized (this) {
			notifyAll();
		}
	}

	public void skipFuture() {
		futureSkipped.set(true);
		onFutureSkipped();
		synchronized (this) {
			notifyAll();
		}
	}

	/**
	 * Can be called in a script to wait for this {@link GameFuture} to complete
	 * 
	 * @throws ScriptSkippedException
	 *             Thrown when the script is skipped
	 */
	public void waitForCompletion() throws ScriptSkippedException {
		while (!completed.get()) {
			if (futureSkipped.get()) {
				return;
			}
			if (scriptSkipped.get()) {
				onScriptSkipped();
				throw new ScriptSkippedException();
			}
			try {
				synchronized (this) {
					wait();
				}
			} catch (InterruptedException e) {
				scriptSkipped.set(true);
				onScriptSkipped();
				throw new ScriptSkippedException();
			}
		}
	}

	/**
	 * Returns if this {@link GameFuture} is complete
	 * 
	 * @return True if this completed without being skipped
	 */
	public boolean isCompleted() {
		return completed.get();
	}

	/**
	 * Returns if this {@link GameFuture} was skipped
	 * 
	 * @return True if this {@link GameFuture} was skipped
	 */
	public boolean isFutureSkipped() {
		return futureSkipped.get();
	}

	/**
	 * Returns if the script was skipped
	 * 
	 * @return True if this {@link GameFuture} was waiting for completion when
	 *         the script was skipped
	 */
	public boolean isScriptSkipped() {
		return scriptSkipped.get();
	}

	/**
	 * Returns this unique id of this {@link GameFuture} instance
	 * @return
	 */
	public int getFutureId() {
		return ID_GENERATOR.incrementAndGet();
	}
}