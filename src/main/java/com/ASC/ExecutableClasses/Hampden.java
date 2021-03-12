package com.ASC.ExecutableClasses;

import com.ASC.Common.InitilizerClass;
import org.testng.annotations.Test;

public class Hampden {

    private String url,value;

    @Test
    public void test(){
        InitilizerClass.initialize(url,value);
    }
}
