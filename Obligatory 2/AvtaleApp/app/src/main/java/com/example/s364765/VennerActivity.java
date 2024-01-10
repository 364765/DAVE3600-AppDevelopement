package com.example.s364765;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.avtaleapp.R;

import java.util.List;

public class VennerActivity extends AppCompatActivity implements VennDialog.LeggTilInterface, SlettVennDialog.SlettInterface {
    private Datakilde dataKilde;
    private ArrayAdapter<Venner> vennerArrayAdapter;
    private List<Venner> venner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venner);

        dataKilde = new Datakilde(this);
        dataKilde.open();

        // Viewet med liste over venner
        ListView vennerListView = findViewById(R.id.vennerListView);

        venner = dataKilde.finnAlleVenner();

        // Legger in alle venner i list viewet
        vennerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, venner);
        vennerListView.setAdapter(vennerArrayAdapter);

        // Legg til venn knapp
        Button leggtilButton = findViewById(R.id.leggtilBtn);
        leggtilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VennDialog vennDialog = new VennDialog();
                vennDialog.show(getSupportFragmentManager(), "LeggTilDialog");
            }
        });

        // onClick listener for venn i liste som åpner dialog vindu med info
        vennerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Venner venn = venner.get(position);
                long vennID = venn.getId();

                SlettVennDialog slettVennDialog = new SlettVennDialog(vennID, dataKilde);
                slettVennDialog.show(getSupportFragmentManager(), "SlettVennDialog");
            }
        });
    }

    // LeggTillVenn knapp i dialog
    @Override
    public void onLeggTilClick(String navn, String telefonNr) {
        if (!navn.isEmpty() && !telefonNr.isEmpty()) {
            // Legger til venn i DB
            Venner nyVenn = dataKilde.leggInnVenn(navn, telefonNr);
            vennerArrayAdapter.add(nyVenn);
        }
    }

    // SlettVenn knapp i dialog
    @Override
    public void onSlettClick(long vennID) {
        dataKilde.slettVenn(vennID);

        //Oppdaterer view
        ListView vennerListView = findViewById(R.id.vennerListView);
        venner = dataKilde.finnAlleVenner();
        vennerArrayAdapter = new ArrayAdapter<>(getBaseContext(),
                android.R.layout.simple_list_item_1, venner);
        vennerListView.setAdapter(vennerArrayAdapter);
    }


    @Override
    public void onAvbrytClick() {

    }

    @Override
    protected void onResume() {
        dataKilde.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataKilde.close();
        super.onPause();
    }
}
