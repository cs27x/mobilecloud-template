package com.magnum.videoup.test;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

public class RunTests extends TestSuite {
    public static Test suite() {
        return new TestSuiteBuilder(RunTests.class).includeAllPackagesUnderHere().build();
    }
}
