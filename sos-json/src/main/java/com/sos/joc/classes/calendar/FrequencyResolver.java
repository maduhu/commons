package com.sos.joc.classes.calendar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sos.exception.SOSInvalidDataException;
import com.sos.exception.SOSMissingDataException;
import com.sos.joc.model.calendar.CalendarDatesFilter;
import com.sos.joc.model.calendar.Dates;
import com.sos.joc.model.calendar.Frequencies;
import com.sos.joc.model.calendar.Holidays;
import com.sos.joc.model.calendar.MonthDays;
import com.sos.joc.model.calendar.Months;
import com.sos.joc.model.calendar.Repetition;
import com.sos.joc.model.calendar.RepetitionText;
import com.sos.joc.model.calendar.WeekDays;
import com.sos.joc.model.calendar.WeeklyDay;

public class FrequencyResolver {

    private SortedSet<String> dates = new TreeSet<String>();
    private SortedSet<String> withExcludes = new TreeSet<String>();
    private Calendar dateFrom = null;
    private Calendar dateTo = null;
    private Frequencies includes = null;
    private Frequencies excludes = null;
    private DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public FrequencyResolver() {
    }

    public SortedSet<String> getDates() {
        return dates;
    }

    public SortedSet<String> getWithExcludes() {
        return withExcludes;
    }

    public Dates resolve(CalendarDatesFilter calendarFilter) throws SOSMissingDataException, SOSInvalidDataException {
        if (calendarFilter != null) {
            return resolve(calendarFilter.getCalendar(), calendarFilter.getDateFrom(), calendarFilter.getDateTo());
        } else {
            Dates d = new Dates();
            d.setDates(new ArrayList<String>(dates));
            d.setWithExcludes(new ArrayList<String>(withExcludes));
            d.setDeliveryDate(Date.from(Instant.now()));
            return d;
        }
    }

    public Dates resolve(String calendarJson, String from, String to) throws SOSMissingDataException, SOSInvalidDataException, JsonParseException,
            JsonMappingException, IOException {
        return resolve(new ObjectMapper().readValue(calendarJson, com.sos.joc.model.calendar.Calendar.class), from, to);
    }

    public Dates resolve(com.sos.joc.model.calendar.Calendar calendar, String from, String to) throws SOSMissingDataException,
            SOSInvalidDataException {
        init(calendar, from, to);
        addDates();
        addHolidays();
        addWeekDays();
        addMonthDays();
        addUltimos();
        addMonths();
        addRepetitions();
        removeDates();
        removeWeekDays();
        removeMonthDays();
        removeUltimos();
        removeMonths();
        removeHolidays();
        removeRepetitions();
        Dates d = new Dates();
        d.setDates(new ArrayList<String>(dates));
        d.setWithExcludes(new ArrayList<String>(withExcludes));
        d.setDeliveryDate(Date.from(Instant.now()));
        return d;
    }

    public Dates resolveFromToday(String calendarJson) throws SOSMissingDataException, SOSInvalidDataException, JsonParseException,
            JsonMappingException, IOException {
        return resolve(calendarJson, df.format(Date.from(Instant.now())), null);
    }

    public Dates resolveFromToday(com.sos.joc.model.calendar.Calendar calendar) throws SOSMissingDataException, SOSInvalidDataException {
        return resolve(calendar, df.format(Date.from(Instant.now())), null);
    }

    public void init(CalendarDatesFilter calendarFilter) throws SOSMissingDataException, SOSInvalidDataException {
        if (calendarFilter != null) {
            init(calendarFilter.getCalendar(), calendarFilter.getDateFrom(), calendarFilter.getDateTo());
        }
    }

    public void init(com.sos.joc.model.calendar.Calendar calendar, String from, String to) throws SOSMissingDataException, SOSInvalidDataException {
        if (calendar != null) {
            setDateFrom(from, calendar.getFrom());
            setDateTo(to, calendar.getTo());
            if (this.dateFrom.after(this.dateTo)) {
                throw new SOSInvalidDataException("'dateFrom' must be an older date or equals than 'dateTo'.");
            }
            this.includes = calendar.getIncludes();
            this.excludes = calendar.getExcludes();
        }
    }

