package BankingManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {


    private Connection connection;
    private Scanner scanner;

    public AccountManager(Connection connection, Scanner scanner){
        this.connection = connection;
        this.scanner = scanner;
    }


    public void credit_money(long account_number) throws SQLException{
        scanner.nextLine();
        System.out.print("Enter amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = scanner.nextLine();


        try {
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND  security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){

                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if(rowsAffected>0){
                            System.out.println("Rs " + amount + " credited successfully");
                            connection.commit();
                        }else {
                            System.out.println("Transaction  failed!!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }

                }else {
                    System.out.println("Invalid pin");
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }





    public void debit_money(long account_number) throws SQLException{
           scanner.nextLine();
        System.out.print("Enter amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = scanner.nextLine();


        try {
            connection.setAutoCommit(false);
            if(account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND  security_pin = ?");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                   double current_balance = resultSet.getDouble("balance");
                   if(amount <= current_balance){
                       String credit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                       PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                       preparedStatement1.setDouble(1,amount);
                       preparedStatement1.setLong(2,account_number);
                       int rowsAffected = preparedStatement1.executeUpdate();
                       if(rowsAffected > 0){
                           System.out.println("Rs " + amount + " debited successfully");
                           connection.commit();
                       }else {
                           System.out.println("Transaction  failed!!");
                           connection.rollback();
                           connection.setAutoCommit(true);
                       }
                   }else {
                       System.out.println("Insufficient balance ");
                   }
                }else {
                    System.out.println("Invalid pin");
                }

            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        connection.setAutoCommit(true);
    }


    public void transfer_money(long sender_account_number)throws SQLException{
        scanner.nextLine();
        System.out.print("Enter receiver account number : ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter amount : ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = scanner.nextLine();

        try {
            connection.setAutoCommit(false);
            if(sender_account_number!=0 && receiver_account_number!=0){
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preparedStatement.setLong(1,sender_account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");
                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ? ";
                        PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                        PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                        creditPreparedStatement.setDouble(1,amount);
                        creditPreparedStatement.setLong(2,receiver_account_number);
                        debitPreparedStatement.setDouble(1,amount);
                        debitPreparedStatement.setLong(2,sender_account_number);
                        int rowsAffected1 = creditPreparedStatement.executeUpdate();
                        int rowsAffected2 = debitPreparedStatement.executeUpdate();

                        if(rowsAffected1>0 && rowsAffected2>0){
                            System.out.println("Transaction successful ");
                            System.out.println("Rs"+amount+"transferred successful");
                            connection.commit();
                        }
                        else{
                            System.out.println("Transaction Failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }else {
                        System.out.println("Insufficient balance");
                    }
                }else {
                    System.out.println("Invalid security pin");
                }
            }else {
                System.out.println("Invalid account number");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public  void getBalance(long account_number){

        scanner.nextLine();
        System.out.print("Enter security pin : ");
        String security_pin = scanner.nextLine();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
            preparedStatement.setLong(1,account_number);
            preparedStatement.setString(2,security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: "+balance);
            }else {
                System.out.println("Invalid pin");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }


}
