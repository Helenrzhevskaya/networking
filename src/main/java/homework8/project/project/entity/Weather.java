package homework8.project.project.entity;
/**
public class Weather {
    private String city;
    private  String localDate;
    private  Integer temperature;

    public Weather(String city, String localDate, Integer temperature) {
        this.city = city;
        this.localDate = localDate;
        this.temperature = temperature;
    }

    public Weather() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalDate() {
        return localDate;
    }

    public void setLocalDate(String localDate) {
        this.localDate = localDate;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Weather{" +
                "city='" + city + '\'' +
                ", localDate='" + localDate + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}
**/

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "Headline",
        "DailyForecasts"
})

public class Weather {
    private String LocalizedName;  // Создаю вручную!!!!!!!!!!!!!!!!!!!!!!!!!

    @JsonProperty("Headline")
    private Headline headline;
    @JsonProperty("DailyForecasts")
    private List<DailyForecast> dailyForecasts = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

//Гетерреы и сетеры для города


    public String getLocalizedName() {
        return LocalizedName;
    }

    public void setLocalizedName(String localizedName) {
        LocalizedName = localizedName;
    }

    @JsonProperty("Headline")
    public Headline getHeadline() {
        return headline;
    }

    @JsonProperty("Headline")
    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    @JsonProperty("DailyForecasts")
    public List<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    @JsonProperty("DailyForecasts")
    public void setDailyForecasts(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Weather() {
    }
/**
    public String toString() {
     return String.format("Погода на дату: %s\n Температура: от %s до %s\n Днём: %s\n Ночью: %s\n\n",
                                        date, tempMin, tempMax +
                                        hasPrecipitationDay ? precipitationTypeDay + " - " + precipitationIntensityDay : "без осадков" +
                                        hasPrecipitationNight ? precipitationTypeNight + " - " + precipitationIntensityNight : "без осадков");
    }
 **/
}