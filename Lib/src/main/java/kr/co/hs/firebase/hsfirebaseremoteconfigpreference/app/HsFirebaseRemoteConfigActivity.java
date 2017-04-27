package kr.co.hs.firebase.hsfirebaseremoteconfigpreference.app;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import kr.co.hs.app.HsActivity;
import kr.co.hs.firebase.hsfirebaseremoteconfigpreference.HsFirebaseRemoteConfig;

/**
 * Created by Bae on 2016-12-31.
 */

public abstract class HsFirebaseRemoteConfigActivity extends HsActivity implements HsFirebaseRemoteConfig.OnFetchResultListener{
    private HsFirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseRemoteConfig = HsFirebaseRemoteConfig.getInstance();

        getRemoteConfig().syncConfig(0, this);
        Map<String, Object> defaults = new HashMap<>();
        setDefaults(defaults);
        if(defaults.size() > 0)
            getRemoteConfig().setDefaults(defaults);
    }

    public HsFirebaseRemoteConfig getRemoteConfig(){
        return this.mFirebaseRemoteConfig;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public String getRemoteConfigString(String key){
        return this.getRemoteConfig().getString(key);
    }

    public long getRemoteConfigLong(String key){
        return this.getRemoteConfig().getLong(key);
    }

    public boolean getRemoteConfigBoolean(String key){
        return this.getRemoteConfig().getBoolean(key);
    }

    protected abstract void setDefaults(Map<String, Object> map);
}
