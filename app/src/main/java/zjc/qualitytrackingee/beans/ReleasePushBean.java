package zjc.qualitytrackingee.beans;

import java.util.List;

public class ReleasePushBean {
    private List<ReleasePushBean> channellist;

    public List<ReleasePushBean> getChannelist() {
        return channellist;
    }

    public void setChannelList(List<ReleasePushBean> channellist) {
        this.channellist = channellist;
    }

    public static class ReleasePushsBean {
        private String releasepushName; //渠道名称
        private String releasepushPhone; //渠道数据值
        private boolean isSelected; //自定义列表是否选中的标识

        public String getReleasepushName() {
            return releasepushName;
        }

        public void setReleasepushName(String releasepushName) {
            this.releasepushName = releasepushName;
        }

        public String getReleasepushPhone() {
            return releasepushPhone;
        }

        public void setReleasepushPhone(String releasepushPhone) {
            this.releasepushPhone = releasepushPhone;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
