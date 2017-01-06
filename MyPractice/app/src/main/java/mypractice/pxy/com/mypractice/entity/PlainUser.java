package mypractice.pxy.com.mypractice.entity;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by Administrator on 2016/12/28.
 */


public class PlainUser {
    public final ObservableField<String> firstName = new ObservableField<>();
    public final ObservableField<String> lastName = new ObservableField<>();
    public final ObservableInt age = new ObservableInt();
    public  final ObservableField<Boolean>  adult=new ObservableField<>();

    public ObservableField<String> getFirstName() {
        return firstName;
    }

    public ObservableField<String> getLastName() {
        return lastName;
    }

    public ObservableInt getAge() {
        return age;
    }

    public ObservableField<Boolean> getAdult() {
        return adult;
    }
}
