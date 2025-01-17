package com.ceshiren.hogwarts.frameworkwechat;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SeleniumTestCase extends TestCase{
    private WebDriver driver;
    private WebElement currentElement;

    public SeleniumTestCase(WebDriver driver) {
        this.driver = driver;
    }

    public void run() {
        //System.setProperty("webdriver.chrome.driver","C:\\Aliceyyf\\hogwar\\chrome\\chromedriver.exe");
        //driver=new ChromeDriver();
        steps.forEach(step -> {
            Set<String> keyset=step.keySet();
//            if (step.keySet().contains("chrome")) {
//                driver = new ChromeDriver();
//            }
            if (step.keySet().contains("quit")) {
                driver.quit();
            }

            if (step.keySet().contains("sleep")){
                try {
                    Thread.sleep(Long.valueOf(getValue(step, "sleep").toString()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (step.keySet().contains("implicitly_wait")) {
                driver.manage().timeouts().implicitlyWait(
                        (int) getValue(step, "implicitly_wait", 5), TimeUnit.SECONDS);
            }
            if (step.keySet().contains("get")) {
                driver.get(getValue(step, "get").toString());
            }

            if (step.keySet().contains("find")) {
                ArrayList<By> bys = new ArrayList<>();
                ((HashMap<String, String>) getValue(step, "find")).entrySet().forEach(stringStringEntry -> {
                    if (stringStringEntry.getKey().contains("id")) {
                        bys.add(By.id(stringStringEntry.getValue()));
                    }

                    if (stringStringEntry.getKey().contains("xpath")) {
                        bys.add(By.xpath(stringStringEntry.getValue()));
                    }

                });
                currentElement = driver.findElement(bys.get(0));
            }

            if (step.keySet().contains("click")) {
                currentElement.click();
            }
            if (step.keySet().contains("send_keys")) {
                //todo: 参数化
                currentElement.sendKeys(getValue(step, "send_keys").toString());
            }
        });
    }
}
