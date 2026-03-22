package com.agropredict.presentation.user_interface.input;

import android.app.Activity;
import android.widget.Spinner;
import com.agropredict.R;
import com.agropredict.application.request.data.Weather;
import java.util.Arrays;

public final class EnvironmentInput {
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
        String temperature = selected(temperatureSpinner);
        String humidity = selected(humiditySpinner);
        String rain = selected(rainSpinner);
        return new Weather(temperature, humidity, rain);
    }

    private void populate() {
        fill(temperatureSpinner, "<15°C", "15–25°C", "26–32°C", ">32°C");
        fill(humiditySpinner, "20–40%", "40–60%", "60–80%", ">80%", "No sé");
        fill(rainSpinner, "Hoy", "Esta semana", "Hace 1 semana", "Hace >2 semanas", "No ha llovido");
    }

    private String selected(Spinner spinner) {
        return spinner.getSelectedItem().toString();
    }

    private void fill(Spinner spinner, String... options) {
        SpinnerPopulator.populate(spinner, Arrays.asList(options));
    }
}
