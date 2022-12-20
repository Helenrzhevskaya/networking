package homework8.project.project;

import homework8.project.project.entity.DailyForecast;
import homework8.project.project.entity.Weather;
import kotlin.jvm.internal.PackageReference;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseRepository {
    private String insertWeather = "insert into weather (date, mintemperature, maxtemperature) values (?, ?, ?)";
    private String getWeatherS = "select * from weather";
    private static final String DB_PATH = "jdbc:postgresql://localhost:5433/javacore";
    private static  String username = "postgres";
    private static  String password = "1234567";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


public boolean saveWeatherToDatabase2(List<DailyForecast> dailyForecastList) throws SQLException {
    //TODO //Сохранение пачки. будет принимать на вход пачку погод . примерно Как сверху

    //Weather weather = objectMapper.readValue(dailyForecastList, Weather.class);
    //List<DailyForecast> dailyForecastList1 = weather.getDailyForecasts();

    // for (DailyForecast dayWeather : dailyForecastList) { //если что от сюда убрать цикл
    try (Connection connection = DriverManager.getConnection(DB_PATH, username, password)) {
        PreparedStatement saveWeather2 = connection.prepareStatement(insertWeather);
        for (DailyForecast dailyForecast : dailyForecastList) {
            saveWeather2.setString(1, dailyForecast.getDate());
            saveWeather2.setDouble(2, dailyForecast.getTemperature().getMinimum().getValue());
            saveWeather2.setDouble(3, dailyForecast.getTemperature().getMaximum().getValue());
            saveWeather2.addBatch();
        }
        saveWeather2.executeBatch();

    } catch (SQLException throwables) {
        throwables.printStackTrace();
        throw new SQLException("Сохранение погоды в бд не выполнено!");
    }
    return true;
}

    //TODO homework 2/05 min ПРОВЕРИТЬ ПРАВИЛЬНО ЛИ
    public List<DailyForecast> getSavedToDBWeather() throws SQLException {
        List<DailyForecast> dailylist = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_PATH, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getWeatherS);
            while (resultSet.next()) {
                System.out.print(resultSet.getString("date"));
                System.out.println("");
                System.out.print(resultSet.getDouble("mintemperature"));
                System.out.println("");
                System.out.print(resultSet.getDouble("maxtemperature"));
                System.out.println("");
                dailylist.add(new DailyForecast(resultSet.getString("date"),
                        resultSet.getDouble("mintemperature"),
                        resultSet.getString("maxtemperature")));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dailylist;
    }


}

