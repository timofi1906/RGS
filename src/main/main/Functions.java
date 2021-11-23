package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Functions {

    public static String getZodiac(String date) {
        String[] data  = date.split("-");
        int month = Integer.parseInt(data[1]);
        int day = Integer.parseInt(data[2]);

        if ((month == 12 && day >= 22 && day <= 31) || (month == 1 && day >= 1 && day <= 19))
            return "Capricorn";
        else if ((month == 1 && day >= 20 && day <= 31) || (month == 2 && day >= 1 && day <= 17))
            return "Aquarius";
        else if ((month == 2 && day >= 18 && day <= 29) || (month == 3 && day >= 1 && day <= 19))
            return "Pisces";
        else if ((month == 3 && day >= 20 && day <= 31) || (month == 4 && day >= 1 && day <= 19))
            return "Aries";
        else if ((month == 4 && day >= 20 && day <= 30) || (month == 5 && day >= 1 && day <= 20))
            return "Taurus";
        else if ((month == 5 && day >= 21 && day <= 31) || (month == 6 && day >= 1 && day <= 20))
            return "Gemini";
        else if ((month == 6 && day >= 21 && day <= 30) || (month == 7 && day >= 1 && day <= 22))
            return "Cancer";
        else if ((month == 7 && day >= 23 && day <= 31) || (month == 8 && day >= 1 && day <= 22))
            return "Leo";
        else if ((month == 8 && day >= 23 && day <= 31) || (month == 9 && day >= 1 && day <= 22))
            return "Virgo";
        else if ((month == 9 && day >= 23 && day <= 30) || (month == 10 && day >= 1 && day <= 22))
            return "Libra";
        else if ((month == 10 && day >= 23 && day <= 31) || (month == 11 && day >= 1 && day <= 21))
            return "Scorpio";
        else if ((month == 11 && day >= 22 && day <= 30) || (month == 12 && day >= 1 && day <= 21))
            return "Sagittarius";
        else
            return "Illegal date";
    }

    public static String FriendYear(String sdate){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(sdate, formatter);
        LocalDate endDate = LocalDate.parse(strDate, formatter);
        Period period = Period.between(startDate, endDate);
        return "%s".formatted(period.getYears());
    }

    public static boolean isFamily(String line){
        return !line.toLowerCase().contains("sister") && !line.toLowerCase().contains("boss")
                && !line.toLowerCase().contains("brother") && !line.toLowerCase().contains("mom")
                && !line.toLowerCase().contains("dad") && !line.toLowerCase().contains("girlfriend") &&
                !line.toLowerCase().contains("name") && !line.toLowerCase().contains("[") &&
                !line.toLowerCase().contains("daughter") && !line.toLowerCase().contains("aunt") ;

    }

    public static void Holidays(Map<String,String[]> table){
        String[] christmas = {"All", "https://www.greetingsisland.com/cards/holidays/christmas/1","Christmas"}; //Christmas
        table.put("12-25", christmas);

        String[] easter = {"All","https://www.greetingsisland.com/cards/holidays/easter/1","Easter"}; //Easter
        table.put("04-17", easter);

        String[] womansday = {"Female","https://www.greetingsisland.com/cards/thoughts-and-feelings/sympathy/1","Woman Day"}; // Woman Day
        table.put("03-08", womansday);

        String[] newyear = {"All","https://www.greetingsisland.com/cards/holidays/new-year/1","New Year"}; // New Year
        table.put("01-01", newyear);
    }

    /**
     * Function for download images.
     * @param Url Url for Image
     * @return Path to download file
     */
    public static String getImage(String Url, String Name){
        BufferedImage image;
        String pathname = "";
        try{

            URL url =new URL(Url);
            image = ImageIO.read(url);
            pathname = "resources/" + Name + ".jpg";
            ImageIO.write(image, "jpg",new File(pathname));

        }catch(IOException e){
            e.printStackTrace();
        }
        return pathname;
    }

}
