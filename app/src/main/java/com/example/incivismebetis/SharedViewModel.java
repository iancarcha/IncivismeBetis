package com.example.incivismebetis;

import android.annotation.SuppressLint;
import android.app.Application;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;


public class SharedViewModel extends AndroidViewModel {

    private static final MutableLiveData<String> currentAddress = new MutableLiveData<>();
    private final Application app;
    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private final MutableLiveData<String> checkPermission = new MutableLiveData<>(); 
    private final MutableLiveData<String> buttonText = new MutableLiveData<>();      
    private final MutableLiveData<Boolean> progressBar = new MutableLiveData<>();
    private MutableLiveData<LatLng> currentLatLng = new MutableLiveData<>();
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            if (locationResult != null) {
                fetchAddress(locationResult.getLastLocation());
            }
        }
    };

    FusedLocationProviderClient mFusedLocationClient;
    private boolean mTrackingLocation;

    public SharedViewModel(@NonNull Application application) {
        super(application);

        this.app = application;
    }
    public static LiveData<String> getCurrentAddress() {
        return currentAddress;
    }

    public MutableLiveData<LatLng> getCurrentLatLng() {
        return currentLatLng;
    }
    void setFusedLocationClient(FusedLocationProviderClient mFusedLocationClient) {
        this.mFusedLocationClient = mFusedLocationClient;
    }
    public MutableLiveData<LatLng> getCurrentUser () {
        if (currentLatLng == null){
            currentLatLng = new MutableLiveData<>();
        }
        Log.e("sharedView", "ShareViewModel "+currentLatLng.getValue());
        return currentLatLng;
    }


    public MutableLiveData<String> getButtonText() {
        return buttonText;
    }

    public MutableLiveData<Boolean> getProgressBar() {
        return progressBar;
    }

    private void fetchAddress(Location location) {


        try {
            LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
            currentLatLng.postValue(latlng);

        } catch (Exception ioException) {
            ioException.printStackTrace();
        }

    }

    LiveData<String> getCheckPermission() { 
        return checkPermission;
    }



    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public void switchTrackingLocation() {
        if (!mTrackingLocation) {
            startTrackingLocation(true);
        } else {
            stopTrackingLocation();
        }

    }

    @SuppressLint("MissingPermission")
    void startTrackingLocation(boolean needsChecking) {
        if (needsChecking) {                             
            checkPermission.postValue("check");
        } else {
            mFusedLocationClient.requestLocationUpdates( 
                    getLocationRequest(),
                    mLocationCallback, null
            );

            currentAddress.postValue("Carregant...");

            progressBar.postValue(true);
            mTrackingLocation = true;
            buttonText.setValue("Aturar el seguiment de la ubicació");
        }
    }


    private void stopTrackingLocation() {
        if (mTrackingLocation) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            mTrackingLocation = false;
            progressBar.postValue(false);
            buttonText.setValue("Comença a seguir la ubicació");
        }
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void setUser(FirebaseUser passedUser) {
        user.postValue(passedUser);
    }


}
