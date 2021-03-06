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
package org.mini2Dx.miniscript.lua;

import org.mini2Dx.miniscript.core.GameScriptingEngine;
import org.mini2Dx.miniscript.core.ScriptExecutorPool;

/**
 * An implementation of {@link GameScriptingEngine} for Lua scripts
 */
public class LuaGameScriptingEngine extends GameScriptingEngine {
	/**
	 * Constructs a scripting engine backed by a thread pool with the maximum
	 * amount of concurrent scripts set to the amount of processors + 1;
	 */
	public LuaGameScriptingEngine() {
		super();
	}

	/**
	 * Constructs a scripting engine backed by a thread pool.
	 * 
	 * @param maxConcurrentScripts
	 *            The maximum amount of concurrently running scripts. Note this
	 *            is a 'requested' amount and may be less due to the amount of
	 *            available processors on the player's machine.
	 */
	public LuaGameScriptingEngine(int maxConcurrentScripts) {
		super(maxConcurrentScripts);
	}
	
	/**
	 * Constructs a scripting engine backed by a thread pool with the maximum
	 * amount of concurrent scripts set to the amount of processors + 1.
	 * 
	 * @param sandboxed
	 *            True if script sandboxing should be enabled
	 */
	public LuaGameScriptingEngine(boolean sandboxed) {
		super(sandboxed);
	}
	
	/**
	 * Constructs a scripting engine backed by a thread pool.
	 * 
	 * @param maxConcurrentScripts
	 *            The maximum amount of concurrently running scripts. WARNING:
	 *            this is a 'requested' amount and may be less due to the amount
	 *            of available processors on the player's machine.
	 * @param sandboxed
	 *            True if script sandboxing should be enabled
	 */
	public LuaGameScriptingEngine(int maxConcurrentScripts, boolean sandboxed) {
		super(maxConcurrentScripts, sandboxed);
	}

	@Override
	protected ScriptExecutorPool<?> createScriptExecutorPool(int poolSize, boolean sandboxed) {
		return new LuaScriptExecutorPool(this, poolSize, sandboxed);
	}

	@Override
	public boolean isSandboxingSupported() {
		return true;
	}
}
