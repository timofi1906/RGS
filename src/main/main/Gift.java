package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 *  Class Gift work with parsing Url Sites and find in websites gifts for friends by some attributes.
 * @author Kovalenko Tim
 * @version 1.0
 * @since   2021-10-18
 */
public class Gift{
    private final ArrayList<String> congratulations;
    private final ArrayList<String> pictures;
    protected String FirstName;
    protected String LastName;
    protected String Age;
    protected String Gender;
    protected String Zodiac;
    protected String Email;
    protected Map<String, String> gifts = new HashMap();
    Random random = new Random();


    public Gift(String Friend, ArrayList<String> congratulations, ArrayList<String> pictures){
        this.congratulations = congratulations;
        this.pictures = pictures;

        String[] friend = Friend.split("\t");
        this.FirstName = friend[0];
        this.LastName = friend[1];
        this.Gender = friend[2];
        this.Age = Functions.FriendYear(friend[3]);
        this.Zodiac = friend[4];
        this.Email = friend[5];
    }

    /**
     * Function for find gifts
     * @param Url Url of Site with gifts
     */
    private void FindGifts(String Url) {
        Map<String, String> name = new HashMap();
        try {
            Document page = Jsoup.connect(Url).get();
            Elements names = page.select("a");
            for (Element e : names){
                if (e.attr("title").length() != 0 && Functions.WordFilter(e.attr("title"))){

                    String url = e.attr("href");
                    Pattern pattern = Pattern.compile("[0-9]{9,}");
                    Matcher matcher = pattern.matcher(url);
                    if (matcher.find()) name.put(e.attr("title"), "https://www.etsy.com/listing/" + matcher.group());
                }
            }
            List<String> keysAsArray = new ArrayList<>(name.keySet());
            int num = random.nextInt(keysAsArray.size());
            String n = name.get(keysAsArray.get(num));
            gifts.put(keysAsArray.get(num), n);
        }catch (IOException ioException){
            ioException.printStackTrace();
        }catch (IllegalArgumentException ignored){}
    }


    /**
     * Concate all info together and showing lists with gifts
     * @throws IOException Wrong path or file not found
     */
    public void BirthDayGreetings() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));

        FindGifts("https://www.etsy.com/search?q=name%20" + FirstName);
        FindGifts("https://www.etsy.com/search?q=" + Age + "th+birthday+gift" + Gender);
        FindGifts("https://www.etsy.com/search?q=zodiac%20" + Zodiac);
        List<String> keysAsArray = new ArrayList<>(gifts.keySet());
        System.out.printf("Gift for %s%n", FirstName + " " + LastName + " " + Age + " " + Gender);
        for(String e : keysAsArray){
            System.out.printf("%s\n%s\n", e,gifts.get(e));
        }
        String Subject = "Dear %s it's your %sth birthday :)".formatted(FirstName + " " + LastName,Age);
        String Text = "With best wishes to you : " + congratulations.get(random.nextInt(congratulations.size()));
        System.out.println(Text);
        System.out.print("Do yo want change message? (Y / N) : ");
        String change = reader.readLine();
        if (change.toLowerCase(Locale.ROOT).equals("y")){
            Text = reader.readLine();
        }

        String Path = Functions.getImage(pictures.get(random.nextInt(pictures.size())),FirstName);
        SendEmail.SendBirthdaySMS(Email, Subject, Text, Path , FirstName + LastName);
    }
}

