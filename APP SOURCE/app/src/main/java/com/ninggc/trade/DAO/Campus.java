package com.ninggc.trade.DAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ninggc.trade.address.IEntity;

/**
 * Created by Ning on 7/24/2017 0024.
 */
public class Campus implements IBean, Parcelable, IEntity {
    @JSONField(name = "id")
    private String id;

    @JSONField(name = "name")
    private String name;
    
    /**
     * 是否选中。
     */
    private boolean isSelect;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campus campus = (Campus) o;

        if (isSelect != campus.isSelect) return false;
        if (id != null ? !id.equals(campus.id) : campus.id != null) return false;
        return name != null ? name.equals(campus.name) : campus.name == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (isSelect ? 1 : 0);
        return result;
    }

    public Campus() {}

    protected Campus(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public static final Creator<Campus> CREATOR = new Creator<Campus>() {

        @Override
        public Campus createFromParcel(Parcel source) {
            return new Campus(source);
        }

        @Override
        public Campus[] newArray(int size) {
            return new Campus[size];
        }
    };

//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(id);
//        dest.writeString(name);
//        dest.writeTypedList(campusList);
//        dest.writeByte((byte) (isSelect ? 1 : 0));
//    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Creator<String> CREATOR = new Creator<Campus>() {
//        @Override
//        public String createFromParcel(Parcel in) {
//            return new String(in);
//        }
//
//        @Override
//        public String[] newArray(int size) {
//            return new String[size];
//        }
//    };
}
