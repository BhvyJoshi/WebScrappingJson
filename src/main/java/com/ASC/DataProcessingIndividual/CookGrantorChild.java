package com.ASC.DataProcessingIndividual;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.Arrays;
import java.util.List;

public class CookGrantorChild {


    public String[] getHeader(WebDriver driver, String xPath) {
        WebElement headerLine = driver.findElement(By.xpath(xPath));
        List<WebElement> headers = headerLine.findElements(By.tagName("th"));
        String[] header = new String[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            header[i] = headers.get(i).getText();
        }
        return header;
    }

    public String[] grabHeader(WebDriver driver, String XPath){ //can be used for first page's data
        String dummyHeader = Arrays.toString(getHeader(driver,XPath));
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        return dummyHeader.split(",");

    }

    public String[] grabHeader(WebDriver driver, String XPath,int x){ // can be used for 2nd page's data
        String[] hdr = getHeader(driver,XPath);
        hdr = ArrayUtils.remove(hdr,x);
        String dummyHeader = Arrays.toString(hdr);
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        return dummyHeader.split(",");
    }

    public JSONArray appendToList(JSONArray original,JSONArray toBeAppend)
    {
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }


    public void getGrantorGranteeChildData(WebDriver driver, int rowCount){

    }
}
