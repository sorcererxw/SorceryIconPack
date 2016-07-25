package com.sorcerer.sorcery.iconpack.xposed.theme;

import android.content.res.Resources;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class Util {
    public static String TAG = "Util";

    public static ArrayList<IconReplacementItem> ParseIconReplacements(String packageName,
            Resources r, XmlPullParser xrp) throws XmlPullParserException, IOException {
        ArrayList<IconReplacementItem> iconReplacementItemArrayList = new ArrayList();
        xrp.next();
        int eventType = xrp.getEventType();
        while (eventType != 1) {
            if (eventType == 2 && xrp.getName().equalsIgnoreCase("item")) {
                String component = xrp.getAttributeValue(null, "component");
                String drawableName = xrp.getAttributeValue(null, "drawable");
                if (component == null || drawableName == null) {
                    eventType = xrp.next();
                } else {
                    int resId = r.getIdentifier(drawableName, "drawable", packageName);
                    if (resId == 0) {
                        eventType = xrp.next();
                    } else {
                        IconReplacementItem irt =
                                new IconReplacementItem(component, resId, r.getResourceName(resId));
                        if (irt.getPackageName() == null) {
                            eventType = xrp.next();
                        } else {
                            iconReplacementItemArrayList.add(irt);
                        }
                    }
                }
            }
            eventType = xrp.next();
        }
        return iconReplacementItemArrayList;
    }

    public static IconMaskItem ParseIconMask(String packageName, Resources r, XmlPullParser xrp)
            throws XmlPullParserException, IOException {
        IconMaskItem iconMaskItem = new IconMaskItem(packageName);
        xrp.next();
        int eventType = xrp.getEventType();
        while (eventType != 1) {
            int i;
            if (eventType == 2 && xrp.getName().equalsIgnoreCase("iconback")) {
                for (i = 0; i < xrp.getAttributeCount(); i++) {
                    if (xrp.getAttributeName(i).contains("img")) {
                        iconMaskItem.addIconback(xrp.getAttributeValue(i), r);
                    }
                }
            } else if (eventType == 2 && xrp.getName().equalsIgnoreCase("iconmask")) {
                for (i = 0; i < xrp.getAttributeCount(); i++) {
                    if (xrp.getAttributeName(i).contains("img")) {
                        iconMaskItem.setIconmask(xrp.getAttributeValue(i), r);
                    }
                }
            } else if (eventType == 2 && xrp.getName().equalsIgnoreCase("iconupon")) {
                for (i = 0; i < xrp.getAttributeCount(); i++) {
                    if (xrp.getAttributeName(i).contains("img")) {
                        iconMaskItem.setIconupon(xrp.getAttributeValue(i), r);
                    }
                }
            } else if (eventType == 2 && xrp.getName().equalsIgnoreCase("scale")) {
                iconMaskItem.setScale(Float.parseFloat(xrp.getAttributeValue(null, "factor")));
            }
            eventType = xrp.next();
        }
        return iconMaskItem;
    }
}
