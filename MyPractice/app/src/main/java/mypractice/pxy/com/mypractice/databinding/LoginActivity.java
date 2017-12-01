package mypractice.pxy.com.mypractice.databinding;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mypractice.pxy.com.mypractice.R;
import mypractice.pxy.com.mypractice.entity.ObservableUser;
import mypractice.pxy.com.mypractice.entity.PlainUser;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ObservableUser user;

    private PlainUser  plainUser;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);

        //生成bindview的名称  为 activity_login  变成大些
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        user = new ObservableUser("pxy", "tcd",true);
        binding.setUser(user);


    }

    public void doClick(View view) {
//        user.setFirstName("pxy tcd" + index++);
        user.setLastName("king james" + index++);
        user.setAdult(false);
    }

}

