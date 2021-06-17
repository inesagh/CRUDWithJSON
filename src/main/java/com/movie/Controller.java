package com.movie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Controller {
    static final String DB_PATH = "jdbc:mariadb://localhost:3306";
    static final String USER = "root";
    static final String PASS = "?????";
    Service service = new Service();

    public void controller() {
        try (Connection connection = DriverManager.getConnection(DB_PATH, USER, PASS)) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS movie_db");
            statement.execute("use movie_db");
            statement.execute("CREATE TABLE IF NOT EXISTS movie(id INT NOT NULL UNIQUE AUTO_INCREMENT, movie_desc JSON)");


            Scanner scannerForMenu = new Scanner(System.in);
            Scanner scannerForID = new Scanner(System.in);

            System.out.println("------MENU------");
            System.out.println("0 -> create \n" +
                    "1 -> read \n" +
                    "2 -> update \n" +
                    "3 -> delete \n" +
                    "4 -> quit");
            String choice;
            boolean quit = true;
            while (quit) {
                System.out.println("Choose: ");
                choice = scannerForMenu.nextLine();
                switch (choice) {
                    case "0":
                        System.out.println(service.createDBWithMovies(connection));
                        break;
                    case "1":
                        Scanner scannerForReading = new Scanner(System.in);
                        System.out.println("Read all movies - 0");
                        System.out.println("Read the desired movies - 1");
                        while(true){
                            System.out.println("Choose: ");
                            String choiceForReading = scannerForReading.nextLine();
                            if(choiceForReading.equals("1")) {
                                System.out.println("Enter id for reading: ");
                                String readId = scannerForID.nextLine();
                                System.out.println(service.readMovie(connection, Integer.parseInt(readId)));
                                break;
                            }else if(choiceForReading.equals("0")){
                                System.out.println(service.readAlLMovies(connection));
                                break;
                            }
                        }

                        break;
                    case "2":
                        System.out.println("Enter id for updating: ");
                        String updateId = scannerForID.nextLine();
                        System.out.println("Enter title/author for updating: ");
                        String updateSmth = scannerForID.nextLine();
                        System.out.println("Enter new value for updating: ");
                        String updateTo = scannerForID.nextLine();
                        System.out.println(service.updateMovie(connection, Integer.parseInt(updateId), updateSmth, updateTo));
                        break;
                    case "3":
                        System.out.println("Enter id for deleting: ");
                        String deleteId = scannerForID.nextLine();
                        System.out.println(service.deleteMovie(connection, Integer.parseInt(deleteId)));
                        break;
                    case "4":
                        System.out.println("Thank you :)");
                        quit = false;
                        break;
                    default:
                        System.out.println("Try again! ");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
