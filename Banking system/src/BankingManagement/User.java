package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {

    private Connection connection;

    private Scanner scanner;

    public User(Connection connection,Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }

    public void register(){
        scanner.nextLine();
        System.out.print("Full name : ");
        String full_name = scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        if(user_exist(email)){
            System.out.println("user is already registered");
            return;
        }
        String register_query = "INSERT INTO user(full_name, email, password) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(register_query);
            preparedStatement.setString(1,full_name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,password);
            int affectedRows = preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Registration succefull");
            }else{
                System.out.println("Registration failed");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    public String login(){
        scanner.nextLine();
        System.out.print("Email : ");
        String email = scanner.nextLine();
        System.out.print("Password : ");
        String password = scanner.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(login_query);
                preparedStatement.setString(1,email);
                preparedStatement.setString(2,password);
                ResultSet resultSet  = preparedStatement.executeQuery();
                if(resultSet.next()){
                    return email;
                }else {
                    return null;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return null;

    }

    public boolean user_exist(String email) {
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return true;
            }
            else {
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());;
        }
        return false;
    }

}
