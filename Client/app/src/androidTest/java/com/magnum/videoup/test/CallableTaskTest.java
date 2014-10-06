package com.magnum.videoup.test;

import android.os.Looper;

import junit.framework.TestCase;

import org.magnum.videoup.client.CallableTask;
import org.magnum.videoup.client.TaskCallback;

import java.util.concurrent.Callable;

/**
 *
 * Created by jules on 10/6/14.
 */
public class CallableTaskTest extends TestCase {

    private boolean notified_ = false;
    private boolean backgroundWorkCalledInNonUIThread_ = false;

    public void testInvokeNoError() throws Exception {
        final Object lock = new Object();

        CallableTask.invoke(new Callable<Object>(){
            @Override
            public Object call() throws Exception {
                backgroundWorkCalledInNonUIThread_ = Looper.myLooper() != Looper.getMainLooper();
                return null;
            }
        }, new TaskCallback<Object>() {
            @Override
            public void success(Object result) {
                synchronized (lock){
                    // make sure that we are on the UI thread
                    notified_ = Looper.myLooper() == Looper.getMainLooper();
                    lock.notify();
                }
            }

            @Override
            public void error(Exception e) {
                synchronized (lock) {
                    notified_ = false;
                    lock.notify();
                }
            }
        });

        synchronized (lock) {
            lock.wait(1000);

            if(!backgroundWorkCalledInNonUIThread_){
                fail("The background work was run in the UI thread rather than a background thread");
            }
            else if(!notified_){
                fail("Failed to be notified of the result of the background task (or an error occurred)");
            }
        }
    }


    public void testInvokeWithError() throws Exception {
        final Object lock = new Object();

        CallableTask.invoke(new Callable<Object>(){
            @Override
            public Object call() throws Exception {
                backgroundWorkCalledInNonUIThread_ = Looper.myLooper() != Looper.getMainLooper();
                throw new RuntimeException("Should be caught and passed to error handler");
            }
        }, new TaskCallback<Object>() {
            @Override
            public void success(Object result) {
                synchronized (lock){
                    // Make the com.magnum.videoup.test fail
                    notified_ = false;
                    lock.notify();
                }
            }

            @Override
            public void error(Exception e) {
                synchronized (lock){
                    // make sure that we are on the UI thread
                    notified_ = Looper.myLooper() == Looper.getMainLooper();
                    lock.notify();
                }
            }
        });

        synchronized (lock) {
            lock.wait(1000);

            if(!backgroundWorkCalledInNonUIThread_){
                fail("The background work was run in the UI thread rather than a background thread");
            }
            else if(!notified_){
                fail("Failed to be notified of the exception");
            }
        }
    }

}
