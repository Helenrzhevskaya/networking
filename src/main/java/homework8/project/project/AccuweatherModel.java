package homework8.project.project;
import com.fasterxml.jackson.databind.ObjectMapper;
import homework8.project.project.entity.DailyForecast;
import homework8.project.project.entity.Weather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccuweatherModel extends Weather implements WeatherModel {
    //http://dataservice.accuweather.com/forecasts/v1/daily/1day/
    //http://dataservice.accuweather.com/forecasts/v1/daily/5day/
    private static final String PROTOCOL = "https";
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECASTS = "forecasts";
    private static final String VERSION = "v1";
    private static final String DAILY = "daily";
    private static final String FIVE_DAYS = "5day";
    private static final String API_KEY = "1Ox4yVLIUISxvLZufhig4jLYpULBmrnA";
    private static final String API_KEY_QUERY_PARAM = "apikey";
    private static final String LOCATIONS = "locations";
    private static final String CITIES = "cities";
    private static final String AUTOCOMPLETE = "autocomplete";

    private static final OkHttpClient okHttpClient = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private DatabaseRepository databaseRepository = new DatabaseRepository();


    public void getWeather(String selectedCity, Period period) throws IOException, SQLException {
        String cityKey = detectCityKey(selectedCity);
        String cityName = detectCityName(selectedCity);

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
                        .addPathSegment(cityKey)
                        .addQueryParameter(API_KEY_QUERY_PARAM, API_KEY)
                        .build();

                Request request1 = new Request.Builder()
                        .url(httpUrl1)
                        .build();

                Response fiveDaysForecastResponse = okHttpClient.newCall(request1).execute();
                String weatherResponse1 = fiveDaysForecastResponse.body().string();
               // System.out.println(weatherResponse1);

                Weather weather = objectMapper.readValue(weatherResponse1, Weather.class);

                List<DailyForecast> dailyForecasts = weather.getDailyForecasts();

                for (DailyForecast dayWeather : dailyForecasts) {
                    double maxTemperature = dayWeather.getTemperature().getMaximum().getValue();
                    maxTemperature = (int)((maxTemperature - 32) / 1.8);
                   // System.out.println(maxTemperature);
                    double minTemperature = dayWeather.getTemperature().getMaximum().getValue();
                    minTemperature = (int)((minTemperature - 32) / 1.8);

                String data = dayWeather.getDate().split("T")[0];

                String precipitationTypeDay = "";
                String precipitationIntensityDay  = "";

                String precipitationTypeNight = "";
                String precipitationIntensityNight = "";

                Boolean hasPrecipitationDay = dayWeather.getDay().getHasPrecipitation().booleanValue();
                Boolean hasPrecipitationNight = dayWeather.getNight().getHasPrecipitation().booleanValue();

                if (hasPrecipitationDay) {
                    precipitationTypeDay = dayWeather.getDay().getPrecipitationType();
                    precipitationIntensityDay = dayWeather.getNight().getPrecipitationIntensity();
                    }

                if (hasPrecipitationNight) {
                    precipitationTypeNight = dayWeather.getNight().getPrecipitationType();
                    precipitationIntensityNight = dayWeather.getNight().getPrecipitationIntensity();
                    }
                System.out.printf(" В городе %s погода на дату: %s\n Температура: от %s до %s\n Днём ожидается: %s\n Ночью: %s\n\n",
                        cityName, data, minTemperature, maxTemperature,
                        hasPrecipitationDay ? precipitationTypeDay + " - " + precipitationIntensityDay : "без осадков",
                        hasPrecipitationNight ? precipitationTypeNight + " - " + precipitationIntensityNight : "без осадков");
            }
                databaseRepository.saveWeatherToDatabase2(dailyForecasts);
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
        //System.out.println(responseString);
        String cityKey = objectMapper.readTree(responseString).get(0).at("/Key").asText();

        return cityKey;
    }

    private String detectCityName(String selectCity) throws IOException {

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
        String cityName = objectMapper.readTree(responseString).get(0).at("/LocalizedName").asText();
        return cityName;
    }

    @Override
    public List<DailyForecast> getSavedToDBWeather() throws SQLException {
        return databaseRepository.getSavedToDBWeather();

    }

    public static void main(String[] args) throws IOException, SQLException {
        homework8.project.project.UserInterfaceView userInterfaceView = new UserInterfaceView();
        userInterfaceView.runInterface();
        DatabaseRepository databaseRepository = new DatabaseRepository();
        System.out.println(databaseRepository.getSavedToDBWeather());

    }
}