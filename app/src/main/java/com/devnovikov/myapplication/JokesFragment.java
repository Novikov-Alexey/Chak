package com.devnovikov.myapplication;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class JokesFragment extends Fragment {

    public final String URL = "https://api.icndb.com/jokes/random";
    private OkHttpClient okHttpClient;

    //views
    Button changeJokesBtn;
    TextView resultTv;
    ListView jokesLv;
    EditText inputEt;
    String txt;
    ArrayList<String> massOfJokes;
    ArrayAdapter<String> listAdapter;
    final Handler handler = new Handler();

    public JokesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("massOfJokes", massOfJokes);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        massOfJokes = new ArrayList<>();


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jokes, container, false);

        //init views
        changeJokesBtn = view.findViewById(R.id.changeJokesBtn);
        inputEt = view.findViewById(R.id.inputEt);

        listAdapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_list_item_1, massOfJokes);
        jokesLv = (ListView) view.findViewById(R.id.jokesLv);
        jokesLv.setAdapter(listAdapter);
        if (savedInstanceState != null) {
            massOfJokes = savedInstanceState.getStringArrayList("massOfJokes");
            listAdapter.notifyDataSetChanged();
            if (massOfJokes.size()>0){
                for (int i = 0; i < massOfJokes.size(); i++) {
                    Toast.makeText(inflater.getContext(), massOfJokes.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        }

        changeJokesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRandomFacts();
            }
        });

        return view;
    }

    private void loadRandomFacts() {
        massOfJokes.clear();
        okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(URL).build();
        //Integer count = inputEt.getText().toString()
        if (inputEt.getText().length() > 0){
            for(int i=0;i< Integer.parseInt(inputEt.getText().toString()); i++){
                Call call =  okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                View view = getView();
                                Toast.makeText(view.getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String body = response.body().string();
                        try {
                            txt = new JSONObject(body).getJSONObject("value").get("joke").toString();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    massOfJokes.add(Html.fromHtml(txt).toString());
                                    listAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } else {
            View view = getView();
            Toast.makeText(view.getContext(), "Write a number", Toast.LENGTH_SHORT).show();
        }
    }

}
