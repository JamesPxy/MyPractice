package mypractice.pxy.com.mypractice.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import mypractice.pxy.com.mypractice.BR;


/**
 * Created by Administrator on 2016/12/28.
 */

public class ObservableUser extends BaseObservable{
    private String firstName;
    private String lastName;

    public boolean adult=true;

    public ObservableUser(String firstName, String lastName, boolean adult) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.adult = adult;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
        notifyPropertyChanged(BR.adult);
    }
}