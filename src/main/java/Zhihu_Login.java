import java.io.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.os.WindowsUtils;

public class Zhihu_Login {
    public  static  void addCookies() {

        System.getProperties().setProperty("webdriver.chrome.driver", "drivers/chromedriver_windows.exe");
        //开启新WebDriver进程
        WebDriver driver = new ChromeDriver();


        driver.get("http://www.zhihu.com/#signin");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement user = driver
                .findElement(By.xpath("//input[@name='username']"));
        user.clear();
        user.sendKeys("13558714741");
        WebElement password = driver.findElement(By
                .xpath("//input[@name='password']"));
        password.clear();
        password.sendKeys("wang1991");

        WebElement submit = driver.findElement(By
                .xpath("//button[@class='Button SignFlow-submitButton Button--primary Button--blue']"));
        submit.submit();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Set<Cookie> cookies=driver.manage().getCookies();
        Iterator iterator=cookies.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
        File file = new File("broswer.data");
        try {
            // delete file if exists
            file.delete();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (Cookie ck : driver.manage().getCookies()) {
                bw.write(ck.getName() + ";" + ck.getValue() + ";"
                        + ck.getDomain() + ";" + ck.getPath() + ";"
                        + ck.getExpiry() + ";" + ck.isSecure());
                bw.newLine();
            }
            bw.flush();
            bw.close();
            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("cookie write to file");
        }
        driver.quit();
    }
    public static void main(String[] args) throws Exception{
        addCookies();
        WindowsUtils.tryToKillByName("chrome.exe");
        WindowsUtils.getProgramFilesPath();
        System.getProperties().setProperty("webdriver.chrome.driver", "drivers/chromedriver_windows.exe");
        //开启新WebDriver进程
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.zhihu.com/");
        try
        {
            File file=new File("broswer.data");
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);
            String line;
            while((line=br.readLine())!= null)
            {
                StringTokenizer str=new StringTokenizer(line,";");
                while(str.hasMoreTokens())
                {
                    String name=str.nextToken();
                    String value=str.nextToken();
                    String domain=str.nextToken();
                    String path=str.nextToken();
                    Date expiry=null;
                    String dt;
                    if(!(dt=str.nextToken()).equals(null))
                    {
                        //expiry=new Date(dt);
                        System.out.println();
                    }
                    boolean isSecure=new Boolean(str.nextToken()).booleanValue();
                    Cookie ck=new Cookie(name,value,domain,path,expiry,isSecure);
                    driver.manage().addCookie(ck);
                }
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        driver.get("http://www.zhihu.com/");
        driver.quit();
    }
    }

