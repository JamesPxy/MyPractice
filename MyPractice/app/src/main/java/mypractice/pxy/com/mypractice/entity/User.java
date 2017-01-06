package mypractice.pxy.com.mypractice.entity;

import android.databinding.BaseObservable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;


/**
 * Created by Administrator on 2016/12/27.
 */
//@Entity(
//        // 如果你有超过一个的数据库结构，可以通过这个字段来区分
//        // 该实体属于哪个结构
////        schema = "mySchema",
//
//        //  实体是否激活的标志，激活的实体有更新，删除和刷新的方法
//        active = true,
//
//        // 确定数据库中表的名称
//        // 表名称默认是实体类的名称
////        nameInDb = "user_tb",
//
//        // Define indexes spanning multiple columns here.
//        indexes = {
//                @Index(value = "name DESC", unique = true)
//        }
//
//        // DAO是否应该创建数据库表的标志(默认为true)
//        // 如果你有多对一的表，将这个字段设置为false
//        // 或者你已经在GreenDAO之外创建了表，也将其置为false
////        createInDb = false
//)
/**
 * Entity mapped to table "NOTE".
 */
@Entity(indexes = {
        @Index(value = "name, age DESC", unique = false)
})
public class User extends BaseObservable{

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "userName")
    @NotNull
    public String name="";

    private int age;

//    @ToOne(joinProperty ="customerId")
//    private Customer customer;

    @Transient
    private int tempPassWord; //不存入数据库

    public User(@NotNull String name, int age, int tempPassWord) {
        this.name = name;
        this.age = age;
        this.tempPassWord = tempPassWord;
    }

    @Generated(hash = 955858333)
    public User(Long id, @NotNull String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * Not-null value; ensure this value is available before it is saved to the database.
     */
    public void setName(@NotNull String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTempPassWord() {
        return tempPassWord;
    }

    public void setTempPassWord(int tempPassWord) {
        this.tempPassWord = tempPassWord;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", tempPassWord=" + tempPassWord +
                '}';
    }
}
