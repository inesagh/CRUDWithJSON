package com.movie;

import com.google.gson.Gson;

import java.sql.*;

public class Service {

    Gson gson = new Gson();
    Movie movie = new Movie("The Shawshank Redemption", "Frank Darabont");
    Movie movie1 = new Movie("The Lord of the Rings: The Return of the King", "Peter Jackson");
    Movie movie2 = new Movie("Seven Samurai", "Akira Kurosawa");
    Movie movie3 = new Movie("La Dolce Vita", "Federico Fellini");
    Movie movie4 = new Movie("Citizen Kane", "Orson Welles");

//    Create
    public String createDBWithMovies(Connection connection) {
        String s = gson.toJson(movie);
        String s1 = gson.toJson(movie1);
        String s2 = gson.toJson(movie2);
        String s3 = gson.toJson(movie3);
        String s4 = gson.toJson(movie4);
        creatingMovie(connection, s);
        creatingMovie(connection, s1);
        creatingMovie(connection, s2);
        creatingMovie(connection, s3);
        creatingMovie(connection, s4);
        return "Successfully created!";
    }
    public String creatingMovie(Connection connection, String movieJson){
        String command = "INSERT INTO movie(movie_desc) VALUES (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(command);
            preparedStatement.setString(1, movieJson);
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Successfully created!";
    }

//    Read
    public String readMovie(Connection connection, int idForReading){
    Movie movie = null;

    String command = "SELECT movie_desc FROM movie WHERE id = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(command);
        preparedStatement.setInt(1, idForReading);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String movie_desc = resultSet.getString("movie_desc");
            movie = gson.fromJson(movie_desc, Movie.class);
        }
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    if(movie != null){
        return movie.toString() + "\n" + "Successfully read!";
    }else{
        return "Try again with valid info.";
    }
}
    public String readAlLMovies(Connection connection){
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM movie");
            while (resultSet.next()){
                String id = "id = " + resultSet.getInt("id");
                String movie_desc = resultSet.getString("movie_desc");
                Movie movie = gson.fromJson(movie_desc, Movie.class);
                System.out.println(id);
                System.out.println(movie.toString());
                System.out.println();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "Successfully read all movies!";
    }

//    Update
    public String  updateMovie(Connection connection, int idForUpdating, String updateSmth, String updateTo ){
    String command = "SELECT movie_desc FROM movie WHERE id = ?";
    Movie movie = null;
    String movie_desc = "";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(command);
        preparedStatement.setInt(1, idForUpdating);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            movie_desc = resultSet.getString("movie_desc");
            movie = gson.fromJson(movie_desc, Movie.class);
        }
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    if(movie != null) {
        if (updateSmth.equals("title")) {
            movie.setTitle(updateTo);
        } else {
            movie.setAuthor(updateTo);
        }
        String s = gson.toJson(movie);
        String colName = "'$." + updateSmth + "'";
        String updatedValue = "'" + updateTo + "'";
        String commandForUpdating = "UPDATE movie SET movie_desc = JSON_SET(movie_desc," + colName + " ," + updatedValue + " )  WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(commandForUpdating);
            preparedStatement.setInt(1, idForUpdating);
            ResultSet resultSet = preparedStatement.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("Try again with valid info");
        }
        return "Successfully updated!";
    }else{
        return "Try again with valid info.";
    }

}

//    Delete
    public String deleteMovie(Connection connection, int idForDeleting){
    String command = "DELETE FROM movie WHERE id = ?";
    try {
        PreparedStatement preparedStatement = connection.prepareStatement(command);
        preparedStatement.setInt(1, idForDeleting);
        ResultSet resultSet = preparedStatement.executeQuery();
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }

    return "Successfully deleted!";
}


}
