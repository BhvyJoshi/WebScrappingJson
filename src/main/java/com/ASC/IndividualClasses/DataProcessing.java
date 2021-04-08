package com.ASC.IndividualClasses;

import org.openqa.selenium.WebDriver;

public abstract class DataProcessing {

    public abstract void grabHeaders();
    public abstract void getFirstPageData();
    public abstract void getWholeData();
    public abstract void mergeData();
    public abstract void fileCreate();

    public final void ExecuteSequence(Object driver){

        grabHeaders();
        getWholeData();
        mergeData();
        fileCreate();
    }

}
