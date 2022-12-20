package homework8.project.project;
import homework8.project.project.entity.DailyForecast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public interface WeatherModel {
    void getWeather(String selectedCity, Period period) throws IOException, SQLException;

    List<DailyForecast> getSavedToDBWeather() throws SQLException;

}
