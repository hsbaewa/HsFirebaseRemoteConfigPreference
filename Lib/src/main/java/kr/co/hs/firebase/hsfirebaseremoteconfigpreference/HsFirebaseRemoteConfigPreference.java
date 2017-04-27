package kr.co.hs.firebase.hsfirebaseremoteconfigpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Iterator;
import java.util.Map;

import kr.co.hs.content.advancedpreference.AdvancedPreference;

/**
 * 생성된 시간 2017-04-27, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsFirebaseRemoteConfigPreference
 * 패키지명 : kr.co.hs.firebase.hsfirebaseremoteconfigpreference
 */

public class HsFirebaseRemoteConfigPreference extends AdvancedPreference implements OnCompleteListener<Void> {
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public HsFirebaseRemoteConfigPreference(SharedPreferences mSharedPreferences) {
        super(mSharedPreferences);
        init();
    }

    public HsFirebaseRemoteConfigPreference(Context context) {
        super(context);
        init();
    }

    public HsFirebaseRemoteConfigPreference(Context context, String name, int mode) {
        super(context, name, mode);
        init();
    }

    private void init(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings settings = new FirebaseRemoteConfigSettings.Builder()
                .build();
        mFirebaseRemoteConfig.setConfigSettings(settings);
    }

    public FirebaseRemoteConfig getRemoteConfig(){
        return this.mFirebaseRemoteConfig;
    }

    private Task<Void> fetch(){
        return this.mFirebaseRemoteConfig.fetch();
    }
    private Task<Void> fetch(long cacheExpire){
        return this.mFirebaseRemoteConfig.fetch(cacheExpire);
    }
    private boolean activateFetched(){
        return this.mFirebaseRemoteConfig.activateFetched();
    }
    public void syncFetched(){
        syncFetched(-1);
    }
    public void syncFetched(long cacheExpire){
        synchronized (this){
            Map<String, Object> defaults = getCacheDataMap();
            if(defaults.size() > 0)
                getRemoteConfig().setDefaults(getCacheDataMap());
        }
        if(cacheExpire < 0)
            this.fetch().addOnCompleteListener(this);
        else
            this.fetch(cacheExpire).addOnCompleteListener(this);
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        if(task.isSuccessful()){
            activateFetched();
            synchronized (this){
                Map<String, ?> allData = getAll();
                Iterator<String> keySet = allData.keySet().iterator();
                while(keySet.hasNext()){
                    String key = keySet.next();
                    Object obj = allData.get(key);
                    if(obj instanceof String) {
                        String str = getRemoteConfig().getString(key);
                        set(key, str);
                    } else if(obj instanceof Integer) {
                        long l = getRemoteConfig().getLong(key);
                        set(key, (int)l);
                    } else if(obj instanceof Long) {
                        long l = getRemoteConfig().getLong(key);
                        set(key, l);
                    } else if(obj instanceof Float) {
                        double d = getRemoteConfig().getDouble(key);
                        set(key, (float)d);
                    } else if(obj instanceof Boolean) {
                        boolean b = getRemoteConfig().getBoolean(key);
                        set(key, b);
                    }
                }
                commit();
            }
        }
    }
}
