package lesson7_2.project;

import java.io.IOException;


public interface WeatherModel {
    void getWeather(String selectedCity, Period period) throws IOException;   //период создали как enum (перечисление)
}
