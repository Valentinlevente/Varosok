package com.example.varosok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

public class InsertActivity extends AppCompatActivity {

    private EditText nev_editText, orszag_editText, lakossag_editText;
    private Button felvetel_button, vissza_button;
    private String BASE_URL = "https://retoolapi.dev/9MOKWf/data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);
        init();


        vissza_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InsertActivity.this, MainActivity.class ));
            }
        });

        felvetel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVaros();
            }

        });
    }

    private void init(){
        nev_editText = findViewById(R.id.nev_editText);
        orszag_editText = findViewById(R.id.orszag_editText);
        lakossag_editText = findViewById(R.id.lakossag_editText);
        felvetel_button =findViewById( R.id.felvetel_button);
        vissza_button =findViewById( R.id.vissza_button);

    }

    private void addVaros() {
        String nev = nev_editText.getText().toString();
        String orszag = orszag_editText.getText().toString();
        String lakossag = lakossag_editText.getText().toString();

        boolean valid = validation();

        if (valid){
            Toast.makeText(this, "Mindent ki kell tolteni", Toast.LENGTH_SHORT).show();
            return;
        }

        int lakossag_Int = Integer.parseInt(lakossag);
        Varosok city = new Varosok(0,nev,orszag,lakossag_Int);
        Gson jsonConverter = new Gson();
        RequestTask task = new RequestTask(BASE_URL, "POST",
                jsonConverter.toJson(city));
        task.execute();
    }

    private boolean validation() {
        if (nev_editText.getText().toString().isEmpty() ||
                orszag_editText.getText().toString().isEmpty() || lakossag_editText.getText().toString().isEmpty())
            return true;
        else
            return false;
    }

    class RequestTask extends AsyncTask<Void, Void, Response> {
        private String requestUrl;
        private String requestMethod;
        private String requestBody;

        public RequestTask(String requestUrl) {
            this.requestUrl = requestUrl;
            this.requestMethod = "GET";
        }

        public RequestTask(String requestUrl, String requestMethod) {
            this.requestUrl = requestUrl;
            this.requestMethod = requestMethod;
        }

        public RequestTask(String requestUrl, String requestMethod, String requestBody) {
            this.requestUrl = requestUrl;
            this.requestMethod = requestMethod;
            this.requestBody = requestBody;
        }

        @Override
        protected Response doInBackground(Void... voids) {
            Response response = null;
            try {
                switch (requestMethod) {
                    case "GET":
                        response = RequestHandler.get(BASE_URL);
                        break;
                    case "POST":
                        response = RequestHandler.post(requestUrl, requestBody);
                        break;
                    case "PUT":
                        response = RequestHandler.put(requestUrl, requestBody);
                        break;
                    case "DELETE":
                        response = RequestHandler.delete(requestUrl);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
}
}