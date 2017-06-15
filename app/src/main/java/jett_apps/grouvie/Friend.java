package jett_apps.grouvie;

import java.io.Serializable;

public class Friend implements Serializable {
    private String name;
    private String phoneNum;
    private boolean checked = false;
    private boolean madeChange = false;

    public Friend(String name, String phoneNum){
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String toString() {
        return name;
    }
    public void toggleChecked() {
        checked = !checked;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        return phoneNum.equals(friend.phoneNum);

    }

    @Override
    public int hashCode() {
        return phoneNum.hashCode();
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
