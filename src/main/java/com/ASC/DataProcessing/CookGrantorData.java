package com.ASC.DataProcessing;

import com.ASC.HeaderProcessing.Cook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CookGrantorData extends Cook {

    public static final String subHeaderXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[1]/table/thead/tr";

    public static final String subTableXpath = "//*[@id=\"DocList1_ContentContainer1\"]/table/tbody/tr[1]/td/div/div[2]/table/tbody";
    private final static String subTableNextButton = "//*[@id=\"DocList1_LinkButtonNext\"]";
    private final static String groupListButton = "//*[@id=\"TabResultController1_tabItemGroupListtabitem\"]";



    public JSONArray getGrantorGranteeData(WebDriver driver, int row, JSONObject commonObjData,String requestID){
        String GrantorCount = null,GranteeCount = null;

       while(GranteeCount==null || GranteeCount==null) {
           try{
                GranteeCount = driver.findElement(By.xpath(getMainTableRow(row) + "/td[4]")).getText();
                GrantorCount = driver.findElement(By.xpath(getMainTableRow(row) + "/td[3]")).getText();
           }catch(Exception e){
               e.printStackTrace();
           }
       }
       System.out.println("value of grantor count :-----"+GrantorCount);
        System.out.println("value of grantee count :-----"+GranteeCount);
        JSONArray grantor = new JSONArray();
        String clickBtn;

        if(Integer.parseInt(GrantorCount)!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl02\"]";
            }
            else{
               clickBtn =  "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl02\"]";
            }

            grantor.put(getChildObjects(driver, clickBtn,commonObjData,requestID));
        }

        if(Integer.parseInt(GranteeCount)!=0){
            if(row<9){
               clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl0"+(row+1)+"_ctl03\"]";
            }
            else{
                clickBtn = "//*[@id=\"NameList1_GridView_NameListGroup_ctl"+(row+1)+"_ctl03\"]";
            }
            grantor = appendToList(grantor,getChildObjects(driver, clickBtn,commonObjData,requestID));
        }
        return grantor;
    }

    private JSONArray getChildObjects(WebDriver driver, String clickBtn,JSONObject commonDataObj,String requestID) {
        JSONArray dataRecords;
        try {
            Thread.sleep(3000);
            //wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(clickBtn))));
            new WebDriverWait(driver, 30).until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(clickBtn))));
            driver.findElement(By.xpath(clickBtn)).click();
            System.out.println("value is clicked");
            Thread.sleep(1000);

        }catch(Exception e)
            {e.printStackTrace();}
        dataRecords = subTableData(driver,commonDataObj,requestID);
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

    public JSONArray subTableData(WebDriver driver,JSONObject commonObjData,String requestID) {
        String[] headers = grabHeader(driver,subHeaderXpath,0);

        JSONArray tableDataContent;
        tableDataContent = grabSubTable(driver, headers,commonObjData,requestID);

        boolean checkNext = true;

        while (checkNext) {
            try {
                Thread.sleep(1000);
                driver.findElement(By.xpath(subTableNextButton)).click();
                Thread.sleep(1000);
                tableDataContent = appendToList(tableDataContent, grabSubTable(driver, headers,commonObjData,requestID));
            } catch (Exception e1) {
                checkNext = false;
            }
        }
        return tableDataContent;
    }

    public JSONArray grabSubTable(WebDriver driver,String[] subHeader,JSONObject commonData,String requestID){
        JSONArray objForSubPage = new JSONArray();
        JSONObject objForSubRow = new JSONObject();
        JSONObject childRecord = new JSONObject();

        int subRowSize = driver.findElement(By.xpath(subTableXpath)).findElements(By.tagName("tr")).size();

        for (int subRowCount=1;subRowCount<=subRowSize;subRowCount++){

            System.out.println("---------------subTable row no --->"+subRowCount);

            //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(getSubTableRow(subRowCount)+"/td")));

            new WebDriverWait(driver,20).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(getSubTableRow(subRowCount)+"/td")));

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
            objForSubRow.put("Name__c",commonData.get("Name__c"));
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
        childRecord1.put("Name__c",data[0]);
        childRecord1.put("attributes",putSubAttributes(subRowCount));

        childRecord2.put("Type__c",subHeader[1]);
        childRecord2.put("Name__c",data[1]);
        childRecord2.put("attributes",putSubAttributes(subRowCount));

        return childRecordArray.put(childRecord1).put(childRecord2);
    }
}
