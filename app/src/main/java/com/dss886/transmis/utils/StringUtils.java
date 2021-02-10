package com.dss886.transmis.utils;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by duansishu on 2018/1/28.
 */

public class StringUtils {

    public static String listToString(List<String> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String string : list) {
            sb.append(string).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @NonNull
    public static List<String> parseToList(String input) {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(input)) {
            return list;
        }
        String[] array = input.split(",");
        list.addAll(Arrays.asList(array));
        return list;
    }
}
