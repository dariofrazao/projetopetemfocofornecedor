package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.Localizacao;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

public class CadastroGeolocalizacaoActivity extends AppCompatActivity {
    private MapView mMapView;
    private LatLng mLocalizacaoFornecedor;
    private LocationManager locationManager;
    private CameraUpdate mMinhaPosicao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocalizacao);

        // Associa os componetes ao layout XML
        Toolbar toolbar = findViewById(R.id.tb_geolocalizacao);
        mMapView = findViewById(R.id.map_view);
        Button mButtonConfirmar = findViewById(R.id.btnConfirmarEdicao);

        // Configura toolbar
        toolbar.setTitle(R.string.tb_geolocalizacao_titulo);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left_white);
        setSupportActionBar(toolbar);

        // Inicializa o maps
        MapsInitializer.initialize(this);

        //Receber os dados do serviço de outra activity
        Intent i = getIntent();
        final String mCep = (String) i.getSerializableExtra("CEP");
        mMinhaPosicao = CameraUpdateFactory.newLatLngZoom(coordinadasPorCep(mCep), 19);

        // Gets to GoogleMap from the MapView and does initialization stuff
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setUpMap(googleMap);
            }
        });
        mMapView.onCreate(savedInstanceState);

        // Confirmar e retornar o fornecedor com as coordenadas geograficas
        mButtonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Confirmar e retornar o fornecedor com as coordenadas geograficas
        mButtonConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void setUpMap(GoogleMap googleMap) {
        verificarGPS();
        final GoogleMap mGoogleMap = configuracaoMap(googleMap);
        mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mLocalizacaoFornecedor = latLng;
                // Adiciona no map a localização do estabelecimento
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(mLocalizacaoFornecedor).title("Fornecedor"));
                mLocalizacaoFornecedor = latLng;
            }
        });

        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mLocalizacaoFornecedor = latLng;
                // Adiciona no map a localização do estabelecimento
                mGoogleMap.clear();
                mGoogleMap.addMarker(new MarkerOptions().position(mLocalizacaoFornecedor).title("Fornecedor"));
                mLocalizacaoFornecedor = latLng;
            }
        });

//        // Busca minha localização
//        CameraUpdate mMinhaPosicao = CameraUpdateFactory.newLatLngZoom(minhaLocalizacao(act), 22);

        // Direciona a camera para a local do estabelecimento
        mGoogleMap.moveCamera(mMinhaPosicao);
    }

    // Configura o visial do Mapa
    private GoogleMap configuracaoMap(GoogleMap googleMap) {
        googleMap.setMapType(googleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            verificarGPS();
        }

        return googleMap;
    }

//    // Minha localização
//    private LatLng coordinadasPorGPS(Activity act) {
//        verificarGPS();
//        LatLng mMinhaLocalizacao = null;
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            double[] posicaoAtual = Localizacao.getCurrentLocation(act);
//            mMinhaLocalizacao = new LatLng(posicaoAtual[0],posicaoAtual[1]);
//        }else{
//            mMinhaLocalizacao = new LatLng(0,0);
//        }
//        return mMinhaLocalizacao;
//    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarGPS();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onBackPressed() {
        Toast mToast;
        try {
            // Verifica as coordenadas do fornecedor
            VerificadorDeObjetos.vDadosObrCoordenadasGeograficas(mLocalizacaoFornecedor);
            CadastroEnderecoActivity.localizacao = mLocalizacaoFornecedor;
            super.onBackPressed();
            finish();
        } catch (ValidacaoException e) {
            mToast = Toast.makeText(CadastroGeolocalizacaoActivity.this, R.string.erro_coordenadas_geograficas_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
            mToast.show();
        } catch (Exception e) {
            mToast = Toast.makeText(CadastroGeolocalizacaoActivity.this, R.string.erro_coordenadas_geograficas_campos_obrigatorios_Toast, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }


    private void verificarGPS() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            perdirParaLigarGPS();
            //Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
        }

    }

    private void perdirParaLigarGPS() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS está desligado, deseja liga-lo?")
                .setCancelable(false)
                .setPositiveButton("Vá para as configurações de localização para ativa-lo",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancelar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    //Método que busca a geolocalização baseda no cep
    public static LatLng coordinadasPorCep(String cep) {
        LatLng locationPoint = new LatLng(0,0);

        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            url = new URL("https://maps.google.com/maps/api/geocode/json?address=" + cep + "&key=AIzaSyA5ajs6LhgYb3YfbuQ-hyDrUq4GYMjMkT0");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject json;
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            String result = line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }

            String lat;
            String lng;
            json = new JSONObject(result);
            JSONObject geoMetryObject = new JSONObject();
            JSONObject locations = new JSONObject();
            JSONArray jarr = json.getJSONArray("results");
            int i;
            for (i = 0; i < jarr.length(); i++) {
                json = jarr.getJSONObject(i);
                geoMetryObject = json.getJSONObject("geometry");
                locations = geoMetryObject.getJSONObject("location");
                lat = locations.getString("lat");
                lng = locations.getString("lng");
                locationPoint = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locationPoint;
    }

}
