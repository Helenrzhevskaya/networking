package lesson7_2.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework8.project.project.Period;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Iterator;

public class AccuweatherModel extends Weather implements WeatherModel{
    //http://dataservice.accuweather.com/forecasts/v1/daily/1day/
    //http://dataservice.accuweather.com/forecasts/v1/daily/5day/
private static final String PROTOCOL = "https";
private static final String BASE_HOST = "dataservice.accuweather.com";
private static final String FORECASTS = "forecasts";
private static final String VERSION = "v1";
private static final String DAILY = "daily";
//private static final String ONE_DAY = "1day";
private static final String FIVE_DAYS = "5day";
//apiKey нужно периодически заменять на новый
private static final String API_KEY = "1Ox4yVLIUISxvLZufhig4jLYpULBmrnA";
private static final String API_KEY_QUERY_PARAM = "apikey";
private static final String LOCATIONS = "locations";
private static final String CITIES = "cities";
private static final String AUTOCOMPLETE = "autocomplete";

private static final OkHttpClient okHttpClient = new OkHttpClient();
private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void getWeather(String selectedCity, Period period) throws IOException {
        switch (period) {

            case NOW:
                break;
            case FIVE_DAYS:
                HttpUrl httpUrl1 = new HttpUrl.Builder()
                        .scheme(PROTOCOL)
                        .host(BASE_HOST)
                        .addPathSegment(FORECASTS)
                        .addPathSegment(VERSION)
                        .addPathSegment(DAILY)
                        .addPathSegment(FIVE_DAYS)
                        .addPathSegment(detectCityKey(selectedCity))
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request1 = new Request.Builder()
                        .url(httpUrl1)
                        .build();

                Response fiveDaysForecastResponse = okHttpClient.newCall(request1).execute(); //новый вызов(кидаем внутрь запрос и выполняем)
                String weatherResponse1 = fiveDaysForecastResponse.body().string();//получаем тело ответа


                JsonNode tree = objectMapper.readTree(weatherResponse1);
                JsonNode dailyForecasts = tree.get("DailyForecasts");
                Iterator<JsonNode> dailyForecastsElemnts = dailyForecasts.elements();

                String date = "";
                String precipitationTypeDay = "", precipitationIntensityDay = "";
                String precipitationTypeNight = "", precipitationIntensityNight = "";
                Boolean hasPrecipitationDay = false, hasPrecipitationNight = false;
                Integer tempMin = 0, tempMax = 0;

                while (dailyForecastsElemnts.hasNext()) {
                    JsonNode day = dailyForecastsElemnts.next();

                    //date = day.at("/Date").asText();
                    date = day.at("/Date").asText().split("T")[0];
                    tempMin = day.at("/Temperature/Minimum/Value").asInt(); // в Фаренгейтах
                    tempMax = day.at("/Temperature/Maximum/Value").asInt(); // в Фаренгейтах
                    tempMin = (int)((tempMin - 32) / 1.8); // в Цельсиях
                    tempMax = (int)((tempMax - 32) / 1.8); // в Цельсиях

                    hasPrecipitationDay = day.at("/Day/HasPrecipitation").asBoolean();
                    if (hasPrecipitationDay) {
                        precipitationTypeDay = day.at("/Day/PrecipitationType").asText();
                        precipitationIntensityDay = day.at("/Day/PrecipitationIntensity").asText();
                    }

                    hasPrecipitationNight = day.at("/Night/HasPrecipitation").asBoolean();
                    if (hasPrecipitationNight) {
                        precipitationTypeNight = day.at("/Night/PrecipitationType").asText();
                        precipitationIntensityNight = day.at("/Night/PrecipitationIntensity").asText();
                    }

                    System.out.printf("Погода на дату: %s\n Температура: от %s до %s\n Днём: %s\n Ночью: %s\n\n",
                            date, tempMin, tempMax,
                            hasPrecipitationDay ? precipitationTypeDay + " - " + precipitationIntensityDay : "без осадков",
                            hasPrecipitationNight ? precipitationTypeNight + " - " + precipitationIntensityNight : "без осадков");
                }


                break;
        }
    }

    private String detectCityKey(String selectedCity) throws IOException {
  //http://dataservice.accuweather.com/locations/v1/cities/autocomplete
            HttpUrl httpUrl = new HttpUrl.Builder()
              .scheme(PROTOCOL)
              .host(BASE_HOST)
              .addPathSegment(LOCATIONS)
              .addPathSegment(VERSION)
              .addPathSegment(CITIES)
              .addPathSegment(AUTOCOMPLETE)
              .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
              .addQueryParameter("q", selectedCity)
              .build();

            Request request = new Request.Builder()
              .url(httpUrl)
              .get()
              .addHeader("accept", "application/json")
              .build();

            Response cityResponse = okHttpClient.newCall(request).execute();
            String responseString = cityResponse.body().string(); //получаем тело ответа в виде стринги

         String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText(); //пример парсинга строки!!!
        System.out.println(cityKey);

        return cityKey;
     }

    public static void main(String[] args) throws IOException {
        /**   AccuweatherModel accuweatherModel = new AccuweatherModel();
        accuweatherModel.getWeather("Chelyabinsk", Period.FIVE_DAYS);
         **/
         UserInterfaceView userInterfaceView = new UserInterfaceView();
         userInterfaceView.runInterface();

    }
}

