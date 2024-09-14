import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SeleniumCardScraper {

    public static String[] add(String[] array, String line){
        String[] newArray = new String[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = line;
        return newArray;
    }

    public static void main(String[] args) {
        // Imposta il percorso del driver Chrome
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");

        WebDriver driver = new ChromeDriver();
        String[] cardTexts = new String[0];

        try {
            for (int i = 1; i <= 10; i++) {
                String url = String.format("https://swudb.com/card/SHD/%03d", i);
                driver.get(url);

                WebElement element = driver.findElement(By.className("pb-1"));
                String cardText = element.getText();
                cardTexts = add(cardTexts, cardText);

                System.out.println("Testo dalla carta " + i + ": " + cardText);
            }
        } finally {
            driver.quit();
        }

        // Ora puoi fare qualcosa con la lista cardTexts
    }
}