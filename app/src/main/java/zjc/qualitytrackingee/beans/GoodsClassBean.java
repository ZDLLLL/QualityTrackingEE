package zjc.qualitytrackingee.beans;

import android.support.annotation.NonNull;

import zjc.qualitytrackingee.utils.Cn2Spell;

public class GoodsClassBean implements Comparable<GoodsClassBean>{

    private String co_class;
    private String co_classid;
    private String pinyin; // 姓名对应的拼音
    private String firstLetter; // 拼音的首字母
    public GoodsClassBean() {
    }
    public GoodsClassBean(String co_class){
        this.co_class=co_class;
        pinyin = Cn2Spell.getPinYin(co_class); // 根据姓名获取拼音
        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }  public GoodsClassBean(String co_class,String co_classid){
        this.co_class=co_class;
        this.co_classid=co_classid;
        pinyin = Cn2Spell.getPinYin(co_class); // 根据姓名获取拼音
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


    public String getCo_class() {
        return co_class;
    }

    public void setCo_class(String co_class) {
        this.co_class = co_class;
    }

    public String getCo_classid() {
        return co_classid;
    }

    public void setCo_classid(String co_classid) {
        this.co_classid = co_classid;
    }

    @Override
    public int compareTo(@NonNull GoodsClassBean companyBean) {
        if (firstLetter.equals("#") && !companyBean.getFirstLetter().equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && companyBean.getFirstLetter().equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(companyBean.getPinyin());
        }
    }
}
