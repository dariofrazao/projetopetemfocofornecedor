package projetaobcc20172.com.projetopetemfocofornecedor.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

import projetaobcc20172.com.projetopetemfocofornecedor.R;
import projetaobcc20172.com.projetopetemfocofornecedor.excecoes.ValidacaoException;
import projetaobcc20172.com.projetopetemfocofornecedor.utils.VerificadorDeObjetos;

public class CadastroGeolocalizacaoActivity extends AppCompatActivity {
    private MapView mMapView;
     private LatLng mLocalizacaoFornecedor;

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
                mLocalizacaoFornecedor=latLng;
            }
        });

        // Busca minha localização
        CameraUpdate mMinhaPosicao = CameraUpdateFactory.newLatLngZoom(minhaLocalizacao(), 13);

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
        return googleMap;
    }

    // Minha localização
    private LatLng minhaLocalizacao() {
        LatLng mMinhaLocalizacao;
        // Getting LocationManager object from System Service LOCATION_SERVICE
        LocationManager mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Creating a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Getting the name of the best provider
        String provider = mLocationManager.getBestProvider(criteria, true);

        // Getting Current Location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return  mMinhaLocalizacao = new LatLng(-8.9067588, -36.4943075);
        }
        Location mLocation = mLocationManager.getLastKnownLocation(provider);

        if (mLocation != null) {
            // Getting latitude of the current location
            double latitude = mLocation.getLatitude();

            // Getting longitude of the current location
            double longitude = mLocation.getLongitude();

            // Creating a LatLng object for the current location
            mMinhaLocalizacao = new LatLng(latitude, longitude);
        }else{
            mMinhaLocalizacao = new LatLng(-8.9067588, -36.4943075);
        }
        return  mMinhaLocalizacao;
    }

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
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onBackPressed(){
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
}
