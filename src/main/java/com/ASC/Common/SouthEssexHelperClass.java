package com.ASC.Common;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SouthEssexHelperClass {
    public static final String searchRecordsBtn= "//*[@id=\"ctl00_SampleContent_ImageButton1\"]";
    public static final String startDateText= "//*[@id=\"ASPxPageControl1_RecordedTab_NameSearchStart\"]";
    public static final String startDate = "1/2/1951";
    public static final String lastNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSLastName\"]";
    public static final String firstNameText = "//*[@id=\"ASPxPageControl1_RecordedTab_txtNSFirstName\"]";
    public static final String searchBtn = "//*[@id=\"ASPxPageControl1_RecordedTab_cmdNameSearch\"]";

    //public static final String mainTablePath = "//*[@id=\"ASPxGridView1_DXMainTable\"]/tbody";
    public static final String nextBtnPanel = "//*[@id=\"ASPxGridView1\"]/tbody/tr/td/div[1]";
    /*public static final String nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[10]";*/
    //*[@id="ASPxGridView1_DXPagerTop"]/a[11]
    public static final String headerXpath = "//*[@id=\"ASPxGridView1_DXHeadersRow0\"]";
    //public static final String dataRow = "//*[@id=\"ASPxGridView1_DXDataRow15\"]";
    public static final String searchResultCount = "//*[@id=\"dvSearchTerms\"]/table/tbody/tr[4]/td[1]";
    //first page data row 0 to 14,2nd page 15 to 31 and so on.....


    public void firstPage(WebDriver driver,String keyWord){
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.findElement(By.xpath(searchRecordsBtn)).click();
        driver.findElement(By.xpath(startDateText)).sendKeys(startDate);
        driver.findElement(By.xpath(lastNameText)).sendKeys(keyWord);
       // driver.findElement(By.xpath(firstNameText)).sendKeys("abc");
        driver.findElement(By.xpath(searchBtn)).click();
    }

    public String[] grabHeader(WebDriver driver){
        List<WebElement> headers = driver.findElement(By.xpath(headerXpath)).findElements(By.tagName("td"));
        String[] header = new String[headers.size()];
        for (int i =0;i<headers.size();i++)
        {
            header[i]=headers.get(i).getText();
        }
        header = Arrays.toString(header).replace("\n","").replace("[","").replace("]","").split(",");
        for (int i = 0; i < header.length; i++){
            if(!header[i].trim().equals("") || header[i]!=null)
                header[i] = header[i].trim();
        }
        header = Arrays.stream(header).distinct().toArray(String[]::new);
        header = ArrayUtils.removeAll(header,0,1,2);
        return modifyHeader(header);
    }

    private String[] modifyHeader(String[] hdr)
    {
        String header = Arrays.toString(hdr);
        header = header.replace(", ",",");
        header = header.replace("DATE","DATE__c").replace("ROLE","ROLE__c");
        header = header.replace("Type","Type__c").replace("FIRST PARTY NAME","FIRST_PARTY_NAME__c");
        header = header.replace("Book","Book__c").replace("Page","Page__c");
        header = header.replace("DESC","DESC__c").replace("Street","Street__c");
        header = header.replace("Locus","Locus__c").replace("PBK","PBK__c");
        header = header.replace("PPG","PPG__c").replace("Consideration","Consideration__c");
        header = header.replace("SECOND PARTY NAME","SECOND_PARTY_NAME__c").replace("Town","Town__c");
        header = header.replace("[","").replace("]","");
        return header.split(",");
    }

    public void tableData(WebDriver driver,String fileName,String requestID)
    {
        String[] headers = grabHeader(driver);
        System.out.println("Headers size====="+headers.length);
        JSONArray tableDataContent;
        tableDataContent = grabData(driver,headers,requestID,0,15);
        String searchResult = driver.findElement(By.xpath(searchResultCount)).getText();
        searchResult = searchResult.replace("Result:","").replace("Rows","").trim();
        int noOfLoop = Integer.parseInt(searchResult)/15;
        System.out.println("No of full pages ===="+noOfLoop);
        int lastPageData = Integer.parseInt(searchResult) % 15;
        System.out.println("data in last page ==="+lastPageData);
        int count =0;


        while(count<noOfLoop){
            try{
                String nextBtnClick;
                count ++;
                Thread.sleep(1000);
                if(count == 1) {
                     nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[10]";
                }else {
                     nextBtnClick = "//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[11]";
                }
                WebElement nextBtn = driver.findElement(By.xpath(nextBtnClick));
               /* new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOf(nextBtn));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextBtn);*/
                nextBtn.click();
                System.out.print("\n------------next Button clicked--- page No----"+(count+1));
                Thread.sleep(2000);
                tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*count),(15*count)+15));
                System.out.println("\nvalue of Count is --------"+count);
            }
            catch (Exception e1){
                e1.printStackTrace();
            }
        }
        driver.findElement(By.xpath("//*[@id=\"ASPxGridView1_DXPagerTop\"]/a[11]")).click();
        tableDataContent = appendToList(tableDataContent,grabData(driver,headers,requestID,(15*noOfLoop),(15*noOfLoop)+lastPageData));
       System.out.println("no of objects in Json Array ---->"+tableDataContent.length());
        generateFile(fileName,tableDataContent.toString());
    }

    public void generateFile(String fileName, String fileContent){
        try {
            File myObj = new File("C:\\JsonResponse\\"+fileName+".txt");
            if(myObj.createNewFile()) {
                FileWriter myWriter = new FileWriter("C:\\JsonResponse\\"+fileName+".txt");
                myWriter.write(fileContent);
                myWriter.close();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

   /* private boolean checkForData(WebDriver driver){
        WebElement table = driver.findElement(By.xpath(mainTablePath));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        WebElement column = rows.get(rows.size()-1).findElement(By.tagName("td"));
        String columnText = column.getText();
        return columnText.contains("More names may be available");
    }*/


    public JSONArray grabData(WebDriver driver,String[] header,String requestID,int lowerLimit, int upperLimit)
    {
        JSONArray objForPage = new JSONArray();
        JSONObject objForRow = new JSONObject();

        JSONObject attributes = new JSONObject();
        for (int rowCount=lowerLimit;rowCount<upperLimit;rowCount++)
        {
            WebElement row = driver.findElement(By.xpath(getMainTableRow(rowCount)));
            List<WebElement> cols = row.findElements(By.tagName("td"));
            for (int hdr = 0; (hdr < header.length); hdr++) {
                //for (int column = 0, hdr = 0; (column < 7); column++, hdr++) {
                objForRow.put(header[hdr], cols.get(hdr+2).getText());
                while(hdr == 0) {
                    Date dob;
                    try {
                        dob = new SimpleDateFormat("MM/dd/yyyy").parse(cols.get(hdr+2).getText());
                        String str = new SimpleDateFormat("yyyy-MM-dd").format(dob);
                        objForRow.put(header[hdr], str);
                        break;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            attributes.put("type", "Lead_Search_Result__c");
            attributes.put("referenceId","ref"+rowCount+"_"+new Random().nextInt(100000));
            objForRow.put("attributes",attributes);
            objForRow.put("Lead_Search__c",requestID); //add request id
            objForPage.put(objForRow);
            objForRow = new JSONObject();
        }
        return objForPage;
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

    public String getMainTableRow(int count){
        return "//*[@id=\"ASPxGridView1_DXDataRow"+count+"\"]";
    }
}
