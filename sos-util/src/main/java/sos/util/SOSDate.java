package sos.util;

import java.io.File;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/** @author Ghassan Beydoun
 * @author Andreas P�schel
 * @author Titus Meyer */
public class SOSDate {

    private static String outputDateTimeFormat = new String("MM/dd/yy HH:mm:ss");
    private static boolean lenient = false;
    public static String dateFormat = new String("yyyy-MM-dd");
    public static String dateTimeFormat = new String("yyyy-MM-dd HH:mm:ss");
    public static final int SHORT = DateFormat.SHORT;
    public static final int MEDIUM = DateFormat.MEDIUM;
    public static final int LONG = DateFormat.LONG;
    public static final int FULL = DateFormat.FULL;
    public static int dateStyle = DateFormat.SHORT;
    public static int timeStyle = DateFormat.SHORT;
    public static Locale locale = Locale.UK;

    public void setDateFormat(String dateFormat) {
        SOSDate.dateFormat = dateFormat;
    }

    public static String getDateFormat() {
        return SOSDate.dateFormat;
    }

    public static void setDateTimeFormat(String dateTimeFormat) {
        SOSDate.dateTimeFormat = dateTimeFormat;
    }

    public static String getDateTimeFormat() {
        return SOSDate.dateTimeFormat;
    }

    public static Date getCurrentDate() throws Exception {
        return SOSDate.getDate();
    }

    public static String getCurrentDateAsString() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    public static String getCurrentDateAsString(String dateFormat) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    public static Date getCurrentTime() throws Exception {
        return SOSDate.getTime();
    }

