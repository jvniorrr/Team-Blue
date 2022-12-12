package com.callservice.utils;

import java.util.regex.*;
public class ValidatorHelper {

    public static boolean validFilter(String filter) {
        return (filter.equalsIgnoreCase("available") || filter.equalsIgnoreCase("busy")
                || filter.equalsIgnoreCase("logged-out") || filter.equalsIgnoreCase("loggedout")
                || filter.equalsIgnoreCase("preview")
                || filter.equalsIgnoreCase("after"));
    }

    public static String statusVerify(String status) {
        String ret = "available"; // default value if we can not parse the incoming status

        // regex for statuses
        Pattern availablePattern = Pattern.compile("^(?<!no|un|not|in)[-_ ]?(?:available|online|active|ready)[-_ ]?", Pattern.CASE_INSENSITIVE);
        Pattern busyPattern = Pattern.compile("^(?<!no|un|not|in)[-_ ]?(?:busy)[-_ ]?", Pattern.CASE_INSENSITIVE);
        Pattern loggedoutPattern = Pattern.compile("(logged|log)[-\\s_]?out", Pattern.CASE_INSENSITIVE);
        Pattern previewPattern = Pattern.compile("preview", Pattern.CASE_INSENSITIVE);
        Pattern afterPattern = Pattern.compile("after", Pattern.CASE_INSENSITIVE);

        if (busyPattern.matcher(status).matches()) {
            ret = "busy";
        } else if (loggedoutPattern.matcher(status).matches()) {
            ret = "loggedout";
        } else if (previewPattern.matcher(status).matches()) {
            ret = "preview";
        } else if (afterPattern.matcher(status).matches() || !(availablePattern.matcher(status).matches())) {
            ret = "after";
        }
        return ret;
    }
}
