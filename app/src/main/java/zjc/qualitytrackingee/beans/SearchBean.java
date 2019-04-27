package zjc.qualitytrackingee.beans;

import java.util.List;

/**
 * Created by cold on 2017/7/24.
 */

public class SearchBean {
    private List<search> search;

    public List<SearchBean.search> getSearch() {
        return search;
    }

    public void setSearch(List<SearchBean.search> search) {
        this.search = search;
    }

    public class search{
        private String cid;
        private String c_name;


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


    }
}
