package zjc.qualitytrackingee.beans;

import android.support.annotation.NonNull;

import zjc.qualitytrackingee.utils.Cn2Spell;

public class CompanyBean implements Comparable<CompanyBean>{
    private String c_place;
    private String c_url;
    private String cid;
    private String c_name;
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母
    public CompanyBean() {
    }
    public CompanyBean(String c_name){
        this.c_name=c_name;
        pinyin = Cn2Spell.getPinYin(c_name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }
    public CompanyBean(String c_name,String cid){
        this.c_name=c_name;
        this.cid=cid;
        pinyin = Cn2Spell.getPinYin(c_name); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public void setFirstLetter(String firstLetter) {
        this.firstLetter = firstLetter;
    }

    public String getC_place() {
        return c_place;
    }

    public void setC_place(String c_place) {
        this.c_place = c_place;
    }

    public String getC_url() {
        return c_url;
    }

    public void setC_url(String c_url) {
        this.c_url = c_url;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }


    @Override
    public int compareTo(@NonNull CompanyBean companyBean) {
        if (firstLetter.equals("#") && !companyBean.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && companyBean.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(companyBean.getPinyin());
        }
    }
}
