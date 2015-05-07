package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.RealmObject;
import io.realm.examples.realmspeedtest.models.PostData;
import io.realm.exceptions.RealmException;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnType;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.LinkView;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;
import io.realm.internal.android.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostDataRealmProxy extends PostData {

    private static long INDEX_POSTCODE;
    private static long INDEX_ADDRESS;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("postCode");
        fieldNames.add("address");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public String getPostCode() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_POSTCODE);
    }

    @Override
    public void setPostCode(String value) {
        realm.checkIfValid();
        row.setString(INDEX_POSTCODE, (String) value);
    }

    @Override
    public String getAddress() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_ADDRESS);
    }

    @Override
    public void setAddress(String value) {
        realm.checkIfValid();
        row.setString(INDEX_ADDRESS, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if(!transaction.hasTable("class_PostData")) {
            Table table = transaction.getTable("class_PostData");
            table.addColumn(ColumnType.STRING, "postCode");
            table.addColumn(ColumnType.STRING, "address");
            table.setPrimaryKey("");
            return table;
        }
        return transaction.getTable("class_PostData");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if(transaction.hasTable("class_PostData")) {
            Table table = transaction.getTable("class_PostData");
            if(table.getColumnCount() != 2) {
                throw new IllegalStateException("Column count does not match");
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for(long i = 0; i < 2; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }
            if (!columnTypes.containsKey("postCode")) {
                throw new IllegalStateException("Missing column 'postCode'");
            }
            if (columnTypes.get("postCode") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'postCode'");
            }
            if (!columnTypes.containsKey("address")) {
                throw new IllegalStateException("Missing column 'address'");
            }
            if (columnTypes.get("address") != ColumnType.STRING) {
                throw new IllegalStateException("Invalid type 'String' for column 'address'");
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException("Field '" + fieldName + "' not found for type PostData");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_POSTCODE = table.getColumnIndex("postCode");
            INDEX_ADDRESS = table.getColumnIndex("address");
        } else {
            throw new RealmMigrationNeededException("The PostData class is missing from the schema for this Realm.");
        }
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static PostData createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        PostData obj = realm.createObject(PostData.class);
        if (!json.isNull("postCode")) {
            obj.setPostCode((String) json.getString("postCode"));
        }
        if (!json.isNull("address")) {
            obj.setAddress((String) json.getString("address"));
        }
        return obj;
    }

    public static PostData createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        PostData obj = realm.createObject(PostData.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("postCode") && reader.peek() != JsonToken.NULL) {
                obj.setPostCode((String) reader.nextString());
            } else if (name.equals("address")  && reader.peek() != JsonToken.NULL) {
                obj.setAddress((String) reader.nextString());
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static PostData copyOrUpdate(Realm realm, PostData object, boolean update, Map<RealmObject,RealmObject> cache) {
        if (object.realm != null && object.realm.getId() == realm.getId()) {
            return object;
        }
        return copy(realm, object, update, cache);
    }

    public static PostData copy(Realm realm, PostData newObject, boolean update, Map<RealmObject,RealmObject> cache) {
        PostData realmObject = realm.createObject(PostData.class);
        cache.put(newObject, realmObject);
        realmObject.setPostCode(newObject.getPostCode() != null ? newObject.getPostCode() : "");
        realmObject.setAddress(newObject.getAddress() != null ? newObject.getAddress() : "");
        return realmObject;
    }

    static PostData update(Realm realm, PostData realmObject, PostData newObject, Map<RealmObject, RealmObject> cache) {
        realmObject.setPostCode(newObject.getPostCode() != null ? newObject.getPostCode() : "");
        realmObject.setAddress(newObject.getAddress() != null ? newObject.getAddress() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("PostData = [");
        stringBuilder.append("{postCode:");
        stringBuilder.append(getPostCode());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{address:");
        stringBuilder.append(getAddress());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDataRealmProxy aPostData = (PostDataRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aPostData.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aPostData.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aPostData.row.getIndex()) return false;

        return true;
    }

}
