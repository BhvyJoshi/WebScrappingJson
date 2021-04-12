package com.ASC.DataProcessingIndividual;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CookGrantorData{

    public static final String subHeaderXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";
    public static final String subTableXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private final static String subTableNextButton = "//*[@id=\"DocList1_LinkButtonNext\"]";
    private final static String groupListButton = "//*[@id=\"TabResultController1_tabItemGroupListtabitem\"]";

    public JSONObject getGrantorGranteeData(WebDriver driver, int row){

        System.out.println("-----------------main table row---->"+row);
        int GrantorCount = Integer.parseInt(driver.findElement(By.xpath(getMainTableRow(row)+"/td[3]")).getText());
        int GranteeCount = Integer.parseInt(driver.findElement(By.xpath(getMainTableRow(row)+"/td[4]")).getText());

        JSONObject grantor= new JSONObject();
        JSONObject grantee = new JSONObject();

        String clickBtn;

        if(GrantorCount!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl02\"]";
            }
            else{
               clickBtn =  "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl02\"]";
            }
            grantor.put("records",getChildObjects(driver, clickBtn));
        }

        if(GranteeCount!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl03\"]";
            }
            else{
                clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl03\"]";
            }
            grantee.put("records",getChildObjects(driver, clickBtn));
        }
        return new JSONObject().put("Grantor_Count__r",grantor).put("Grantee_Count__r",grantee);
    }

    private JSONArray getChildObjects(WebDriver driver, String clickBtn) {
        WebElement btn;
        JSONArray dataRecords;
        try {
            btn = driver.findElement(By.xpath(clickBtn));
            new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOf(btn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", btn);
            Thread.sleep(2000);
            btn.click();
        }catch(Exception e)
            {e.printStackTrace();}
        dataRecords = subTableData(driver);
        try{
            driver.findElement(By.xpath(groupListButton)).click();
            Thread.sleep(2000);
        }catch(Exception e){e.printStackTrace();}

        return dataRecords;
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

    public String getSubTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

    public JSONArray subTableData(WebDriver driver) {
        String[] headers = grabHeader(driver,subHeaderXpath,0);
        JSONArray tableDataContent;
        tableDataContent = grabSubTable(driver, headers);

        boolean checkNext = true;

        while (checkNext) {
            try {
                Thread.sleep(1000);
                WebElement nextBtn = driver.findElement(By.xpath(subTableNextButton));
                Thread.sleep(1000);
                nextBtn.click();
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent, grabSubTable(driver, headers));
            } catch (Exception e1) {
                checkNext = false;
            }
        }
        return tableDataContent;
    }

    public JSONArray grabSubTable(WebDriver driver,String[] subHeader){
        JSONArray objForSubPage = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        JSONObject subAttributes = new JSONObject();

        WebElement subTable =  driver.findElement(By.xpath(subTableXpath));
        int subRowSize = subTable.findElements(By.tagName("tr")).size();

        for (int subRowCount=1;subRowCount<=subRowSize;subRowCount++)
        {
            //System.out.println("---------------subTable row no --->"+subRowCount);
           WebElement subRow = driver.findElement(By.xpath(getSubTableRow(subRowCount)));
           List<WebElement>   subCols = subRow.findElements(By.tagName("td"));

            for (int subColumn = 0, subHdr = 0; (subColumn < subCols.size()); subColumn++, subHdr++) {
                if (subColumn != 0) {
                    objForSubRow.put(subHeader[subHdr - 1], subCols.get(subColumn).getText());
                    while(subHdr-1 == 4){
                        Date dob;
                        try {
                            dob = new SimpleDateFormat("MM/dd/yyyy").parse(subCols.get(subColumn).getText());
                            String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                            objForSubRow.put(subHeader[subHdr-1],str);
                            break;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            subAttributes.put("type", "Grantor_Grantee_Records__c");
            subAttributes.put("referenceId","ref"+subRowCount+"_"+new Random().nextInt(100000));
            objForSubRow.put("attributes",subAttributes);
            objForSubPage.put(objForSubRow);
            objForSubRow = new JSONObject();
            subAttributes = new JSONObject();
        }
        return objForSubPage;
    }

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
        dummyHeader = dummyHeader.replace("Name","Name__c").replace("Trust#","Trust_No__c");
        String[] result = dummyHeader.split(",");
        result = ArrayUtils.remove(result,3); //removing last 2 headers as not required.
        result = ArrayUtils.remove(result,2);
        return result;

    }

    public String[] grabHeader(WebDriver driver, String XPath,int x){ // can be used for 2nd page's data
        String[] hdr = getHeader(driver,XPath);
        hdr = ArrayUtils.remove(hdr,x);
        String dummyHeader = Arrays.toString(hdr);
        dummyHeader = dummyHeader.replace(", ",",");
        dummyHeader = dummyHeader.replace("[","").replace("]","");
        dummyHeader = dummyHeader.replace("Grantor","Grantor__c").replace("Grantee","Grantee__c");
        dummyHeader = dummyHeader.replace("Doc. #","Doc_No__c").replace("Type Desc.","Type_Desc__c");
        dummyHeader = dummyHeader.replace("Recorded Date","Recorded_Date__c").replace("PIN","PIN__c");
        return dummyHeader.split(",");
    }

    public JSONArray appendToList(JSONArray original,JSONArray toBeAppend){
        JSONArray sourceArray = new JSONArray(toBeAppend);
        JSONArray destinationArray = new JSONArray(original);

        for (int i = 0; i < sourceArray.length(); i++) {
            destinationArray.put(sourceArray.getJSONObject(i));
        }
        return destinationArray;
    }
}
