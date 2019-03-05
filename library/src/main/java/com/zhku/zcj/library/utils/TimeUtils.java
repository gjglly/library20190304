package com.zhku.zcj.library.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.zhku.zcj.library.utils.StringUtils;

public class TimeUtils {

    static Pattern datePattern = Pattern.compile("\\b(\\d{1,4})[-\\/](\\d{1,4})[-\\/](\\d{1,4})([\\s+\\.](\\d{1,2})[:\\.]\\s*(\\d{1,2})[:\\.]\\s*(\\d{1,2})\\b)*");

    public static java.util.Date cvStUtildate(String st) {
        if (st==null) {
            return null;
        } else {
            Matcher matcher = datePattern.matcher(st);
            if (matcher.find()) {
                int groupSize = matcher.groupCount();
                StringBuilder sb = new StringBuilder();
                if (matcher.group(1).length() == 4) {
                    sb.append(matcher.group(1));
                    sb.append("/");
                    sb.append(matcher.group(2));
                    sb.append("/");
                    sb.append(matcher.group(3));
                } else {
                    sb.append(matcher.group(3));
                    sb.append("/");
                    sb.append(matcher.group(1));
                    sb.append("/");
                    sb.append(matcher.group(2));
                }

                sb.append(" ");
                if (groupSize > 3) {
                    sb.append(matcher.group(5) == null ? "0" : matcher.group(5));
                    sb.append(":");
                    sb.append(matcher.group(6) == null ? "0" : matcher.group(6));
                    sb.append(":");
                    sb.append(matcher.group(7) == null ? "0" : matcher.group(7));
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d H:m:s");

                try {
                    return sdf.parse(sb.toString());
                } catch (ParseException var6) {
                    var6.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public static Timestamp cvStTims(String st) {
        if (st==null) {
            return null;
        } else {
            try {
                int nam = 0;
                Timestamp rd = null;
                if (st != null) {
                    if (!(st = st.trim()).equals("")) {
                        if (st.length() <= 10) {
                            return new Timestamp(cvStDate(st).getTime());
                        }

                        String[] timeSt = StringUtils.splitString(st, " ");
                        String[] d = StringUtils.splitString(timeSt[0], timeSt[0].substring(4, 5));
                        if (timeSt[1].length() > 8) {
                            nam = getInt(timeSt[1].substring(timeSt[1].indexOf(".") + 1, timeSt[1].length()));
                            timeSt[1] = timeSt[1].substring(0, timeSt[1].indexOf("."));
                        }

                        String[] s = StringUtils.splitString(timeSt[1], timeSt[1].substring(2, 3));
                        return new Timestamp(Integer.parseInt(d[0]) - 1900, Integer.parseInt(d[1]) - 1, Integer.parseInt(d[2]), Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), nam);
                    }

                    rd = null;
                }

                return (Timestamp)rd;
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }
        }
    }

    public static Date cvStDate(String st) {
        return st==null ? null : new Date(cvStUtildate(st).getTime());
    }

    public static int getInt(String str) {
        if (str==null) {
            return 0;
        } else {
            String s = "0";
            int i;
            if ((i = str.indexOf(".")) > -1) {
                s = str.substring(i + 1, i + 2);
                str = str.substring(0, i);
            }

            return new Integer(s) >= 5 ? new Integer(str) + 1 : new Integer(str);
        }
    }
}
