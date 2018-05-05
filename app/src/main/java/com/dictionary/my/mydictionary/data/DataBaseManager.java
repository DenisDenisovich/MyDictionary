package com.dictionary.my.mydictionary.data;

import android.content.Context;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Endpoint;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Replicator;
import com.couchbase.lite.ReplicatorChange;
import com.couchbase.lite.ReplicatorChangeListener;
import com.couchbase.lite.ReplicatorConfiguration;
import com.couchbase.lite.URLEndpoint;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * Created by luxso on 02.05.2018.
 */

public class DataBaseManager {
    private static final String LOG_TAG = "Log_dbManager";
    private static DataBaseManager instance = null;
    public Database databaseWords;
    public Database databaseGroups;
    protected DataBaseManager(Context context){
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        try {
            databaseWords = new Database(CBKeys.DB_WORDS, config);
            databaseGroups = new Database(CBKeys.DB_GROUPS, config);
            if(databaseGroups.getCount() == 0){
                MutableDocument defaultGroup = new MutableDocument();
                defaultGroup.setString(CBKeys.KEY_TYPE, CBKeys.GROUP_TYPE);
                defaultGroup.setString(CBKeys.KEY_TITLE, CBKeys.DEFAULT_GROUP);
                defaultGroup.setDate(CBKeys.KEY_DATE, new Date());
                databaseGroups.save(defaultGroup);
            }

            Endpoint targetEndpoint = new URLEndpoint(new URI("ws://localhost:4984/dbwords"));
            ReplicatorConfiguration replConfig = new ReplicatorConfiguration(databaseWords, targetEndpoint);
            replConfig.setReplicatorType(ReplicatorConfiguration.ReplicatorType.PUSH_AND_PULL);
            replConfig.setContinuous(true);
            Replicator replication = new Replicator(replConfig);
            replication.start();
            replication.addChangeListener(new ReplicatorChangeListener() {
                @Override
                public void changed(ReplicatorChange change) {
                    Log.d(LOG_TAG,change.toString());
                }
            });


            Endpoint targetEndpoint2 = new URLEndpoint(new URI("ws://localhost:4984/dbgroups"));
            ReplicatorConfiguration replConfig2 = new ReplicatorConfiguration(databaseGroups, targetEndpoint2);
            replConfig2.setReplicatorType(ReplicatorConfiguration.ReplicatorType.PUSH_AND_PULL);
            replConfig2.setContinuous(true);
            Replicator replication2 = new Replicator(replConfig2);
            replication2.start();
            replication2.addChangeListener(new ReplicatorChangeListener() {
                @Override
                public void changed(ReplicatorChange change) {
                    Log.d(LOG_TAG,change.toString());
                }
            });


        }catch (CouchbaseLiteException e){
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static DataBaseManager getSharedInstance(Context context){
        if(instance == null){
            instance = new DataBaseManager(context);
        }
        return instance;
    }
}
