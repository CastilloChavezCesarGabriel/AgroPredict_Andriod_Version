package com.agropredict.presentation.user_interface.component.input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.Weather;


public final class EnvironmentInput extends SpinnerInput {
    private final Spinner temperatureSpinner;
    private final Spinner humiditySpinner;
    private final Spinner rainSpinner;

    public EnvironmentInput(Activity activity) {
        this.temperatureSpinner = activity.findViewById(R.id.spnTemperature);
        this.humiditySpinner = activity.findViewById(R.id.spnHumidity);
        this.rainSpinner = activity.findViewById(R.id.spnRain);
        populate();
    }

    public Weather collect() {
        String temperature = extract(temperatureSpinner);
        String humidity = extract(humiditySpinner);
        String rain = extract(rainSpinner);
        return new Weather(temperature, humidity, rain);
    }

    private void populate() {
        fill(temperatureSpinner, "<15°C", "15–25°C", "26–32°C", ">32°C");
        fill(humiditySpinner, "20–40%", "40–60%", "60–80%", ">80%", "No sé");
        fill(rainSpinner, "Hoy", "Esta semana", "Hace 1 semana", "Hace >2 semanas", "No ha llovido");
    }

}