package mypractice.pxy.com.mypractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mypractice.pxy.com.mypractice.entity.User;
import mypractice.pxy.com.mypractice.greendao.DBManager;

public class GreenDaoActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greendao);

        editText = (EditText) findViewById(R.id.edittext);
        textView = (TextView) findViewById(R.id.textview);
        dbManager = DBManager.getInstance(GreenDaoActivity.this);

    }


    public void doClick(View view) {


        int id = view.getId();
        switch (id) {
            case R.id.btn_add:
                User user = new User("pxy", 23, 520730);
                dbManager.insertUser(user);
                User user1 = new User("tcd", 22, 3447);
                dbManager.insertUser(user1);
                User user2 = new User("lebron james", 32, 1314);
                dbManager.insertUser(user2);
                break;

            case R.id.btn_read:
                //查询全部数据
//                List<User> users = dbManager.getUserDao().loadAll();
//                String userName = "";
//                for (int i = 0; i < users.size(); i++) {
//                    userName += users.get(i).toString() + "\n";
//                }
//                textView.setText("查询全部数据==>" + userName);

                //查询指定数据
//                User user4 = dbManager.queryUserByName("pxy");
//                if(user4!=null){
//                    textView.setText(user4.toString());
//                }

                List<User> users = dbManager.queryUserByage(32);
                String userName = "";
                for (int i = 0; i < users.size(); i++) {
                    userName += users.get(i).toString() + "\n";
                }
                textView.setText("查询年龄为32岁的用户数据==>" + userName);

                break;
            case R.id.btn_delete:
//                dbManager.deleteAll();
                dbManager.deleteUser(51);
                break;
            case R.id.btn_update:
                User user3 = new User((long) 31, "pxjpc", 16);
                dbManager.updateUser(user3);
                break;
            default:
                Toast.makeText(this, "666", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
