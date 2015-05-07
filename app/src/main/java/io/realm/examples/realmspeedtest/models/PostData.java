package io.realm.examples.realmspeedtest.models;

import io.realm.RealmObject;

/**
 * Created by mrt on 2015/05/07.
 */
public class PostData extends RealmObject{
    private String postCode;
    private String address;

    public String getPostCode() {
        return postCode;
    }
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}

