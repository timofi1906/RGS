package Manual;

import main.Functions;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 *  Class CSWrite which connect to database table
 *      and add ifo about friends.
 *
 * @author  Kovalenko Tim
 * @version 1.0
 * @since   2021-10-18
 */

public class Csv {

    protected final ArrayList<String> friends;

    public Csv(ArrayList<String> friends) throws SQLException {

        this.friends = friends;
    }

    String url = "jdbc:mysql://localhost:3306/friends_info";
    Connection connection = DriverManager.getConnection(url , "root", "");
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");

    /**
     * Function CSVtoDB to connect to data table and input info about friends.
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public void InfoToDB () throws SQLException {

        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        IsEmptyTable(statement);

        String sql = "INSERT INTO friends(FirstName,LastName,Gender,BDate,ZodiacS,Email) VALUES(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        AddFriendToTable(preparedStatement,friends);
        ClearCopy(statement);
    }

    /**
     * Function to add friends to table
     * @param preparedStatement Prepared Statement
     * @param friends Array List with friends
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public void AddFriendToTable(PreparedStatement preparedStatement,ArrayList<String> friends) throws SQLException {
        for(String f : friends){
            String[] info = f.split(" ");
            String name, surname, gender,bdate,zodiac, mail;

            name = info[0];
            surname = info[1];
            gender = info[2];
            bdate = info[3];
            mail = info[4];
            zodiac = Functions.getZodiac(bdate);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, gender);
            preparedStatement.setDate(4, java.sql.Date.valueOf(info[3]));
            preparedStatement.setString(5,zodiac);
            preparedStatement.setString(6, mail);
            preparedStatement.addBatch();
            System.out.println("Data has been inserted successfully.");
        }

        preparedStatement.executeBatch();
    }
    /**
     * Function to find the closest Day of birth.
     * @param list Empty list for info
     * @param days Set interval between  now Date and Date + days
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void FindBirthday(ArrayList<String> list, int days) throws SQLException {


        Statement statement = connection.createStatement();
        Date date = new Date();
        Date date_add = DateAdd(date, days);

        String query = "SELECT  * FROM friends tbl WHERE DATE_FORMAT(tbl.Bdate, '%m-%d') BETWEEN " +
                "'%s' AND '%s'".formatted(formatter.format(date),formatter.format(date_add)) +
                " ORDER BY DAYOFMONTH(Bdate) ASC;";

        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(resultSet.getString("FirstName") + "\t" +
                    resultSet.getString("LastName")  + "\t" +
                    resultSet.getString("Gender") + "\t" +
                    resultSet.getDate("BDate")  + "\t" +
                    resultSet.getString("ZodiacS")  + "\t" +
                    resultSet.getString("Email"));}
        resultSet.close();
        statement.close();

    }
    public void Holiday(ArrayList<String> list, String Gender) throws SQLException {

        String query ;
        Statement statement = connection.createStatement();
        if(!Gender.toLowerCase().contains("all")){
            query = "SELECT Email FROM friends_info.friends WHERE Gender = %s ;".formatted(Gender);

        }
        else {
            query = "SELECT Email FROM friends_info.friends ;";
        }
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            list.add(resultSet.getString("Email"));}
        resultSet.close();
        statement.close();

    }

    /**
     *  Function for delete cloned or similar lines in table
     * @param statement The object used for executing a static SQL statement and returning the results it produces
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void ClearCopy(Statement statement) throws SQLException {
        String Clear = "DELETE t1 FROM friends t1 INNER JOIN friends t2 WHERE t1.id < t2.id AND t1.email = t2.email";
        statement.executeUpdate(Clear);
    }

    /**
     * Check empty table or not
     * If not clearing table
     * @param statement The object used for executing a static SQL statement and returning the results it produces
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void IsEmptyTable(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery("SELECT * from friends");
        if (rs.next()) {
            String delete = "TRUNCATE TABLE friends";
            statement.executeUpdate(delete);
            System.out.println("Table was not empty. Data was cleared");
        }
    }

    /**
     * Function to add days fot Date
     * @param date Date
     * @param days count days for add to Date
     * @return Date + days
     */
    public Date DateAdd(Date date ,int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    /**
     * Close Connection with MYSQL server
     * @throws SQLException If a database access error occurs or this method is called on a closed connection
     */
    public void CloseConnection() throws SQLException {
        connection.commit();
        connection.close();
    }


}