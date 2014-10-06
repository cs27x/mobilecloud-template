/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.videoup.client;

import java.util.concurrent.Callable;

import android.os.AsyncTask;
import android.util.Log;

/**
 *
 * A class that is designed to provide a simpler interface to Android's
 * AsyncTask and allow exceptions to be reported back in the UI thread.
 * Rather than needing to subclass AsyncTask, you simply call:
 *
 <pre>
 * {@code
 *
 *  CallableTask.invoke(
 *      new Callable<Collection<Video>>() {
                @Override
                public Collection<Video> call() throws Exception {
                     // Code to execute in the background
                }
            },
            new TaskCallback<Collection<Video>>() {

                @Override
                public void success(Collection<Video> result) {
                     // Code to execute in the UI thread if the
                     // background operation succeeds
                }

                @Override
                public void error(Exception e) {
                     // Code to execute in the UI thread if the
                     // background operation fails
                }
            }
 *  );
 *
 * }
 * @param <T>
 */
public class CallableTask<T> extends AsyncTask<Void,Double,T> {

    private static final String TAG = CallableTask.class.getName();

    public static <V> void invoke(Callable<V> call, TaskCallback<V> callback){
        new CallableTask<V>(call, callback).execute();
    }

    private Callable<T> callable_;

    private TaskCallback<T> callback_;
    
    private Exception error_;

    public CallableTask(Callable<T> callable, TaskCallback<T> callback) {
        callable_ = callable;
        callback_ = callback;
    }

    @Override
    protected T doInBackground(Void... ts) {
        T result = null;
        try{
            result = callable_.call();
        } catch (Exception e){
            Log.e(TAG, "Error invoking callable in AsyncTask callable: "+callable_, e);
            error_ = e;
        }
        return result;
    }

    @Override
    protected void onPostExecute(T r) {
    	if(error_ != null){
    		callback_.error(error_);
    	}
    	else {
    		callback_.success(r);
    	}
    }
}

