package kr.co.hs.firebase.hsfirebaseremoteconfigpreference;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Map;

/**
 * Created by Bae on 2016-12-31.
 */

public class HsFirebaseRemoteConfig implements OnCompleteListener<Void>{
    private static HsFirebaseRemoteConfig instance = null;
    public static HsFirebaseRemoteConfig getInstance(){
        if(instance == null)
            instance = new HsFirebaseRemoteConfig();
        return instance;
    }

    private FirebaseRemoteConfig firebaseRemoteConfig;
    private OnFetchResultListener onFetchResultListener;

    private HsFirebaseRemoteConfig(){
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .build();
        firebaseRemoteConfig.setConfigSettings(settings);
    }

    private FirebaseRemoteConfig getRemoteConfig(){
        return this.firebaseRemoteConfig;
    }

    public void syncConfig(OnFetchResultListener listener){
        syncConfig(-1, listener);
    }

    public void syncConfig(long expire, OnFetchResultListener listener){
        this.onFetchResultListener = listener;
        if(expire >= 0)
            getRemoteConfig().fetch(expire).addOnCompleteListener(this);
        else
            getRemoteConfig().fetch().addOnCompleteListener(this);
    }

    public void setDefaults(Map<String, Object> map){
        getRemoteConfig().setDefaults(map);
    }

    public String getString(String key){
        return getRemoteConfig().getString(key);
    }

    public boolean getBoolean(String key){
        return getRemoteConfig().getBoolean(key);
    }

    public long getLong(String key){
        return getRemoteConfig().getLong(key);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            boolean result = firebaseRemoteConfig.activateFetched();
            if(result)
                this.onFetchResultListener.onSuccessfulActivateFetched(this);
            else
                this.onFetchResultListener.onFailureActivateFetched(this);
        }else{
            this.onFetchResultListener.onFailureActivateFetched(this);
        }
    }

    public interface OnFetchResultListener{
        void onSuccessfulActivateFetched(HsFirebaseRemoteConfig remoteConfig);
        void onFailureActivateFetched(HsFirebaseRemoteConfig remoteConfig);
    }
}
