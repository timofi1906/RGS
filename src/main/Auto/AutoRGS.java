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

public class AutoRGS {

    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        Random random = new Random();
        AutoCsv AC = new AutoCsv("resources/friend_info.csv");
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd");
        Map<String, String[]> holidays = new HashMap<>();
        Functions.Holidays(holidays);
        AC.CSVtoDB();

        while (true) {
            Date date = new Date();
            if (holidays.containsKey(ft.format(date))) {
                ArrayList<String> holiday = new ArrayList<>();
                ArrayList<String> users = new ArrayList<>();
                ParseUrl.PicUrl(holidays.get(ft.format(date))[1], holiday);
                AC.Holiday(users, holidays.get(ft.format(date))[0]);
                String Path = Functions.getImage(holiday.get(random.nextInt(holiday.size())), holidays.get(ft.format(date))[2]);
                SendEmail.SendHolidaySMS(users, "Happy " + holidays.get(ft.format(date))[2], ":)", Path, holidays.get(ft.format(date))[2]);
            }
            ArrayList<String> congratulations = new ArrayList<>();
            ArrayList<String> pictures = new ArrayList<>();
            ParseUrl.PicUrl("https://www.greetingsisland.com/cards/birthday/funny/1", pictures);
            ParseUrl.BirthdayTxt(congratulations);
            ArrayList<String> days = new ArrayList<>();
            AC.FindBirthday(days, 0);
            for (String bday : days) {
                Gift gift = new Gift(bday, congratulations, pictures);
                gift.BirthDayGreetings();
            }
            System.out.println("Idle");
            TimeUnit.HOURS.sleep(24);
            AC.CloseConnection();
        }
    }
}