    public void setDateFrom(String dateFrom, String calendarFrom) throws SOSMissingDataException, SOSInvalidDataException {

        if ((dateFrom == null || dateFrom.isEmpty()) && (calendarFrom == null || calendarFrom.isEmpty())) {
            // use today at 00:00:00.000 as default
            this.dateFrom = Calendar.getInstance();
            this.dateFrom.setTime(Date.from(Instant.now()));
            this.dateFrom.set(Calendar.HOUR_OF_DAY, 0);
            this.dateFrom.set(Calendar.MINUTE, 0);
            this.dateFrom.set(Calendar.SECOND, 0);
            this.dateFrom.set(Calendar.MILLISECOND, 0);
        } else {

            Calendar calFrom = getCalendarFromString(calendarFrom, "calendar field 'from' must have the format YYYY-MM-DD.");
            Calendar dFrom = getCalendarFromString(dateFrom, "'dateFrom' parameter must have the format YYYY-MM-DD.");

            if (calFrom == null) {
                this.dateFrom = dFrom;
            } else if (dFrom == null) {
                this.dateFrom = calFrom;
            } else if (calFrom.before(dFrom)) {
                this.dateFrom = dFrom;
            } else {
                this.dateFrom = calFrom;
            }
        }
    }

    public void setDateTo(String dateTo, String calendarTo) throws SOSMissingDataException, SOSInvalidDataException {

        if ((dateTo == null || dateTo.isEmpty()) && (calendarTo == null || calendarTo.isEmpty())) {
            throw new SOSMissingDataException("'dateTo' parameter and calendar field 'to' are undefined.");
        } else {

            Calendar calTo = getCalendarFromString(calendarTo, "calendar field 'to' must have the format YYYY-MM-DD.");
            Calendar dTo = getCalendarFromString(dateTo, "'dateTo' parameter must have the format YYYY-MM-DD.");

            if (calTo == null) {
                this.dateTo = dTo;
            } else if (dTo == null) {
                this.dateTo = calTo;
            } else if (calTo.after(dTo)) {
                this.dateTo = dTo;
            } else {
                this.dateTo = calTo;
            }
        }
    }

    public void setDateTo_(String dateTo, String calendarTo) throws SOSMissingDataException, SOSInvalidDataException {
        if (dateTo == null || dateTo.isEmpty()) {
            throw new SOSMissingDataException("'dateTo' parameter is undefined.");
        }
        if (!dateTo.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new SOSInvalidDataException("'dateTo' parameter must have the format YYYY-MM-DD.");
        }
        this.dateTo = Calendar.getInstance();
        this.dateTo.setTime(Date.from(Instant.parse(dateTo + "T00:00:00Z")));
    }

    public void addDates() throws SOSInvalidDataException {
        if (includes != null) {
            addDates(includes.getDates());
        }
    }

