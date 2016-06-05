package com.sorcerer.sorcery.iconpack.xposed.theme;

public class IconReplacementItem {
    public static String TAG = "SIP/IconReplacementItem";
    private String activityName;
    private String component;
    private boolean noCustomIcon = true;
    private int origRes;
    private String origResName;
    private String packageName;
    private int replacementRes;
    private String replacementResName;

    public IconReplacementItem(String packageName, String activityName) {
        this.packageName = packageName;
        this.activityName = activityName;
    }

    public IconReplacementItem(String component, int replacementRes, String replacementResName) {
        this.component = component;
        this.replacementRes = replacementRes;
        this.replacementResName = replacementResName;
        String stripped = component.replace("ComponentInfo{", "").replace("}", "");
        if (!stripped.equals("")) {
            String[] splitComponentInfo = stripped.split("/");
            this.packageName = splitComponentInfo[0];
            if (splitComponentInfo.length > 1) {
                this.activityName = splitComponentInfo[1];
            }
        }
        this.noCustomIcon = false;
    }

    public String getComponent() {
        return this.component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return this.activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public int getReplacementRes() {
        return this.replacementRes;
    }

    public void setReplacementRes(int replacementRes) {
        this.replacementRes = replacementRes;
    }

    public String getReplacementResName() {
        return this.replacementResName;
    }

    public void setReplacementResName(String replacementResName) {
        this.replacementResName = replacementResName;
    }

    public int getOrigRes() {
        return this.origRes;
    }

    public void setOrigRes(int origRes) {
        this.origRes = origRes;
    }

    public String getOrigResName() {
        return this.origResName;
    }

    public void setOrigResName(String origResName) {
        this.origResName = origResName;
    }

    public boolean hasNoCustomIcon() {
        return this.noCustomIcon;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (this.origResName.equals(((IconReplacementItem) o).origResName)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return this.origResName.hashCode();
    }

    public String toString() {
        return "IconReplacementItem{component='" + this.component + '\'' + ", packageName='"
                + this.packageName + '\'' + ", activityName='" + this.activityName + '\''
                + ", replacementRes=" + this.replacementRes + ", replacementResName="
                + this.replacementResName + ", origRes=" + this.origRes + ", origResName="
                + this.origResName + '}';
    }
}
