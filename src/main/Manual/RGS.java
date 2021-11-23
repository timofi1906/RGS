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

public class RGS {
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        SimpleDateFormat ft = new SimpleDateFormat("MM-dd");
        Map<String, String[]> holidays = new HashMap<>();
        Functions.Holidays(holidays);
        Random random = new Random();

        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(input);

        System.out.println("Please input all your friends which you want add to program \n" +
                "(Name  Surname  Gender  Birthday Date  Mail):");
        ArrayList<String> friends = new ArrayList<>();

        while(true){
            String lineText = br.readLine();
            if(lineText.length() == 0) break;
            friends.add(lineText);
        }
        Csv CS = new Csv(friends);
        CS.InfoToDB();

        while (true) {
            Date date = new Date();
            if (holidays.containsKey(ft.format(date))) {
                ArrayList<String> holiday = new ArrayList<>();
                ArrayList<String> users = new ArrayList<>();
                ParseUrl.PicUrl(holidays.get(ft.format(date))[1], holiday);
                CS.Holiday(users, holidays.get(ft.format(date))[0]);
                String Path = Functions.getImage(holiday.get(random.nextInt(holiday.size())), holidays.get(ft.format(date))[2]);
                SendEmail.SendHolidaySMS(users, "Happy " + holidays.get(ft.format(date))[2], ":)", Path, holidays.get(ft.format(date))[2]);
            }
            ArrayList<String> congratulations = new ArrayList<>();
            ArrayList<String> pictures = new ArrayList<>();
            ParseUrl.PicUrl("https://www.greetingsisland.com/cards/birthday/funny/1", pictures);
            ParseUrl.BirthdayTxt(congratulations);
            ArrayList<String> days = new ArrayList<>();
            CS.FindBday(days, 3);
            for (String bday : days) {
                Gift gift = new Gift(bday, congratulations, pictures);
                gift.BirthDayGreetings();
            }
            System.out.println("Idle");
            TimeUnit.HOURS.sleep(24);
            CS.CloseConnection();
        }
    }
}
