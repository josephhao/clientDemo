package com.cw.test;


import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;

import org.testng.annotations.Test;
import sun.net.www.http.HttpClient;

import java.io.IOException;


@PrepareForTest({Utils.class, HttpClient.class})
public class UtilsTest extends PowerMockTestCase{


    @Test
    public void  test () {
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.getCurrentSec()).thenReturn(1L);
        Assert.assertEquals(Utils.getCurrentSec(), 1L, "expect 1s");
        PowerMockito.verifyStatic();
    }

    @Test
    public void testReflectionMethod() {
        PowerMockito.mockStatic(Utils.class);
        RelectionMethod relectionMethod = PowerMockito.mock(RelectionMethod.class);
        PowerMockito.when(Utils.invokeReflectionMethod(relectionMethod)).thenReturn(true);
        Assert.assertTrue(Utils.invokeReflectionMethod(relectionMethod), "expect true");
        PowerMockito.verifyStatic();
    }



}