    public void removeDates() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeDates(excludes.getDates());
        }
    }

    public void addHolidays() throws SOSInvalidDataException {
        if (includes != null) {
            addHolidays(includes.getHolidays());
        }
    }

    public void removeHolidays() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeHolidays(excludes.getHolidays());
        }
    }

    public void addWeekDays() throws SOSInvalidDataException {
        if (includes != null) {
            addWeekDays(includes.getWeekdays());
        }
    }

    public void removeWeekDays() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeWeekDays(excludes.getWeekdays());
        }
    }

    public void addMonthDays() throws SOSInvalidDataException {
        if (includes != null) {
            addMonthDays(includes.getMonthdays());
        }
    }

    public void removeMonthDays() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeMonthDays(excludes.getMonthdays());
        }
    }

    public void addUltimos() throws SOSInvalidDataException {
        if (includes != null) {
            addUltimos(includes.getUltimos());
        }
    }

    public void removeUltimos() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeUltimos(excludes.getUltimos());
        }
    }

    public void addRepetitions() throws SOSInvalidDataException {
        if (includes != null) {
            addRepetitions(includes.getRepetitions());
        }
    }

    public void removeRepetitions() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeRepetitions(excludes.getRepetitions());
        }
    }

    public void addMonths() throws SOSInvalidDataException {
        if (includes != null) {
            addMonths(includes.getMonths());
        }
    }

    public void removeMonths() throws SOSInvalidDataException {
        if (excludes != null && dates.size() > 0) {
            removeMonths(excludes.getMonths());
        }
    }

    private Calendar getCalendarFromString(String cal, String msg) throws SOSInvalidDataException {
        Calendar calendar = null;
        if (cal != null && !cal.isEmpty()) {
            if (!cal.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                throw new SOSInvalidDataException(msg);
            }
            calendar = Calendar.getInstance();
            calendar.setTime(Date.from(Instant.parse(cal + "T00:00:00Z")));
        }
        return calendar;
    }

    private void addDates(List<String> list) throws SOSInvalidDataException {
        addAll(resolveDates(list));
    }

    private void removeDates(List<String> list) throws SOSInvalidDataException {
        removeAll(resolveDates(list));
    }

    private void addHolidays(List<Holidays> holidays) throws SOSInvalidDataException {
        if (holidays != null) {
            for (Holidays holiday : holidays) {
                addDates(holiday.getDates());
            }
        }
    }

    private void removeHolidays(List<Holidays> holidays) throws SOSInvalidDataException {
        if (holidays != null) {
            for (Holidays holiday : holidays) {
                removeDates(holiday.getDates());
            }
        }
    }

    private void addWeekDays(List<WeekDays> weekDays) throws SOSInvalidDataException {
        addWeekDays(weekDays, dateFrom, dateTo);
    }

    private void addWeekDays(List<WeekDays> weekDays, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (weekDays != null) {
            for (WeekDays weekDay : weekDays) {
                addAll(resolveWeekDays(weekDay.getDays(), getFrom(weekDay.getFrom(), from), getTo(weekDay.getTo(), to)));
            }
        }
    }

    private void removeWeekDays(List<WeekDays> weekDays) throws SOSInvalidDataException {
        removeWeekDays(weekDays, dateFrom, dateTo);
    }

    private void removeWeekDays(List<WeekDays> weekDays, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (weekDays != null) {
            for (WeekDays weekDay : weekDays) {
                removeAll(resolveWeekDays(weekDay.getDays(), getFrom(weekDay.getFrom(), from), getTo(weekDay.getTo(), to)));
            }
        }
    }

    private void addMonthDays(List<MonthDays> monthDays) throws SOSInvalidDataException {
        addMonthDays(monthDays, dateFrom, dateTo);
    }

    private void addMonthDays(List<MonthDays> monthDays, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (monthDays != null) {
            for (MonthDays monthDay : monthDays) {
                addAll(resolveMonthDays(monthDay.getDays(), monthDay.getWorkingDays(), monthDay.getWeeklyDays(), getFrom(monthDay.getFrom(), from),
                        getTo(monthDay.getTo(), to)));
            }
        }
    }

    private void removeMonthDays(List<MonthDays> monthDays) throws SOSInvalidDataException {
        removeMonthDays(monthDays, dateFrom, dateTo);
    }

    private void removeMonthDays(List<MonthDays> monthDays, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (monthDays != null) {
            for (MonthDays monthDay : monthDays) {
                removeAll(resolveMonthDays(monthDay.getDays(), monthDay.getWorkingDays(), monthDay.getWeeklyDays(), getFrom(monthDay.getFrom(), from),
                        getTo(monthDay.getTo(), to)));
            }
        }
    }

    private void addUltimos(List<MonthDays> monthDays) throws SOSInvalidDataException {
        addUltimos(monthDays, dateFrom, dateTo);
    }

    private void addUltimos(List<MonthDays> ultimos, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (ultimos != null) {
            for (MonthDays ultimo : ultimos) {
                addAll(resolveUltimos(ultimo.getDays(), ultimo.getWorkingDays(), ultimo.getWeeklyDays(), getFrom(ultimo.getFrom(), from), getTo(ultimo
                        .getTo(), to)));
            }
        }
    }

    private void removeUltimos(List<MonthDays> monthDays) throws SOSInvalidDataException {
        removeUltimos(monthDays, dateFrom, dateTo);
    }

    private void removeUltimos(List<MonthDays> ultimos, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (ultimos != null) {
            for (MonthDays ultimo : ultimos) {
                removeAll(resolveUltimos(ultimo.getDays(), ultimo.getWorkingDays(), ultimo.getWeeklyDays(), getFrom(ultimo.getFrom(), from), getTo(
                        ultimo.getTo(), to)));
            }
        }
    }

    private void addRepetitions(List<Repetition> repetitions) throws SOSInvalidDataException {
        if (repetitions != null) {
            for (Repetition repetition : repetitions) {
                addAll(resolveRepetitions(repetition.getRepetition(), repetition.getStep(), getFrom(repetition.getFrom()), getTo(repetition
                        .getTo())));
            }
        }
    }

    private void removeRepetitions(List<Repetition> repetitions) throws SOSInvalidDataException {
        if (repetitions != null) {
            for (Repetition repetition : repetitions) {
                removeAll(resolveRepetitions(repetition.getRepetition(), repetition.getStep(), getFrom(repetition.getFrom()), getTo(repetition
                        .getTo())));
            }
        }
    }

    private void addMonths(List<Months> months) throws SOSInvalidDataException {
        if (months != null) {
            Calendar monthStart = Calendar.getInstance();
            Calendar monthEnd = Calendar.getInstance();
            for (Months month : months) {
                if (month.getMonths() != null) {
                    Calendar from = getFrom(month.getFrom());
                    Calendar to = getTo(month.getTo());
                    while (from.compareTo(to) <= 0) {
                        int lastDayOfMonth = from.getActualMaximum(Calendar.DAY_OF_MONTH);
                        if (month.getMonths().contains(from.get(Calendar.MONTH) + 1)) {
                            Calendar monthFrom = getFromPerMonth(monthStart, from);
                            Calendar monthTo = getFromToMonth(monthEnd, from, to, lastDayOfMonth);
                            addWeekDays(month.getWeekdays(), monthFrom, monthTo);
                            addMonthDays(month.getMonthdays(), monthFrom, monthTo);
                            addUltimos(month.getUltimos(), monthFrom, monthTo);

                        }
                        from.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
                        from.add(Calendar.DATE, 1);
                    }
                }
            }
        }
    }

    private void removeMonths(List<Months> months) throws SOSInvalidDataException {
        if (months != null) {
            Calendar monthStart = Calendar.getInstance();
            Calendar monthEnd = Calendar.getInstance();
            for (Months month : months) {
                if (month.getMonths() != null) {
                    Calendar from = getFrom(month.getFrom());
                    Calendar to = getTo(month.getTo());
                    while (from.compareTo(to) <= 0) {
                        int lastDayOfMonth = from.getActualMaximum(Calendar.DAY_OF_MONTH);
                        if (month.getMonths().contains(from.get(Calendar.MONTH) + 1)) {
                            Calendar monthFrom = getFromPerMonth(monthStart, from);
                            Calendar monthTo = getFromToMonth(monthEnd, from, to, lastDayOfMonth);
                            removeWeekDays(month.getWeekdays(), monthFrom, monthTo);
                            removeMonthDays(month.getMonthdays(), monthFrom, monthTo);
                            removeUltimos(month.getUltimos(), monthFrom, monthTo);
                        }
                        from.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
                        from.add(Calendar.DATE, 1);
                    }
                }
            }
        }
    }

    private Calendar getFromPerMonth(Calendar monthStart, Calendar refFrom) throws SOSInvalidDataException {
        monthStart.set(Calendar.YEAR, refFrom.get(Calendar.YEAR));
        monthStart.set(Calendar.MONTH, refFrom.get(Calendar.MONTH));
        monthStart.set(Calendar.DAY_OF_MONTH, 1);
        return getFrom(monthStart, refFrom);
    }

    private Calendar getFromToMonth(Calendar monthEnd, Calendar refFrom, Calendar refTo, int lastDayOfMonth) throws SOSInvalidDataException {
        monthEnd.set(Calendar.YEAR, refFrom.get(Calendar.YEAR));
        monthEnd.set(Calendar.MONTH, refFrom.get(Calendar.MONTH));
        monthEnd.set(Calendar.DAY_OF_MONTH, lastDayOfMonth);
        return getTo(monthEnd, refTo);
    }

    private boolean addAll(Set<String> set) {
        if (set == null) {
            return false;
        }
        return dates.addAll(set);
    }

    private boolean removeAll(Set<String> set) {
        boolean removeAffects = false;
        if (set == null) {
            return false;
        }
        for (String item : set) {
            if (dates.remove(item)) {
                withExcludes.add(item);
                removeAffects = true;
            }
        }
        return removeAffects;
    }

    private Calendar getFrom(String from) throws SOSInvalidDataException {
        return getFrom(from, dateFrom);
    }

    private Calendar getTo(String to) throws SOSInvalidDataException {
        return getTo(to, dateTo);
    }

    private Calendar getFrom(String from, Calendar fromRef) throws SOSInvalidDataException {
        Calendar cal = Calendar.getInstance();
        if (from == null || from.isEmpty()) {
            return (Calendar) fromRef.clone();
        }
        if (!from.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new SOSInvalidDataException("json field 'from' must have the format YYYY-MM-DD.");
        }
        cal.setTime(Date.from(Instant.parse(from + "T00:00:00Z")));
        if (cal.after(fromRef)) {
            return cal;
        }
        return (Calendar) fromRef.clone();
    }

    private Calendar getFrom(Calendar from, Calendar fromRef) throws SOSInvalidDataException {
        if (from == null) {
            return (Calendar) fromRef.clone();
        }
        if (from.after(fromRef)) {
            return from;
        }
        return (Calendar) fromRef.clone();
    }

    private Calendar getTo(String to, Calendar toRef) throws SOSInvalidDataException {
        if (to == null || to.isEmpty()) {
            return (Calendar) toRef.clone();
        }
        if (!to.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            throw new SOSInvalidDataException("json field 'to' must have the format YYYY-MM-DD.");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(Date.from(Instant.parse(to + "T00:00:00Z")));
        if (cal.before(toRef)) {
            return cal;
        }
        return (Calendar) toRef.clone();
    }

    private Calendar getTo(Calendar to, Calendar toRef) throws SOSInvalidDataException {
        if (to == null) {
            return (Calendar) toRef.clone();
        }
        if (to.before(toRef)) {
            return to;
        }
        return (Calendar) toRef.clone();
    }

    private boolean isBetweenFromTo(String date) throws SOSInvalidDataException {
        if (date != null && !date.isEmpty()) {
            if (!date.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                throw new SOSInvalidDataException("dates must have the format YYYY-MM-DD.");
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(Date.from(Instant.parse(date + "T00:00:00Z")));
            if (cal.compareTo(dateTo) <= 0 && cal.compareTo(dateFrom) >= 0) {
                return true;
            }
        }
        return false;
    }

    private Calendar getFirstDayOfMonth(Calendar firstDayOfMonth, Calendar refCal) {
        return getDayOfMonth(firstDayOfMonth, refCal, 1);
    }

    private Calendar getLastDayOfMonth(Calendar lastDayOfMonth, Calendar refCal) {
        return getDayOfMonth(lastDayOfMonth, refCal, refCal.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    private Calendar getDayOfMonth(Calendar firstDayOfMonth, Calendar refCal, int dayOfMonth) {
        firstDayOfMonth.set(Calendar.YEAR, refCal.get(Calendar.YEAR));
        firstDayOfMonth.set(Calendar.MONTH, refCal.get(Calendar.MONTH));
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return firstDayOfMonth;
    }

    private int getWorkingDay(Calendar currentDay, Calendar firstDayOfMonth) {
        // WEEK_OF_MONTH == 0 if first day is SA or SU
        int countWeekEndDays = (currentDay.get(Calendar.WEEK_OF_MONTH) - firstDayOfMonth.get(Calendar.WEEK_OF_MONTH)) * 2;
        if (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            countWeekEndDays--;
        }
        return currentDay.get(Calendar.DAY_OF_MONTH) - countWeekEndDays;
        // int prevSunday = dayOfMonth - dayOfWeek + 1;
        // if (prevSunday <= 0) {
        // return dayOfMonth;
        // }
        // int a = (prevSunday / 7);
        // int b = (prevSunday % 7);
        // if (b > 1) {
        // b = 2;
        // }
        // return dayOfMonth - (a * 2) - b;
    }

    private int getUltimoWorkingDay(Calendar currentDay, Calendar lastDayOfMonth, Calendar firstDayOfMonth) {
        int maxWorkingDaysInMonth = getWorkingDay(lastDayOfMonth, firstDayOfMonth);
        int workingDayInMonth = getWorkingDay(currentDay, firstDayOfMonth);
        return maxWorkingDaysInMonth - workingDayInMonth + 1;
    }

    private int getWeekOfMonthOfWeeklyDay(Calendar currentDay) {
        int dayOfMonth = currentDay.get(Calendar.DAY_OF_MONTH);
        int weekOfMonthOfWeeklyDay = dayOfMonth / 7;
        if (dayOfMonth % 7 > 0) {
            weekOfMonthOfWeeklyDay++;
        }
        return weekOfMonthOfWeeklyDay;
    }

    private int getWeekOfMonthOfUltimoWeeklyDay(Calendar currentDay) {
        int dayOfMonth = currentDay.get(Calendar.DAY_OF_MONTH);
        int maxDaysOfMonth = currentDay.getActualMaximum(Calendar.DAY_OF_MONTH);
        dayOfMonth = maxDaysOfMonth - dayOfMonth + 1;
        int weekOfMonthOfWeeklyDay = dayOfMonth / 7;
        if (dayOfMonth % 7 > 0) {
            weekOfMonthOfWeeklyDay++;
        }
        return weekOfMonthOfWeeklyDay;
    }

    private Set<String> resolveDates(List<String> dates) throws SOSInvalidDataException {
        Set<String> d = new HashSet<String>();
        if (dates != null && !dates.isEmpty()) {
            for (String date : dates) {
                if (isBetweenFromTo(date)) {
                    d.add(date);
                }
            }
        }
        return d;
    }

    private Set<String> resolveWeekDays(List<Integer> days, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (days == null || days.isEmpty()) {
            throw new SOSInvalidDataException("json field 'days' in 'weekdays' is undefined.");
        }
        if (days.contains(7) && !days.contains(0)) {
            days.add(0);
        }
        Set<String> dates = new HashSet<String>();

        while (from.compareTo(to) <= 0) {
            // Calendar.DAY_OF_WEEK: 1=Sunday, 2=Monday, ... -> -1
            if (days.contains(from.get(Calendar.DAY_OF_WEEK) - 1)) {
                dates.add(df.format(from.getTime()));
            }
            from.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private Set<String> resolveMonthDays(List<Integer> days, List<Integer> workingDays, List<WeeklyDay> weeklyDays, Calendar from, Calendar to)
            throws SOSInvalidDataException {

        Set<String> dates = new HashSet<String>();
        Calendar firstDayOfMonth = Calendar.getInstance();
        WeeklyDay weeklyDay = new WeeklyDay();

        while (from.compareTo(to) <= 0) {
            if (days != null) {
                if (days.contains(from.get(Calendar.DAY_OF_MONTH))) {
                    dates.add(df.format(from.getTime()));
                }
            }
            if (workingDays != null) {
                if (from.get(Calendar.DAY_OF_WEEK) != 1 && from.get(Calendar.DAY_OF_WEEK) != 7) {
                    if (days.contains(getWorkingDay(from, getFirstDayOfMonth(firstDayOfMonth, from)))) {
                        dates.add(df.format(from.getTime()));
                    }
                }
            }
            if (weeklyDays != null) {
                weeklyDay.setDay(from.get(Calendar.DAY_OF_WEEK) - 1);
                weeklyDay.setWeekOfMonth(getWeekOfMonthOfWeeklyDay(from));
                if (weeklyDays.contains(weeklyDay)) {
                    dates.add(df.format(from.getTime()));
                }
            }
            from.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private Set<String> resolveUltimos(List<Integer> days, List<Integer> workingDays, List<WeeklyDay> weeklyDays, Calendar from, Calendar to)
            throws SOSInvalidDataException {

        Set<String> dates = new HashSet<String>();
        WeeklyDay weeklyDay = new WeeklyDay();
        Calendar firstDayOfMonth = Calendar.getInstance();
        Calendar lastDayOfMonth = Calendar.getInstance();

        while (from.compareTo(to) <= 0) {
            if (days != null) {
                int dayOfUltimo = from.getActualMaximum(Calendar.DAY_OF_MONTH) + 1 - from.get(Calendar.DAY_OF_MONTH);
                if (days.contains(dayOfUltimo)) {
                    dates.add(df.format(from.getTime()));
                }
            }
            if (workingDays != null) {
                if (from.get(Calendar.DAY_OF_WEEK) != 1 && from.get(Calendar.DAY_OF_WEEK) != 7) {
                    if (days.contains(getUltimoWorkingDay(from, getLastDayOfMonth(lastDayOfMonth, from), getFirstDayOfMonth(firstDayOfMonth,
                            from)))) {
                        dates.add(df.format(from.getTime()));
                    }
                }
            }
            if (weeklyDays != null) {
                weeklyDay.setDay(from.get(Calendar.DAY_OF_WEEK) - 1);
                weeklyDay.setWeekOfMonth(getWeekOfMonthOfUltimoWeeklyDay(from));
                if (weeklyDays.contains(weeklyDay)) {
                    dates.add(df.format(from.getTime()));
                }
            }
            from.add(Calendar.DATE, 1);
        }
        return dates;
    }

    private Set<String> resolveRepetitions(RepetitionText repetition, Integer step, Calendar from, Calendar to) throws SOSInvalidDataException {
        if (repetition == null) {
            throw new SOSInvalidDataException("json field 'repetition' in 'repetitions' is undefined.");
        }
        if (step == null) {
            step = 1;
        }
        Set<String> dates = new HashSet<String>();
        int dayOfMonth = from.get(Calendar.DAY_OF_MONTH);

        while (from.compareTo(to) <= 0) {
            dates.add(df.format(from.getTime()));
            switch (repetition) {
            case DAILY:
                from.add(Calendar.DATE, step);
                break;
            case MONTHLY:
                from.add(Calendar.MONTH, step);
                if (dayOfMonth > from.get(Calendar.DAY_OF_MONTH) && from.getActualMaximum(Calendar.DAY_OF_MONTH) >= dayOfMonth) {
                    from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
                break;
            case WEEKLY:
                from.add(Calendar.DATE, (step * 7));
                break;
            case YEARLY:
                from.add(Calendar.YEAR, step);
                // if original 'from' was 29th of FEB
                if (dayOfMonth > from.get(Calendar.DAY_OF_MONTH) && from.getActualMaximum(Calendar.DAY_OF_MONTH) >= dayOfMonth) {
                    from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                }
                break;
            }
        }
        return dates;
    }

}