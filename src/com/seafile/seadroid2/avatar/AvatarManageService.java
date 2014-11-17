package com.seafile.seadroid2.avatar;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.seafile.seadroid2.ConcurrentAsyncTask;
import com.seafile.seadroid2.SeafException;
import com.seafile.seadroid2.account.Account;
import com.seafile.seadroid2.account.AccountManager;

/**
 *  start avatar manager from background   
 *
 */
public class AvatarManageService extends Service {
    private static final String DEBUG_TAG = "AvatarManageService";

    private ArrayList<Account> accounts;
    private AccountManager accountManager;
    
    @Override
    public void onCreate() {
        Log.d(DEBUG_TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, "onStartCommand");
        accountManager = new AccountManager(getApplicationContext());
        accounts = (ArrayList<Account>) accountManager.getAccountList();
        ConcurrentAsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AvatarManager avatarManager = new AvatarManager(accounts);
                try {
                    avatarManager.getAvatars(48);
                } catch (SeafException e) {
                    e.printStackTrace();
                }
            }
        });
        return START_STICKY;
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}