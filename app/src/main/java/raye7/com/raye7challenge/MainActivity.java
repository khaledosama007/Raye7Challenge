package raye7.com.raye7challenge;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private DrawerLayout mDrawer;
    private String[] mTabsNames;
    private Toolbar mToolBar;
    private MapView mMapView;
    private GoogleMap maps;
    private EditText mFromSearch;
    private EditText mToSearch;
    private Geocoder mGeocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mFromSearch = (EditText) findViewById(R.id.from_edit_text);
        mToSearch = (EditText) findViewById(R.id.to_edit_text);
        mFromSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean done = false;
                if(i == EditorInfo.IME_ACTION_DONE){
                    done = true;
                    searchMap(textView.getText().toString());
                }
                return done;
            }
        });
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("");
        mToolBar.setNavigationIcon(R.drawable.ic_drawer);
        mTabsNames = new String[3];
        mTabsNames[0] = new String("Tab1");
        mTabsNames[1] = new String("Tab2");
        mTabsNames[2] = new String("Tab3");
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ListView) findViewById(R.id.left_list);
        mListView.setAdapter(
                new ArrayAdapter<String>(this , R.layout.drawer_list_item, mTabsNames));
        mMapView = (MapView)findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
    }
    public void searchMap(String add){
        mGeocoder = new Geocoder(getBaseContext());
        List<Address> adds = null;
        try{
            adds = mGeocoder.getFromLocationName(add , 4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(adds==null){
            Toast.makeText(this  , "Cant find this address" , Toast.LENGTH_LONG).show();
        }else{
            Address address = (Address) adds.get(0);
            LatLng  latLng= new LatLng(address.getLatitude() , address.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(120));
            maps.addMarker(markerOptions);
            maps.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
