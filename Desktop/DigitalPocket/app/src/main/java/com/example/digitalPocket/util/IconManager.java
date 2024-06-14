package com.example.digitalPocket.util;

import android.content.Context;
import androidx.annotation.DrawableRes;

import com.example.digitalPocket.R;

import java.util.HashMap;
import java.util.Map;

public class IconManager {

    private Map<String, Integer> iconMap;

    public IconManager() {
        iconMap = new HashMap<>();
        initializeIconMap();
    }

    private void initializeIconMap() {
        iconMap.put("ETH", R.drawable.ic_eth);
        iconMap.put("USDT", R.drawable.ic_usdt);
        iconMap.put("Shib", R.drawable.ic_shib_logo);
    }

    public @DrawableRes Integer getIconResId(String iconKey) {
        return iconMap.get(iconKey);
    }

    public void addIcon(String key, @DrawableRes int resId) {
        iconMap.put(key, resId);
    }
}
