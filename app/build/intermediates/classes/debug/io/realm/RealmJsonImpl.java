package io.realm;


import android.util.JsonReader;
import io.realm.exceptions.RealmException;
import io.realm.internal.RealmJson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import io.realm.examples.realmspeedtest.models.PostData;

class RealmJsonImpl
    implements RealmJson {

    @Override
    public <E extends RealmObject> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        if (clazz.equals(PostData.class)) {
            return (E) PostDataRealmProxy.createOrUpdateUsingJsonObject(realm, json, update);
        } else {
            throw new RealmException("Could not find the generated proxy class for " + clazz);
        }
    }

    @Override
    public <E extends RealmObject> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        if (clazz.equals(PostData.class)) {
            return (E) PostDataRealmProxy.createUsingJsonStream(realm, reader);
        } else {
            throw new RealmException("Could not find the generated proxy class for " + clazz);
        }
    }

}
