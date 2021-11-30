package Manual;

import main.Functions;
import main.Gift;
import main.ParseUrl;
import main.SendEmail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *  No Automatic class for tests
 *
 * @author  Kovalenko Tim
 * @version 1.0
 * @since   2021-10-18
 */

public class RGS {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd");
        Map<String, String[]> holidays = new HashMap<>();//Empty Map for holidays info
        Functions.Holidays(holidays);//Parse dates
        Random random = new Random();

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(input);

        System.out.println("Please input all your friends which you want add to program \n" +
                "(Name  Surname  Gender  Birthday Date  Mail):");
        System.out.println("Enter to end.");
        ArrayList<String> friends = new ArrayList<>();

        while(true){
            String lineText = br.readLine();
            if(lineText.length() == 0) break;
            friends.add(lineText);
        }


        while (true) {
            Csv CS = new Csv(friends);
            CS.InfoToDB();
            Date date = new Date(); // Every 24 hours get new date
            if (holidays.containsKey(ft.format(date))) {// if date holiday we congratulate all or piece of friends
                ArrayList<String> holiday = new ArrayList<>();//Empty ArrayList for holiday pictures
                ArrayList<String> users = new ArrayList<>();//Empty ArrayList for all friends which we congratulate
                ParseUrl.PicUrl(holidays.get(ft.format(date))[1], holiday);//Parse pictures for holiday and add them to array
                CS.Holiday(users, holidays.get(ft.format(date))[0]);//Parse all friends data
                String Path = Functions.getImage(holiday.get(random.nextInt(holiday.size())), holidays.get(ft.format(date))[2]);//Download Image and return path of this Image
                SendEmail.SendHolidaySMS(users, "Happy " + holidays.get(ft.format(date))[2], ":)", Path, holidays.get(ft.format(date))[2]);//Send holiday message to all users
            }
            ArrayList<String> congratulations = new ArrayList<>();//Empty ArrayList for txt congratulations
            ArrayList<String> pictures = new ArrayList<>(); //Empty ArrayList for Birthday pictures
            ParseUrl.PicUrl("https://www.greetingsisland.com/cards/birthday/funny/1", pictures); //Parse urls of birthday pictures
            ParseUrl.BirthdayTxt(congratulations); //Parse birthday congratulation
            ArrayList<String> days = new ArrayList<>(); //Empty ArrayList for friends birthdays
            CS.FindBirthday(days, 0); // Find all birthdays in range (date;date + Integer)
            for (String bday : days) {
                Gift gift = new Gift(bday, congratulations, pictures); // Set Class with params
                gift.BirthDayGreetings();//Greeting each friend
            }
            System.out.println("Idle");
            TimeUnit.HOURS.sleep(24);//Idle 24 hours
            CS.CloseConnection();
        }
    }
}