    public static String getCurrentTimeAsString() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateTimeFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    public static String getCurrentTimeAsString(String dateTimeFormat) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        formatter.setLenient(lenient);
        Calendar now = Calendar.getInstance();
        return formatter.format(now.getTime());
    }

    public static Date getDate() throws Exception {
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(SOSDate.dateFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw new Exception("invalid date format string: " + e.toString());
        }
        try {
            Calendar now = Calendar.getInstance();
            return now.getTime();
        } catch (Exception e) {
            throw new Exception("illegal date value: " + e.toString());
        }
    }

    public static String getDateAsString() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateFormat);
        formatter.setLenient(lenient);
        return formatter.format(SOSDate.getDate());
    }

    public static Date getDate(String dateStr) throws Exception {
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(SOSDate.dateFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw new Exception("invalid date format string: " + e.toString());
        }
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            throw new Exception("illegal date value: " + e.toString());
        }
    }

    public static String getDateAsString(Date date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    public static Date getDate(String dateStr, String dateFormat) throws Exception {
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(dateFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw new Exception("invalid date format string: " + e.toString());
        }
        try {
            return formatter.parse(dateStr);
        } catch (Exception e) {
            throw new Exception("illegal date string: " + e.toString());
        }
    }

    public static String getDateAsString(Date date, String dateFormat) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    public static Date getTime() throws Exception {
        try {
            Calendar now = Calendar.getInstance();
            return now.getTime();
        } catch (Exception e) {
            throw new Exception("illegal date value: " + e.toString());
        }
    }

    public static String getTimeAsString() throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(SOSDate.getTime());
    }

    public static Date getTime(String dateTimeStr) throws Exception {
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(SOSDate.dateTimeFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw new Exception("invalid date format string: " + e.toString());
        }
        try {
            return formatter.parse(dateTimeStr);
        } catch (Exception e) {
            throw new Exception("illegal date value: " + e.toString());
        }
    }

    public static String getTimeAsString(Date date) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(SOSDate.dateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    public static Date getTime(String dateTimeStr, String dateTimeFormat) throws Exception {
        SimpleDateFormat formatter;
        try {
            formatter = new SimpleDateFormat(dateTimeFormat);
            formatter.setLenient(lenient);
        } catch (Exception e) {
            throw new Exception("invalid date format string: " + e.toString());
        }
        try {
            return formatter.parse(dateTimeStr);
        } catch (Exception e) {
            throw new Exception("illegal date value: " + e.toString());
        }
    }

    public static String getTimeAsString(Date date, String dateTimeFormat) throws Exception {
        SimpleDateFormat formatter = new SimpleDateFormat(dateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    public static boolean isValidDate(String text, int dateStyle, Locale locale) {
        try {
            getDate(text, dateStyle, locale);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidTime(String text, int timeStyle, Locale locale) {
        try {
            getTime(text, timeStyle, locale);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDateTime(String text, int dateStyle, int timeStyle, Locale locale) {
        try {
            getDateTime(text, dateStyle, timeStyle, locale);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDateAsString(Date date, int dateStyle, Locale locale) {
        DateFormat formatter = DateFormat.getDateInstance(dateStyle, locale);
        return formatter.format(date);
    }

    public static String getTimeAsString(Date date, int timeStyle, Locale locale) {
        DateFormat formatter = DateFormat.getTimeInstance(timeStyle, locale);
        return formatter.format(date);
    }

    public static String getDateTimeAsString(Date date, int dateStyle, int timeStyle, Locale locale) {
        DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        return formatter.format(date);
    }

    public static String getDateTimeAsString(String datestr) throws Exception {
        return getDateTimeAsString(datestr, null);
    }

    public static String getDateTimeAsString(String datestr, String outputDateTimeFormat) throws Exception {
        Date date = null;
        if ("%now".equals(datestr)) {
            date = new Date();
        } else {
            date = SOSDate.getTime(datestr);
        }
        if (outputDateTimeFormat == null || outputDateTimeFormat.isEmpty()) {
            outputDateTimeFormat = SOSDate.getOutputDateTimeFormat();
        }
        DateFormat formatter = new SimpleDateFormat(outputDateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    public static String getDateTimeAsString(Date date, String outputDateTimeFormat) throws Exception {
        if (outputDateTimeFormat == null || outputDateTimeFormat.isEmpty()) {
            outputDateTimeFormat = SOSDate.getOutputDateTimeFormat();
        }
        DateFormat formatter = new SimpleDateFormat(outputDateTimeFormat);
        formatter.setLenient(lenient);
        return formatter.format(date);
    }

    public static Date getDate(String text, int dateStyle, Locale locale) throws ParseException {
        DateFormat formatter = DateFormat.getDateInstance(dateStyle, locale);
        return formatter.parse(text);
    }

    public static Date getTime(String text, int timeStyle, Locale locale) throws ParseException {
        DateFormat formatter = DateFormat.getTimeInstance(timeStyle, locale);
        return formatter.parse(text);
    }

    public static Date getDateTime(String text, int dateStyle, int timeStyle, Locale locale) throws ParseException {
        DateFormat formatter = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        return formatter.parse(text);
    }

    public static String getDatePattern(int dateStyle, Locale locale) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance(dateStyle, locale);
        formatter.setLenient(lenient);
        return formatter.toLocalizedPattern();
    }

    public static String getTimePattern(int timeStyle, Locale locale) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getTimeInstance(timeStyle, locale);
        formatter.setLenient(lenient);
        return formatter.toLocalizedPattern();
    }

    public static String getDateTimePattern(int dateStyle, int timeStyle, Locale locale) {
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale);
        formatter.setLenient(lenient);
        return formatter.toLocalizedPattern();
    }

    public static String getOutputDateTimeFormat() {
        return outputDateTimeFormat;
    }

    public static void setOutputDateTimeFormat(String outputDateTimeFormat) {
        SOSDate.outputDateTimeFormat = outputDateTimeFormat;
    }

    public static String getLocaleDateAsString(String datestr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(SOSDate.dateFormat);
        sdf.setLenient(lenient);
        Date date = sdf.parse(datestr);
        DateFormat formatter = DateFormat.getDateInstance(SOSDate.dateStyle, SOSDate.locale);
        return formatter.format(date);
    }

    public static String getLocaleDateTimeAsString(String datestr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(SOSDate.dateTimeFormat);
        sdf.setLenient(lenient);
        Date date = sdf.parse(datestr);
        DateFormat formatter = DateFormat.getDateTimeInstance(SOSDate.dateStyle, SOSDate.timeStyle, SOSDate.locale);
        return formatter.format(date);
    }

    public static String getLocaleDateAsString(Date date) throws Exception {
        DateFormat formatter = DateFormat.getDateInstance(SOSDate.dateStyle, SOSDate.locale);
        return formatter.format(date);
    }

    public static String getLocaleDateTimeAsString(Date date) throws Exception {
        DateFormat formatter = DateFormat.getDateTimeInstance(SOSDate.dateStyle, SOSDate.timeStyle, SOSDate.locale);
        return formatter.format(date);
    }

    public static String getISODateTimeAsString(GregorianCalendar date) throws Exception {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        isoFormat.setLenient(lenient);
        return isoFormat.format(date.getTime());
    }

    public static String getISODateAsString(GregorianCalendar date) throws Exception {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
        isoFormat.setLenient(lenient);
        return isoFormat.format(date.getTime());
    }

    public static boolean isLenient() {
        return lenient;
    }

    public static void setLenient(boolean lenient) {
        SOSDate.lenient = lenient;
    }

    public static Date incrementDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    public static boolean isWeekEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    public static Date getNextWorkingDay(Date date) {
        Date day = date;
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return day;
    }

    public static String getNextWorkingDayAsString(Date date) throws Exception {
        Date day = date;
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return SOSDate.getDateAsString(day);
    }

    public static Date getNextWorkingDay(String dateStr) throws Exception {
        Date day = SOSDate.getDate(dateStr);
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return day;
    }

    public static String getNextWorkingDayAsString(String dateStr) throws Exception {
        Date day = SOSDate.getDate(dateStr);
        day = incrementDay(day);
        while (isWeekEnd(day)) {
            day = incrementDay(day);
        }
        return SOSDate.getDateAsString(day);
    }

    public static String getNextWorkingDayAsString(String dateStr, File xmlFile) throws Exception {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(xmlFile);
        return getNextWorkingDayAsString(dateStr, doc);
    }

    public static Date getNextWorkingDay(Date date, File xmlFile) throws Exception {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(xmlFile);
        return getNextWorkingDay(date, doc);
    }

    public static Date getNextWorkingDay(Date date, Document holidays) throws Exception {
        Date sortedDate = null;
        long dateDiff = -1;
        Vector dateList = new Vector();
        SOSDateRecord dateRecord = null;
        Date nextWorkingDay = getNextWorkingDay(date);
        Element docEle = holidays.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName("holiday");
        if (nl != null && nl.getLength() > 0) {
            for (int i = 0; i < nl.getLength(); i++) {
                Element el = (Element) nl.item(i);
                if (el.getAttribute("date") != null) {
                    dateRecord = new SOSDateRecord();
                    dateRecord.setDate(date);
                    dateList.add(dateRecord.getDate());
                }
            }
            java.util.Collections.sort(dateList);
            for (int i = 0; i < dateList.size(); i++) {
                if (dateList.get(i) != null) {
                    sortedDate = (Date) dateList.get(i);
                    if (sortedDate.before(date)) {
                        continue;
                    }
                    dateDiff = (nextWorkingDay.getTime() - sortedDate.getTime()) / (60 * 60 * 1000);
                    if (dateDiff < 0) {
                        continue;
                    }
                    if (nextWorkingDay.getTime() == sortedDate.getTime() || (dateDiff < 24)) {
                        nextWorkingDay = getNextWorkingDay(nextWorkingDay);
                    }
                }
            }
        }
        return nextWorkingDay;
    }

    public static Date getNextWorkingDay(String dateStr, Document holidays) throws Exception {
        return getNextWorkingDay(SOSDate.getDate(dateStr), holidays);
    }

    public static String getNextWorkingDayAsString(Date date, Document holidays) throws Exception {
        return SOSDate.getDateAsString(getNextWorkingDay(date, holidays));
    }

    public static String getNextWorkingDayAsString(String dateStr, Document holidays) throws Exception {
        return SOSDate.getDateAsString(getNextWorkingDay(SOSDate.getDate(dateStr), holidays));
    }

    public static Date getNextWorkingDay(Date date, String holidays) throws Exception {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(new InputSource(new StringReader(holidays)));
        return getNextWorkingDay(date, doc);
    }

    public static Date getNextWorkingDay(String date, String holidays) throws Exception {
        return getNextWorkingDay(SOSDate.getDate(date), holidays);
    }

    public static String getNextWorkingDayAsString(Date date, String holidays) throws Exception {
        return SOSDate.getDateAsString(getNextWorkingDay(date, holidays));
    }

    public static String getNextWorkingDayAsString(String dateStr, String holidays) throws Exception {
        return getNextWorkingDayAsString(SOSDate.getDate(dateStr), holidays);
    }

    public static void main(String[] args) {
        try {
            System.out.println(SOSDate.getDateAsString(SOSDate.getDate("01.19.2008", "dd.MM.yyyy"), "yyyy-MM-dd"));
        } catch (Exception e) {
            System.err.println("..error: " + e.getMessage());
        }
    }

}