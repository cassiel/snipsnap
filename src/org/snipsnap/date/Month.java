package com.neotis.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Object that generates a View of the month
 *
 * @author stephan
 * @version $Id$
 **/

public class Month {

  private String[] months = {
    "Januar", "Februar", "Maerz", "April",
    "Mai", "Juni", "Juli", "August",
    "September", "Oktober", "November", "Dezember"
  };
  private String[] monthsValue = {
    "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"
  };

  /** The days in each month. */
  public final static int dom[] = {
    31, 28, 31, 30,	/* jan feb mar apr */
    31, 30, 31, 31, /* may jun jul aug */
    30, 31, 30, 31	/* sep oct nov dec */
  };

  public Month() {
  }


  public String getView(int month, int year)  {
    StringBuffer view = new StringBuffer();
    view.append("<table>");
    view.append("<tr colspan=\"7\"><b>");
    view.append(months[month]);
    view.append(" ");
    view.append(year);
    view.append("</b></tr>");

    int leadGap = 0;  // for German Style Monday starting weeks

    if(month < 0 || month > 11)
      throw new IllegalArgumentException("Month " + month + " bad, must be 0-11");

    Calendar today = new GregorianCalendar();
    today.setTime(new java.util.Date());
    int todayNumber = today.get(Calendar.DAY_OF_MONTH);

    GregorianCalendar calendar = new GregorianCalendar(year, month, 1);

    // Compute how much to leave before the first day.
    // getDay() returns 0 for Sunday, which is just right.
    leadGap = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    leadGap = (leadGap -1 );
    if (leadGap<0) leadGap=6;

    int daysInMonth = dom[month];
    if(calendar.isLeapYear(calendar.get(Calendar.YEAR)) && month == 1)
      ++daysInMonth;

    view.append("<tr><td>Mo</td><td>Di</td><td>Mi</td><td>Do</td><td>Fr</td><td>Sa</td><td>So</td></tr>");

    StringBuffer week = new StringBuffer();
    // Blank out the labels before 1st day of month
    for(int i = 0; i < leadGap; i++) {
      week.append("<td></td>");
    }

    // Fill in numbers for the day of month.

    for(int i = 1; i <= daysInMonth; i++) {

      if(i == todayNumber && month == today.get(Calendar.MONTH) && year == today.get(Calendar.YEAR)) {
        week.append("<td>");
        week.append("<b>");
        week.append(i);
        week.append("</b>");
        week.append("</td>");
      } else {
        week.append("<td>");
        week.append(i);
        week.append("</td>");
      }

      // wrap if end of line.
      if((leadGap + i) % 7 == 0) {
        view.append("<tr align=\"right\">");
        view.append(week);
        week.setLength(0);
        view.append("</tr>");
      }

    }
    view.append("<tr>");
    view.append(week);
    view.append("</tr>");
    view.append("</table>");
    return view.toString();
  }

}