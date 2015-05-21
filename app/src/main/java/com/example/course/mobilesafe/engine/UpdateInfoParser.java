package com.example.course.mobilesafe.engine;

import android.util.Xml;

import com.example.course.mobilesafe.domain.UpdateInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Parse XML data
 *
 */
public class UpdateInfoParser {
    /**
     * @param is xml input stream
     * @return updateinfo
     * @throws XmlPullParserException
     * @throws IOException
     */

    public static UpdateInfo getUpdatedInfo(InputStream is) throws XmlPullParserException,
            IOException{
        // Create a pull parser
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        // Create UpdateInfo object
        UpdateInfo info = new UpdateInfo();
        int type = parser.getEventType();
        while (type != XmlPullParser.END_DOCUMENT) {
            if (type == XmlPullParser.START_TAG) {
                if ("version".equals(parser.getName())) {
                    String version = parser.nextText();
                    info.setVersion(version);
                }else if ("description".equals(parser.getName())) {
                    String description = parser.nextText();
                    info.setDescription(description);
                } else if ("apkurl".equals(parser.getName())) {
                    String apkurl = parser.nextText();
                    info.setApkurl(apkurl);
                }
            }
            type = parser.next();
        }
        return  info;

    }
}
