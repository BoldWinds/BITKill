package cn.edu.bit.BITKill.util;

import java.util.HashMap;
import java.util.List;

public class PrintHelper {

    public static String list2String(List list){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('[');
        for (Object s : list){
            stringBuilder.append(s.toString());
            stringBuilder.append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public static String map2String(HashMap map){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        for (Object s : map.keySet()){
            stringBuilder.append('\"');
            stringBuilder.append(s.toString());
            stringBuilder.append("\" : \"");
            stringBuilder.append(map.get(s).toString());
            stringBuilder.append("\", ");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
