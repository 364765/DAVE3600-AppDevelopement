package com.example.s364765;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.s364765.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LagreDialog.LagreInterface {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Marker sisteMarkør;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Henter objekter fra webserver
        getJSON task = new getJSON();
        task.execute(new
                String[]{"https://dave3600.cs.oslomet.no/~s364765/jsonout.php"});

    }

    /*-----------------Kode for å hente og legge inn steder/markører------------------------------*/
    private class getJSON extends AsyncTask<String, Void, String> {
        JSONObject jsonObject;

        @Override
        protected String doInBackground(String... urls) {
            Log.d("AsyncTask", "doInBackground started");

            String retur = "";
            String s = "";
            String output = "";
            for (String url : urls) {
                try {
                    URL urlen = new URL(urls[0]);
                    HttpsURLConnection conn = (HttpsURLConnection)
                            urlen.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.connect();
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code :" +
                                conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            (conn.getInputStream())));
                    System.out.println("Output from Server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();
                    try {
                        JSONArray steder = new JSONArray(output);

                        // Liste med steder
                        List<Sted> stedListe = new ArrayList<>();

                        // Løkke som går gjennom hvert sted hentet og legger det til i en liste
                        for (int i = 0; i < steder.length(); i++) {
                            JSONObject jsonobject = steder.getJSONObject(i);
                            String navn = jsonobject.getString("navn");
                            String beskrivelse = jsonobject.getString("beskrivelse");
                            String gateadresse = jsonobject.getString("gateadresse");
                            double latitude = Double.parseDouble(jsonobject.getString("latitude"));
                            double longitude = Double.parseDouble(jsonobject.getString("longitude"));

                            // Sted objekt
                            Sted sted = new Sted(navn, beskrivelse, gateadresse, latitude, longitude);

                            // Legger til sted i liste med steder
                            stedListe.add(sted);

                            Log.d("StedInfo", "Sted added: " + sted.getNavn().toString());


                            retur = retur + "Navn: " + navn + "\n" +
                                    "Beskrivelse: " + beskrivelse + "\n" +
                                    "Gateadresse: " + gateadresse + "\n" +
                                    "Latitude: " + latitude + "\n" +
                                    "Longitude: " + longitude + "\n\n";
                        }
                        StedListe.setStedListe(stedListe);
                        return retur;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
                } catch (Exception e) {
                    return "Noe gikk feil" + e;
                }
            }
            return retur;
        }

        @Override
        protected void onPostExecute(String ss) {
            // Henter liste med steder
            List<Sted> stedListe = StedListe.getStedListe();

            // Løkke som går gjennom hvert sted og legger det inn på kartet med info
            for (Sted sted : stedListe) {

                // Finner koordinater
                LatLng gpsCords = new LatLng(sted.getLatitude(), sted.getLongitude());

                // Legger til markør
                mMap.addMarker(new MarkerOptions()
                        .position(gpsCords)
                        .title(sted.getNavn() + " - " + sted.getAdresse())
                        .snippet(sted.getBeskrivelse()));
            }

        }
    }

    // Legger inn sted på webserver DB
    public void postJSON(String url) {
        getJSON post = new getJSON();
        post.execute(url);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    /*----------------Kart kode----------------------*/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Legger til default markør ved Jernbanetorget, Oslo
        LatLng jernbanetorget = new LatLng(59.911104054373574, 10.74996080249548);
        mMap.addMarker(new MarkerOptions().position(jernbanetorget).title("Marker in Jernbanetorget"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jernbanetorget));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // Fjerner siste markør
                if (sisteMarkør != null) {
                    sisteMarkør.remove();
                }

                // Legger til markøy med tittel
                sisteMarkør = mMap.addMarker(new MarkerOptions().position(latLng).title("Clicked Location"));


                // Henter latitude/longitude
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;

                // Henter full adresse
                String adresse = adresseKoordinater(latitude, longitude);
                String latitudeString = Double.toString(latitude);
                String longitudeString = Double.toString(longitude);

                // Dialog åpnes ved klikk på kart
                LagreDialog lagreDialog = new LagreDialog();

                // Legger verdier i bundle som kalles på i dialogen
                Bundle bundle = new Bundle();
                bundle.putString("adresse", adresse);
                bundle.putString("latitude", latitudeString);
                bundle.putString("longitude", longitudeString);
                lagreDialog.setArguments(bundle);

                lagreDialog.show(getSupportFragmentManager(), "LagreDialog");

            }
        });
    }

    /*----------------Metode som regner ut adresse koordinater----------------------*/
    private String adresseKoordinater(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> adresser = geocoder.getFromLocation(latitude, longitude, 1);

            if (adresser != null && adresser.size() > 0) {
                Address addresse = adresser.get(0);

                // Henter full adresse
                String fullAdresse = addresse.getAddressLine(0);
                Log.d("Adresse", "Full Adresse: " + fullAdresse);
                return fullAdresse;
            } else {
                Log.d("Adresse", "Ingen adresse funnet");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Adresse", "Error ved henting av adresse", e);
        }
        return null;
    }


    /*----------------Dialog----------------------*/
    // Avbryt knapp i dialog
    @Override
    public void onAvbrytClick() {
        // Fjerner markøren dersom avbryt trykkes
        if (sisteMarkør != null) {
            sisteMarkør.remove();
            sisteMarkør = null;
        }

    }

    // Lagre knapp i dialog
    @Override
    public void onLagreClick(String navn, String beskrivelse, String adresse, String latitude, String longitude) {
        String url = "https://dave3600.cs.oslomet.no/~s364765/jsonin.php?navn=" + navn + "&beskrivelse=" + beskrivelse + "&gateadresse=" + adresse + "&latitude=" + latitude + "&longitude=" + longitude;

        // Lagrer dialog informasjon om stedet på DB i webserver
        postJSON(url);

        // Oppdaterer kartet med det nye stedet
        getJSON get = new getJSON();
        get.execute(new
                String[]{"https://dave3600.cs.oslomet.no/~s364765/jsonout.php"});


    }


}



