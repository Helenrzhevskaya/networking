package lesson7_2.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

import static lesson7_2.project.Period.NOW;

public class AccuweatherModel implements WeatherModel{
 //http://dataservice.accuweather.com/forecasts/v1/daily/1day/
 private static final String PROTOCOL = "https";
 private static final String BASE_HOST = "dataservice.accuweather.com";
 private static final String FORECASTS = "forecasts";
 private static final String VERSION = "v1";
 private static final String DAILY = "daily";
 private static final String ONE_DAY = "1day";
 private static final String API_KEY = "1Ox4yVLIUISxvLZufhig4jLYpULBmrnA";
 private static final String API_KEY_QUERY_PARAM = "apikey";
 private static final String LOCATIONS = "locations";
 private static final String CITIES = "cities";
 private static final String AUTOCOMPLETE = "autocomplete";

 private static final OkHttpClient okHttpClient = new OkHttpClient();
 private static final ObjectMapper objectMapper = new ObjectMapper();

 public void getWeather(String selectedCity, Period period) throws IOException {
     switch (period) {
     case NOW:
     HttpUrl httpUrl = new HttpUrl.Builder()
             .scheme(PROTOCOL)
             .host(BASE_HOST)
             .addPathSegment(FORECASTS)
             .addPathSegment(VERSION)
             .addPathSegment(DAILY)
             .addPathSegment(ONE_DAY)
 //.addPathSegment(detectCityKey(selectedCity))
             //.addPathSegment("28642") //для Челябинска
             .addPathSegment(detectCityKey(selectedCity))
             .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
             .build();

         Request request = new Request.Builder()
                 .url(httpUrl)
                 .build();

     Response oneDayForecastResponse = okHttpClient.newCall(request).execute();
     String weatherResponse = oneDayForecastResponse.body().string();
     System.out.println(weatherResponse);

     break;

     //TODO: сделать человекочитаемый вывод погоды. Выбрать параметры для вывода на свое усмотрение
 //Например: Погода в городе Москва - 5 градусов по цельсию Expect showers late Monday night

     case FIVE_DAYS:
 //TODO*: реализовать вывод погоды на 5 дней/ ДЗ
     break;
     }
 }

    private String detectCityKey(String selectCity) throws IOException {

  //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
      HttpUrl httpUrl = new HttpUrl.Builder()
              .scheme(PROTOCOL)
              .host(BASE_HOST)
              .addPathSegment(LOCATIONS)
              .addPathSegment(VERSION)
              .addPathSegment(CITIES)
              .addPathSegment(AUTOCOMPLETE)
              .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
              .addQueryParameter("q", selectCity)
              .build();

      Request request = new Request.Builder()
              .url(httpUrl)
              .get()
              .addHeader("accept", "application/json")
              .build();
      Response cityResponse = okHttpClient.newCall(request).execute();
      String responseString = cityResponse.body().string();

         String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText();
         //System.out.println(cityKey);
         return cityKey;
     }

     public static void main(String[] args) throws IOException {
      //AccuweatherModel accuweatherModel = new AccuweatherModel(); //1
      //accuweatherModel.getWeather("Moscow", NOW); //для первого случая
     //accuweatherModel.detectCityKey("Moscow"); //для второго случая
     //accuweatherModel.getWeather("Moscow", NOW); //для третьего случая//2

         UserInterfaceView userInterfaceView = new UserInterfaceView();

         userInterfaceView.runInterface();
 }
}

