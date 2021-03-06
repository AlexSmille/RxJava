package catsend.rxjava;

public abstract class AsyncJob<T> {

	public abstract void start(Callback<T> callback);

	
	public <R> AsyncJob<R> map(final Func<T,R> func){
		final AsyncJob<T> source = this;
		
		return new AsyncJob<R>(){

			@Override
			public void start(final Callback<R> callback) {
				source.start(new Callback<T>() {

					@Override
					public void onResult(T result) {
						
						R r = func.call(result);
						callback.onResult(r);
					}

					@Override
					public void onError(Exception e) {
						callback.onError(e);
						
					}
				});
			}
			
		};
		
	}
	
	
	
	public <R> AsyncJob<R> flatMap(final Func<T, AsyncJob<R>> func ){
		final AsyncJob<T> source = this;
		return new AsyncJob<R>(){

			@Override
			public void start(final Callback<R> callback) {
				source.start(new Callback<T>() {

					@Override
					public void onResult(T result) {
						AsyncJob<R> mapped = func.call(result);
						mapped.start(callback);
					}

					@Override
					public void onError(Exception e) {
						callback.onError(e);
						
					}
				});
				
			}
			
		};
	}
}
