package projetaobcc20172.com.projetopetemfocofornecedor.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONObject;
import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.helper.JsonRequestAsync;

/**
 * Created by Alexsandro on 25/02/2018.
 */

public class Localizacao {
    private static final int REQUEST_LOCATION = 1;

    public static double[] getCurrentLocation(Activity act){
        LocationManager locationManager = (LocationManager)act.getSystemService(Context.LOCATION_SERVICE);
        double[] localizacao = {655,-655};
        if(ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(act, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                localizacao[0] = latti;
                localizacao[1] = longi;
            }
        }
        return localizacao;
    }


    public static double distanciaEntreDoisPontos(Activity act,double latOri,double longiOri, double latDest, double longiDest){
        double distancia = -1;
        String url = act.getString(R.string.url_matrix_distance)+"&origins="+latOri+","+longiOri+"&destinations="+latDest+","+longiOri+"&key="+act.getString(R.string.api_key_google_matrix);
        try {
            JsonRequestAsync jsonRequest = new JsonRequestAsync(url);
            jsonRequest.execute();
            String json = jsonRequest.getJson(act);
            JSONObject root = new JSONObject(json);
            JSONArray array_rows = root.getJSONArray("rows");
            JSONObject object_rows = array_rows.getJSONObject(0);
            JSONArray array_elements = object_rows.getJSONArray("elements");
            JSONObject  object_elements = array_elements.getJSONObject(0);
//            JSONObject object_duration=object_elements.getJSONObject("duration");
            JSONObject object_distance = object_elements.getJSONObject("distance");
            distancia = Double.parseDouble(object_distance.getString("text").replace("km",""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distancia;
    }

}





