/*
 * Copyright contributors to the Galasa project 
 */
package dev.galasa.framework; 

import static org.junit.Assert.assertArrayEquals;

import java.util.List;

import org.junit.Test;

public class TestResultNames {

    @Test
    public void TestGetDefaultResultNames(){
        List<String> resultNames = ResultNames.getDefaultResultNames();
        assertArrayEquals(resultNames.toArray(), new String[]{"Ignored","Passed","Failed","EnvFail"});
    } 
}