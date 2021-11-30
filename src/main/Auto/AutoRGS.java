package Auto;

import main.Functions;
import main.Gift;
import main.ParseUrl;
import main.SendEmail;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Main class to see work code
 * @author Kovalenko Tim
 * @version 1.0
 * @since   2021-10-18
 */
public class AutoRGS {
    /**
     * Main function with code
     * @throws SQLException if a database access error occurs
     * @throws IOException File not found or wrong path
     * @throws InterruptedException  Thread is waiting, sleeping, or otherwise occupied
     */
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        Random random = new Random();
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd");
        Map<String, String[]> holidays = new HashMap<>();//Empty Map for holidays info
        Functions.Holidays(holidays);//Parse dates

        while (true) {
            AutoCsv AC = new AutoCsv("resources/friend_info.csv");
            AC.CSVtoDB();
            Date date = new Date(); // Every 24 hours get new date
            if (holidays.containsKey(ft.format(date))) { // if date holiday we congratulate all or piece of friends
                ArrayList<String> holiday = new ArrayList<>(); //Empty ArrayList for holiday pictures
                ArrayList<String> users = new ArrayList<>(); //Empty ArrayList for all friends which we congratulate
                ParseUrl.PicUrl(holidays.get(ft.format(date))[1], holiday);//Parse pictures for holiday and add them to array
                AC.Holiday(users, holidays.get(ft.format(date))[0]);//Parse all friends data
                String Path = Functions.getImage(holiday.get(random.nextInt(holiday.size())), holidays.get(ft.format(date))[2]); //Download Image and return path of this Image
                SendEmail.SendHolidaySMS(users, "Happy " + holidays.get(ft.format(date))[2], ":)", Path, holidays.get(ft.format(date))[2]);//Send holiday message to all users
            }
            ArrayList<String> congratulations = new ArrayList<>();//Empty ArrayList for txt congratulations
            ArrayList<String> pictures = new ArrayList<>(); //Empty ArrayList for Birthday pictures
            ParseUrl.PicUrl("https://www.greetingsisland.com/cards/birthday/funny/1", pictures); //Parse urls of birthday pictures
            ParseUrl.BirthdayTxt(congratulations); //Parse birthday congratulation
            ArrayList<String> days = new ArrayList<>(); //Empty ArrayList for friends birthdays
            AC.FindBirthday(days, 0); // Find all birthdays in range (date;date + Integer)
            for (String bday : days) {
                Gift gift = new Gift(bday, congratulations, pictures); // Set Class with params
                gift.BirthDayGreetings();//Greeting each friend
            }
            System.out.println("Idle");
            TimeUnit.HOURS.sleep(24);//Idle 24 hours
            AC.CloseConnection();//Close connection
        }
    }
}
