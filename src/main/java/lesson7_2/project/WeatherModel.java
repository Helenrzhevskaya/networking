package lesson7_2.project;

import homework8.project.project.Period;

import java.io.IOException;


public interface WeatherModel {
    //метод который будет давать погоду
    void getWeather(String selectedCity, Period period) throws IOException;   //период создали как enum (перечисление). для дз7..
}
