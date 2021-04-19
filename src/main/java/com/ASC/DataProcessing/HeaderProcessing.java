package com.ASC.DataProcessing;

import org.openqa.selenium.WebDriver;


public interface HeaderProcessing {

    public String[] grabHeader(WebDriver driver);

    public  String[] modifyHeader(String[] hdr);
}
