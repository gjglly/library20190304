package com.zhku.zcj.library.utils;

public class StringUtils {

    public static String[] splitString(String str, String ident) {
        try {
            if (str==null) {
                return null;
            } else {
                String[] file = new String[countMatches(str, ident) + 1];

                for(int i = 0; i < file.length; ++i) {
                    int j = str.indexOf(ident);
                    if (j == -1) {
                        file[i] = str;
                    } else {
                        file[i] = str.substring(0, j);
                        str = subString(str, j + ident.length(), str.length());
                    }
                }

                return file;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public static String[] splitString(String str, int length) {
        if (str == null) {
            return null;
        } else {
            int max = str.length();
            String[] strArray = new String[(max + length - 1) / length];

            for(int i = 0; i < strArray.length; ++i) {
                int pointBegin = i * length;
                int pointEnd = pointBegin + length;
                if (pointEnd > max) {
                    pointEnd = max;
                }

                strArray[i] = str.substring(pointBegin, pointEnd);
            }

            return strArray;
        }
    }

    public static int countMatches(String str, String sub) {
        if (str == null) {
            return 0;
        } else {
            int count = 0;

            for(int idx = 0; (idx = str.indexOf(sub, idx)) != -1; idx += sub.length()) {
                ++count;
            }

            return count;
        }
    }

    public static String subString(String str, int Start, int End) throws Exception {
        if (Start >= 0 && End <= str.length()) {
            char[] chrArry = str.toCharArray();
            char[] tempArry = new char[End - Start];
            int n = 0;

            for(int i = Start; i < End; ++i) {
                tempArry[n] = chrArry[i];
                ++n;
            }

            return new String(tempArry);
        } else {
            throw new Exception("错误：输入的长度在字符串中不被认可");
        }
    }
}
