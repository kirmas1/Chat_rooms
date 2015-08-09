package com.sagikirma.chat_rooms.Map_and_RoomsList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.common.api.zzi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.concurrent.TimeUnit;

import com.sagikirma.chat_rooms.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class mapFragment extends Fragment implements GoogleApiClient, GoogleApiClient.OnConnectionFailedListener, android.location.LocationListener, LocationListener, GoogleApiClient.ConnectionCallbacks{

    private static final long INTERVAL = 1000*10;
    private static final long FASTEST_INTERVAL =1000*5;

    MapView mMapView;
    private GoogleMap googleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLocation;
    SwipeRefreshLayout swipeLayout;

    ////---onCreate--start----------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();

        if (!isGooglePlayServicesAvailable()) getActivity().finish();

        createLocationRequest();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        swipeLayout = (SwipeRefreshLayout)view.findViewById(R.id.map_swipeLayout);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(getActivity(), ReloadService.class);
                getActivity().startService(intent);
            }
        });

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ReloadService.DONE);
        getActivity().registerReceiver(reloadDone, intentFilter);

        // Perform any camera updates here
        return view;
    }

    ////end of onCreate   ------------

    private BroadcastReceiver reloadDone = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            swipeLayout.setRefreshing(false);
            Toast.makeText(getActivity(),"Reload is done", Toast.LENGTH_SHORT).show();
        }
    };

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        try {
            getActivity().unregisterReceiver(reloadDone);
        }
        catch (Exception ex) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mGoogleApiClient.isConnected()) startLocationUpdates();

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

    @Override
    public <A extends Api.Client, R extends Result, T extends zza.zza<R, A>> T zza(T t) {
        return null;
    }

    @Override
    public <A extends Api.Client, T extends zza.zza<? extends Result, A>> T zzb(T t) {
        return null;
    }

    @Override
    public <L> zzi<L> zzo(L l) {
        return null;
    }

    @Override
    public <C extends Api.Client> C zza(Api.ClientKey<C> clientKey) {
        return null;
    }

    @Override
    public boolean zza(Api<?> api) {
        return false;
    }

    @Override
    public boolean hasConnectedApi(Api<?> api) {
        return false;
    }

    @Override
    public ConnectionResult getConnectionResult(Api<?> api) {
        return null;
    }

    @Override
    public boolean zza(Scope scope) {
        return false;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public Looper getLooper() {
        return null;
    }

    @Override
    public void connect() {

    }

    @Override
    public ConnectionResult blockingConnect() {
        return null;
    }

    @Override
    public ConnectionResult blockingConnect(long l, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public void reconnect() {

    }

    @Override
    public PendingResult<Status> clearDefaultAccountAndReconnect() {
        return null;
    }

    @Override
    public void stopAutoManage(FragmentActivity fragmentActivity) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isConnecting() {
        return false;
    }

    @Override
    public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {

    }

    @Override
    public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
        return false;
    }

    @Override
    public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {

    }

    @Override
    public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {

    }

    @Override
    public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
        return false;
    }

    @Override
    public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {

    }

    @Override
    public int getSessionId() {
        return 0;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;

        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(mLocation.getLatitude(), mLocation.getLongitude())).title("my location - kirma");

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), "Failed to connect to location updates",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getActivity(), "Starting location updates",Toast.LENGTH_SHORT).show();
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, (com.google.android.gms.location.LocationListener)this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
