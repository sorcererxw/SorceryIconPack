package com.sorcerer.sorcery.iconpack.xposed.theme;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Random;

public class IconMaskItem {
    private ArrayList<Integer> iconback;
    private int iconbackCounter = 0;
    private ArrayList<Integer> iconmask;
    private int iconmaskCounter = 0;
    private ArrayList<Integer> iconupon;
    private int iconuponCounter = 0;
    private String packageName;
    private float scale = 1.0f;

    public IconMaskItem(String packageName) {
        this.packageName = packageName;
        this.iconback = new ArrayList();
        this.iconmask = new ArrayList();
        this.iconupon = new ArrayList();
    }

    public int getRandomOf(ArrayList<Integer> list) {
        if (list.size() <= 0) {
            return 0;
        }
        return ((Integer) list.get(generateRandomBetween(0, list.size()))).intValue();
    }

    public ArrayList<Integer> getIconback() {
        return this.iconback;
    }

    public void addIconback(String iconback, Resources res) {
        if (res.getIdentifier(iconback, "drawable", this.packageName) != 0) {
            this.iconback.add(Integer.valueOf(res.getIdentifier(iconback, "drawable", this.packageName)));
        }
    }

    public ArrayList<Integer> getIconmask() {
        return this.iconmask;
    }

    public void setIconmask(String iconmask, Resources res) {
        if (res.getIdentifier(iconmask, "drawable", this.packageName) != 0) {
            this.iconmask.add(Integer.valueOf(res.getIdentifier(iconmask, "drawable", this.packageName)));
        }
    }

    public ArrayList<Integer> getIconupon() {
        return this.iconupon;
    }

    public void setIconupon(String iconupon, Resources res) {
        if (res.getIdentifier(iconupon, "drawable", this.packageName) != 0) {
            this.iconupon.add(Integer.valueOf(res.getIdentifier(iconupon, "drawable", this.packageName)));
        }
    }

    public float getScale() {
        return this.scale;
    }

    public void setScale(float scale) {
        if (scale != 0.0f) {
            this.scale = scale;
        }
    }

    public String toString() {
        return "IconMaskItem{, packageName='" + this.packageName + '\'' + ", iconback=" + this.iconback + ", iconmask=" + this.iconmask + ", iconupon=" + this.iconupon + ", scale=" + this.scale + '}';
    }

    public static int generateRandomBetween(int min, int max) {
        return new Random().nextInt(max - min) + min;
    }
}
