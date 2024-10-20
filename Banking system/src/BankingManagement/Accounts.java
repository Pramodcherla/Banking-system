package BankingManagement;

import java.sql.*;
import java.util.Scanner;

public class Accounts {

    private Connection connection;
    private Scanner scanner;

    public Accounts(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public long open_account(String email){
        if(!account_exist(email)){
              String open_account_query = "INSERT INTO accounts(account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
              scanner.nextLine();
            System.out.print("Enter full name : ");
            String full_name = scanner.nextLine();
            System.out.print("Enter initial amount : ");
            double balance = scanner.nextDouble();
            scanner.nextLine();
            System.out.print("Enter security pin : ");
            String security_pin = scanner.nextLine();


            try {
                long account_number = generateAccount_number();
                PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,full_name);
                preparedStatement.setString(3,email);
                preparedStatement.setDouble(4,balance);
                preparedStatement.setString(5,security_pin);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected>0){
                    return account_number;
                }
                else {
                    throw new RuntimeException("account creation failed");
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
        throw new RuntimeException("account already exist");
    }

    public long getAccount_number(String email){
    String query = "SELECT account_number FROM accounts WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("account number do not exist");
    }

    private long generateAccount_number(){

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
            if (resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }else {
                return 10000100;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
     return 10000100;
    }


    public boolean account_exist(String email){

        String query = "SELECT account_number FROM accounts WHERE email = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
