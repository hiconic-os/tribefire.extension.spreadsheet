// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package tribefire.extension.spreadsheet.processing.importing.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ParallelProcessing<T> implements AutoCloseable {
	private int threadCount;
	private ExecutorService executor;
	private List<Future<T>> futures = new ArrayList<>();
	
	public ParallelProcessing(int threadCount) {
		this.threadCount = threadCount;
		this.executor = Executors.newFixedThreadPool(threadCount);
	}
	
	public void submitForAllThreads(Callable<T> callable) {
		for (int i = 0; i < threadCount; i++)
			submit(callable);
	}
	
	public void submitForAllThreads(Runnable runnable) {
		submitForAllThreads(callableFromRunnable(runnable));
	}
	
	private Callable<T> callableFromRunnable(Runnable runnable) {
		return () -> {
			runnable.run();
			return null;
		};
	}
	
	public void submit(Callable<T> callable) {
		futures.add(executor.submit(callable));
	}
	
	public void submit(Runnable runnable) {
		submit(callableFromRunnable(runnable));
	}
	
	public int getThreadCount() {
		return threadCount;
	}
	
	public Stream<T> stream(Supplier<? extends Exception> exceptionSupplier) {
		return futures.stream().map(f -> {
			try {
				return f.get();
			} catch (Exception e) {
				exceptionSupplier.get().addSuppressed(e);
				return null;
			}
		}).filter(t -> t != null);
	}
	
	public void consumeResults(Supplier<? extends Exception> exceptionSupplier) {
		stream(exceptionSupplier).count();
	}

	@Override
	public void close() {
		try {
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// noop
		}
	}

}
