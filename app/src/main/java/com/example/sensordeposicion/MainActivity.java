package com.example.sensordeposicion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int latigo = 2;

    private EditText posicion; // editText de posicion
    private SensorManager sm; // objeto de sensor manager que se utiliza para gestionar
    private Sensor sa; // objeto de tipo sensor que representa el sensor de acelerometro
    private SensorEventListener SEL; // objeto tipo sensoreventlistener que se utiliza para escuchar los cambios del sensor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posicion = findViewById(R.id.et_posicion);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sa = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sa == null) {
            Toast.makeText(this, "No hay sensor", Toast.LENGTH_SHORT).show();
        } else {
            SEL = new SensorEventListener() {
                @Override
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // This method is empty as there's no specific action needed for accuracy changes.
                }

                @Override
                public void onSensorChanged(SensorEvent event) {
                    float x = event.values[0];
                    posicion.setText(Float.toString(x));

                    if (x > -5 && latigo == 0) {
                        latigo++;
                        getWindow().getDecorView().setBackgroundColor(Color.BLUE);
                    } else if (x > 5 && latigo == 1) {
                        latigo++;
                        getWindow().getDecorView().setBackgroundColor(Color.RED);
                    }
                    if (latigo == 2) {
                        sonido();
                        latigo = 0;
                    }
                }
            };
            iniciar(); // Start listening to the sensor events
        }
    }

    public void iniciar() {
        sm.registerListener(SEL, sa, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void detener() {
        sm.unregisterListener(SEL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciar(); // Re-register sensor listener when the activity resumes
    }

    @Override
    protected void onStop() {
        detener(); // Unregister sensor listener when the activity stops
        super.onStop();
    }

    private void sonido() {
        MediaPlayer mp = MediaPlayer.create(this, R.raw.latigosheldon);
        mp.start();
    }
}
