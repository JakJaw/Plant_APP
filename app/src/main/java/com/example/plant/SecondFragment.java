package com.example.plant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.plant.databinding.FragmentSecondBinding;
import com.example.plant.exceptions.RoslinaDaneException;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private ArrayList<RoslinaDane> rosliny;
    private EditText nazwa;
    private EditText czestPodlewania;
    private Switch zraszanie;
    private boolean zraszanieBool;
    private EditText notatki;
    private String[] typyRoslin = {"Kaktus", "Dracena", "Paproć", "Drzewo liściaste", "Nie ma na liscie"};
    private AutoCompleteTextView typyRoslinInput;
    private ArrayAdapter<String> adapterTypRoslin;
    private String typeOfPlant;

    private boolean edytowanieRosliny;
    private int updateRoslinaPos;
    private boolean czyUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //obsluga danych
        rosliny = new ArrayList<>();
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        nazwa = binding.getRoot().findViewById(R.id.editTextTextPersonName2);
        czestPodlewania = binding.getRoot().findViewById(R.id.editTextNumberDecimal);
        zraszanie = binding.getRoot().findViewById(R.id.switch1);
        notatki = binding.getRoot().findViewById(R.id.notes);

        //bool slider
        zraszanie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    zraszanieBool = true;
                } else {
                    zraszanieBool = false;
                }
            }
        });

        //akcja usunięcia
        binding.usun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (czyUpdate) {
                    SharedPreferences roslinyZapis = getContext().getSharedPreferences("zapisRoslin", 0);
                    int numberOfPlants = roslinyZapis.getInt("numberOfPlants", 0);
                    for (int i = 0; i < numberOfPlants; i++) {
                        rosliny.add(new RoslinaDane(roslinyZapis.getString("nazwa" + i, ""), roslinyZapis.getInt("czestotliwoscPodlewaniaWDniach" + i, 0), roslinyZapis.getBoolean("zraszanie" + i, false), roslinyZapis.getString("notatki" + i, ""), roslinyZapis.getString("zdjecie" + i, ""),
                                roslinyZapis.getString("data" + i, "")));
                    }

                    //zmiana indeksu i zapis do pliku
                    rosliny.remove(rosliny.get(updateRoslinaPos));
                    SharedPreferences.Editor editor = roslinyZapis.edit();
                    for (int i = 0; i < rosliny.size(); i++) {
                        RoslinaDane roslinaDane = rosliny.get(i);
                        editor.putString("nazwa" + i, roslinaDane.getNazwa());
                        editor.putInt("czestotliwoscPodlewaniaWDniach" + i, roslinaDane.getCzestotliwoscPodlewaniaWDniach());
                        editor.putBoolean("zraszanie" + i, roslinaDane.isZraszanie());
                        editor.putString("notatki" + i, roslinaDane.getNotatki());
                        editor.putString("zdjecie" + i, roslinaDane.getZdjecieStr());
                    }

                    editor.putInt("numberOfPlants", rosliny.size());
                    editor.apply();
                    rosliny = new ArrayList<>();

                    //Powrót do menu głównego
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
                //Powiadomienie
                else {Toast.makeText(getContext(), "Nie możesz usunąć rośliny którą dodajesz", Toast.LENGTH_LONG).show();}
            }
        });

        //Przycisk podlania resetujący czas
        binding.podlanaRoslina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (czyUpdate) {
                    SharedPreferences roslinyZapis = getContext().getSharedPreferences("zapisRoslin", 0);
                    int numberOfPlants = roslinyZapis.getInt("numberOfPlants", 0);
                    for (int i = 0; i < numberOfPlants; i++) {
                        rosliny.add(new RoslinaDane(roslinyZapis.getString("nazwa" + i, ""), roslinyZapis.getInt("czestotliwoscPodlewaniaWDniach" + i, 0), roslinyZapis.getBoolean("zraszanie" + i, false), roslinyZapis.getString("notatki" + i, ""), roslinyZapis.getString("zdjecie" + i, ""),
                                roslinyZapis.getString("data" + i, "")));
                    }

                    //data do podlewania
                    String format = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                    //wybrana roślina
                    rosliny.get(updateRoslinaPos).setDataNastępnegoPodlania(format);


                    SharedPreferences.Editor editor = roslinyZapis.edit();
                    for (int i = 0; i < rosliny.size(); i++) {
                        RoslinaDane roslinaDane = rosliny.get(i);
                        editor.putString("nazwa" + i, roslinaDane.getNazwa());
                        editor.putInt("czestotliwoscPodlewaniaWDniach" + i, roslinaDane.getCzestotliwoscPodlewaniaWDniach());
                        editor.putBoolean("zraszanie" + i, roslinaDane.isZraszanie());
                        editor.putString("notatki" + i, roslinaDane.getNotatki());
                        editor.putString("zdjecie" + i, roslinaDane.getZdjecieStr());
                        editor.putString("data" + i, roslinaDane.getDataNastępnegoPodlania());

                    }
                    editor.putInt("numberOfPlants", rosliny.size());
                    editor.apply();
                    rosliny = new ArrayList<>();

                    //Powrót
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                } else {Toast.makeText(getContext(), "Nie możesz podlać rośliny którą dodajesz", Toast.LENGTH_LONG).show();}
            }
        });


        binding.dodaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean blad = false;
                try {
                    SharedPreferences roslinyZapis = getContext().getSharedPreferences("zapisRoslin", 0);
                    int numberOfPlants = roslinyZapis.getInt("numberOfPlants", 0);
                    for (int i = 0; i < numberOfPlants; i++) {
                        rosliny.add(new RoslinaDane(roslinyZapis.getString("nazwa" + i, ""), roslinyZapis.getInt("czestotliwoscPodlewaniaWDniach" + i, 0), roslinyZapis.getBoolean("zraszanie" + i, false), roslinyZapis.getString("notatki" + i, ""), roslinyZapis.getString("zdjecie" + i, ""),
                                roslinyZapis.getString("data" + i, "")));
                    }
                    if (czyUpdate) {
                        RoslinaDane roslinaDane = rosliny.get(updateRoslinaPos);
                        roslinaDane.setNazwa(nazwa.getText().toString());
                        roslinaDane.setCzestotliwoscPodlewaniaWDniach(Integer.parseInt(czestPodlewania.getText().toString()));
                        roslinaDane.setZraszanie(zraszanieBool);
                        roslinaDane.setZdjecie(typeOfPlant);
                        roslinaDane.setNotatki(notatki.getText().toString());
                    }
                    //Jeśli brak danych w polu
                    else {
                        if (nazwa.getText() == null || nazwa.getText().toString().equals("") || czestPodlewania.getText() == null || czestPodlewania.getText().toString().equals("")) {
                            Toast.makeText(getContext(), "Błąd, sprawdź czy uzupełniłeś wszystkie dane", Toast.LENGTH_LONG).show();
                            rosliny = new ArrayList<>();
                            blad = true;
                        }
                        else {
                            rosliny.add(new RoslinaDane(nazwa.getText().toString(), Integer.parseInt(czestPodlewania.getText().toString()), zraszanieBool, notatki.getText().toString(), typeOfPlant,
                                    new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime())));
                        }
                    }

                    if (!blad) {
                        SharedPreferences.Editor editor = roslinyZapis.edit();
                        for (int i = 0; i < rosliny.size(); i++) {
                            RoslinaDane roslinaDane = rosliny.get(i);
                            editor.putString("nazwa" + i, roslinaDane.getNazwa());
                            editor.putInt("czestotliwoscPodlewaniaWDniach" + i, roslinaDane.getCzestotliwoscPodlewaniaWDniach());
                            editor.putBoolean("zraszanie" + i, roslinaDane.isZraszanie());
                            editor.putString("notatki" + i, roslinaDane.getNotatki());
                            editor.putString("zdjecie" + i, roslinaDane.getZdjecieStr());
                            editor.putString("data" + i, roslinaDane.getDataNastępnegoPodlania());
                        }
                        editor.putInt("numberOfPlants", rosliny.size());
                        editor.apply();
                    }

                }
                catch (RoslinaDaneException e) {
                    e.printStackTrace();
                    Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG)
                            .setAction("", null).show();
                }
                for (RoslinaDane roslinaDane : rosliny) {
                    System.out.println(roslinaDane + "\n");
                }
                if (!blad) {
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment);
                }
            }
        });
        return binding.getRoot();
    }


    //Obsługa UI i pól wyboru
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typyRoslinInput = getView().findViewById(R.id.auto_complete_txt);

        Bundle bundle = this.getArguments();
        //bundle podaje wybraną roślinę w menu głównym
        if (bundle != null) {
            czyUpdate = true;
            updateRoslinaPos = bundle.getInt("roslinaPos");
            RoslinaDane roslinaDane = (RoslinaDane) bundle.getSerializable("roslina");
            nazwa.setText(roslinaDane.getNazwa());
            czestPodlewania.setText(Integer.toString(roslinaDane.getCzestotliwoscPodlewaniaWDniach()));
            zraszanie.setChecked(roslinaDane.isZraszanie());
            typyRoslinInput.setText(roslinaDane.getZdjecieStr());
            typeOfPlant = roslinaDane.getZdjecieStr();
            notatki.setText(roslinaDane.getNotatki());
        } else {
            czyUpdate = false;
        }
        adapterTypRoslin = new ArrayAdapter<>(this.getContext(), R.layout.type_of_plant, typyRoslin);
        typyRoslinInput.setAdapter(adapterTypRoslin);
        typyRoslinInput.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                typeOfPlant = adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}