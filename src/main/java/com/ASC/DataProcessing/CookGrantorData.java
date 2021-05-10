package com.ASC.DataProcessing;

import com.ASC.HeaderProcessing.Cook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CookGrantorData extends Cook {

    //public static final String subHeaderXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";

    public static final String subTableXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private final static String subTableNextButton = "//*[@id=\"DocList1_LinkButtonNext\"]";
    private final static String groupListButton = "//*[@id=\"TabResultController1_tabItemGroupListtabitem\"]";

    public JSONArray getGrantorGranteeData(WebDriver driver, int row, JSONObject commonObjData,String requestID,String[] subHeaders,String logFileName){
        String GrantorCount = null,GranteeCount = null;

       while(GranteeCount==null || GranteeCount==null) {
           try{
                GranteeCount = driver.findElement(By.xpath(getMainTableRow(row) + "/td[4]")).getText();
                GrantorCount = driver.findElement(By.xpath(getMainTableRow(row) + "/td[3]")).getText();
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       writeLog("value of grantor count :-----"+GrantorCount,logFileName);
       writeLog("value of grantee count :-----"+GranteeCount,logFileName);
       //System.out.println("value of grantor count :-----"+GrantorCount);
        // System.out.println("value of grantee count :-----"+GranteeCount);
        JSONArray grantor = new JSONArray();
        JSONArray grantee = new JSONArray();
        String clickBtn;

        if(Integer.parseInt(GrantorCount)!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl02\"]";
            }
            else{
               clickBtn =  "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl02\"]";
            }
            grantor = getChildObjects(driver, clickBtn,commonObjData,requestID,subHeaders,logFileName);
        }

        if(Integer.parseInt(GranteeCount)!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl03\"]";
            }
            else{
                clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl03\"]";
            }
            grantee = getChildObjects(driver, clickBtn,commonObjData,requestID,subHeaders,logFileName);
        }
        return appendToListJSON(grantor,grantee);
    }

    private JSONArray getChildObjects(WebDriver driver, String clickBtn,JSONObject commonDataObj,String requestID,String[] subHeaders,String logFileName) {
        JSONArray dataRecords ;

        try {
            Thread.sleep(2000);
            driver.findElement(By.xpath(clickBtn)).click();
            Thread.sleep(1000);
        } catch (Exception e) {
            //System.out.println("inside catch block");
            try{
                new WebDriverWait(driver,60).until(ExpectedConditions.elementToBeClickable(By.xpath(clickBtn)));
            }catch(Exception e1){JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", driver.findElement(By.xpath(clickBtn)));
            }
        }
        writeLog("value is clicked",logFileName);
        //System.out.println("value is clicked");

        dataRecords = subTableData(driver,commonDataObj,requestID,subHeaders,logFileName);
        try{
            driver.findElement(By.xpath(groupListButton)).click();
            Thread.sleep(2000);
        }catch(Exception e){e.printStackTrace();}

        return dataRecords;
    }

    public JSONArray subTableData(WebDriver driver,JSONObject commonObjData,String requestID,String[] subHeaders,String logFileName) {

        JSONArray tableDataContent ;
        tableDataContent = grabSubTable(driver, subHeaders,commonObjData,requestID,logFileName);

        boolean checkNext = true;

        while (checkNext) {
            try {
                Thread.sleep(1000);
                driver.findElement(By.xpath(subTableNextButton)).click();
                Thread.sleep(1000);
                tableDataContent = appendToListJSON(tableDataContent, grabSubTable(driver, subHeaders,commonObjData,requestID,logFileName));
            } catch (Exception e1) {
                checkNext = false;
            }
        }
        return tableDataContent;
    }

    public JSONArray grabSubTable(WebDriver driver,String[] subHeader,JSONObject commonData,String requestID,String logFileName){
        JSONArray objForSubPage = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        JSONObject childRecord = new JSONObject();

        new WebDriverWait(driver,60).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(subTableXpath+"/tr")));
        int subRowSize = driver.findElement(By.xpath(subTableXpath)).findElements(By.tagName("tr")).size();

        for (int subRowCount=1;subRowCount<=subRowSize;subRowCount++){

            writeLog("---------------subTable row no --->"+subRowCount,logFileName);
            //System.out.println("---------------subTable row no --->"+subRowCount);

            new WebDriverWait(driver,30).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getSubTableRow(subRowCount)+"/td")));

            String[] data = new String[6]; //data of each row
            for (int itr = 0; itr<6; itr++){
                new WebDriverWait(driver,10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getSubTableRow(subRowCount)+"/td")));
                String xPath = getSubTableRow(subRowCount)+"/td["+(itr+2)+"]";
                data[itr] = driver.findElement(By.xpath(xPath)).getText();
            }
            for (int itr1 = 2;itr1 <subHeader.length;itr1++){ //mapping of header and data in json object
                while(itr1 == 4){
                    data[itr1] = generateDate(data[itr1]);
                    break;
                }
                objForSubRow.put(subHeader[itr1],data[itr1]);
            }

            childRecord.put("records",childRecords(subHeader,data,subRowCount));
            objForSubRow.put("attributes",putAttributes(subRowCount)); // only add grantor and grantee column
            objForSubRow.put("Grantors__r",childRecord);
            objForSubRow.put("Name",commonData.get("Name"));
            objForSubRow.put("Trust__c",commonData.get("Trust__c"));
            objForSubRow.put("Lead_Search__c",requestID);
            objForSubPage.put(objForSubRow);
            objForSubRow = new JSONObject();
        }
        return objForSubPage;
    }

    private JSONArray childRecords(String[] subHeader, String[] data, int subRowCount){
        JSONObject childRecord1 = new JSONObject();
        JSONObject childRecord2 = new JSONObject();
        JSONArray childRecordArray = new JSONArray();

        childRecord1.put("Type__c",subHeader[0]);
        childRecord1.put("Name",data[0]);// need to change Name__c if required
        childRecord1.put("attributes",putSubAttributes(subRowCount));

        childRecord2.put("Type__c",subHeader[1]);
        childRecord2.put("Name",data[1]); // need to change Name__c if required
        childRecord2.put("attributes",putSubAttributes(subRowCount));

        return childRecordArray.put(childRecord1).put(childRecord2);
    }

    public String getMainTableRow(int count){
        return "//*[@id=\"NameList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }

    public String getSubTableRow(int count){
        return "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody/tr["+count+"]";
    }
}
