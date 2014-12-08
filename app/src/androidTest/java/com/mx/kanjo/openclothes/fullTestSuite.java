package com.mx.kanjo.openclothes;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by JARP on 12/5/14.
 */
public class fullTestSuite extends TestSuite {


    public static Test suite()
    {
        return  new TestSuiteBuilder(fullTestSuite.class).includeAllPackagesUnderHere().build();
    }

    public fullTestSuite()
    {
        super();
    }
}
