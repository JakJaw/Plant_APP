package com.example.plant;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.example.plant.databinding.FragmentFirstBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FirstFragment extends Fragment {
private FragmentFirstBinding binding;
private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      binding = FragmentFirstBinding.inflate(inflater, container, false);
      return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getView().findViewById(R.id.listView);

        //wczytanie elementów
        ArrayList<RoslinaDane> rosliny = new ArrayList<>();
        SharedPreferences roslinyZapis = getContext().getSharedPreferences("zapisRoslin", 0);
        int numberOfPlants = roslinyZapis.getInt("numberOfPlants", 0);

        //Dodanie wszystkich roślin do listy
        for (int i = 0; i < numberOfPlants; i++) {
            rosliny.add(new RoslinaDane(roslinyZapis.getString("nazwa" + i, ""), roslinyZapis.getInt("czestotliwoscPodlewaniaWDniach" + i, 0), roslinyZapis.getBoolean("zraszanie" + i, false), roslinyZapis.getString("notatki" + i, ""), roslinyZapis.getString("zdjecie" + i, ""),
                    roslinyZapis.getString("data" + i, "")));
        }

        //Wyznaczenie daty podlania i pobranie aktualnej. Następnie wyświetlenie powiadomienia odnośnie podlania i zraszania
        for (int i = 0; i < rosliny.size(); i++) {
            RoslinaDane roslinaDane = rosliny.get(i);
            System.out.println(roslinaDane.getDataNastępnegoPodlania());
            Date datePodlaniaRosliny = new Date();
            try {
                datePodlaniaRosliny = new SimpleDateFormat("yyyy-MM-dd").parse(roslinaDane.getDataNastępnegoPodlania());
            } catch (Exception e) {
                e.printStackTrace();
            }


            Calendar c = Calendar.getInstance();
            c.setTime(datePodlaniaRosliny);
            c.add(Calendar.DATE, roslinaDane.getCzestotliwoscPodlewaniaWDniach());
            datePodlaniaRosliny = c.getTime();
            Date time = Calendar.getInstance().getTime();
            if (datePodlaniaRosliny.compareTo(time) <= 0) {
                Toast.makeText(getContext(), "Roslina: \"" + roslinaDane.getNazwa() + "\" wymaga podlania" + (roslinaDane.isZraszanie() ? " i zraszania" : ""), Toast.LENGTH_LONG).show();
            }
        }

        RoslinaDaneAdapter roslinaDaneAdapter = new RoslinaDaneAdapter(getContext(), R.layout.plant_list_row, rosliny);
        listView.setAdapter(roslinaDaneAdapter);

        //Po kliknięciu w roślinę powinno przenieść do ekranu edycji
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("edttext", "From Activity");

                RoslinaDane roslinaDane = rosliny.get(i);
                bundle.putSerializable("roslina", roslinaDane);
                bundle.putInt("roslinaPos", i);

                NavController navController = NavHostFragment.findNavController(FirstFragment.this);
                navController.navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });

        //Wdrożenie nawigacji po menu, od prostych kliknięć przycisków po bardziej złożone akcje, takie jak paski aplikacji.

                binding.addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }


    //Pozwala fragmentowi oczyścić zasoby związane z jego widokiem.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}