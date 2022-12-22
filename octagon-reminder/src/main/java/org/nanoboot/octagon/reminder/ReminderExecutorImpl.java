///////////////////////////////////////////////////////////////////////////////////////////////
// Octagon: Database frontend.
// Copyright (C) 2020-2022 the original author or authors.
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// as published by the Free Software Foundation; version 2
// of the License only.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
///////////////////////////////////////////////////////////////////////////////////////////////

package org.nanoboot.octagon.reminder;

import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.plugin.reminder.api.ReminderRepository;
import org.nanoboot.octagon.plugin.reminder.classes.AlarmStatus;
import org.nanoboot.octagon.plugin.reminder.classes.Reminder;
import org.nanoboot.powerframework.time.duration.Duration;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.powerframework.time.utils.TimeUnit;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.tags.A;
import org.nanoboot.powerframework.web.html.tags.Div;
import org.nanoboot.powerframework.web.html.tags.Table;
import org.nanoboot.powerframework.web.html.tags.Td;
import org.nanoboot.powerframework.web.html.tags.Th;
import org.nanoboot.powerframework.web.html.tags.Tr;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.Element;
import org.nanoboot.powerframework.xml.NoElement;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.core.utils.Mailer;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class ReminderExecutorImpl extends Thread {
    @Setter
    private RepositoryRegistry repositoryRegistry;

    @Setter
    private LabelRepository labelRepository;
    @Setter
    private Mailer mailer;
    @Setter
    private AtomicBoolean stop = new AtomicBoolean(false);
    @Setter
    private String env;
    @Setter
    private Boolean enabled;

    private boolean atLeastOneExecution = false;

    private ReminderRepository reminderRepository;
    private ActionLogRepository actionLogRepository;
    public ReminderExecutorImpl() {
        setName(getClass().getName());
        setDaemon(true);
    }

    @Override
    public void run() {
        if(!enabled) {
            return;
        }

        if(repositoryRegistry == null) {
            throw new OctagonException("Spring dependency repositoryRegistry is not satisfied (repositoryRegistry is null).");
        } else {
            reminderRepository = (ReminderRepository) repositoryRegistry.find("Reminder");
            actionLogRepository = (ActionLogRepository) repositoryRegistry.find("ActionLog");
        }
        System.out.println("Reminder starting.");

        while (true) {

            UniversalDateTime udt = UniversalDateTime.now();
            int minute = udt.getMinute();
            int second = udt.getSecond();
            int millisecond = udt.getMillisecond();
            Duration fromFullHour = new Duration(0, 0, minute, second, millisecond);
            Duration oneHour = Duration.of(1, TimeUnit.HOUR);
            Duration untilFullHour = oneHour.minus(fromFullHour);

            System.err.println("fromFullHour=" + fromFullHour.toString());
            System.err.println("oneHour=" + oneHour.toString());
            System.err.println("untilFullHour=" + untilFullHour.toString());

            System.err.println("Going to sleep for " + untilFullHour.toTotal(TimeUnit.MINUTE) + " minutes.");
            untilFullHour = untilFullHour.plus(15, TimeUnit.MINUTE);
            if (atLeastOneExecution) {
                untilFullHour = untilFullHour.plus(6, TimeUnit.HOUR);
            }

            try {
                //long ms = 10000;
                long ms = Double.valueOf(untilFullHour.toTotal(TimeUnit.MILLISECOND)).intValue();
                if (!"prod".equals(env)) {
                    ms = 30000;
                }
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            execute();
            atLeastOneExecution = true;

            if (stop.get()) {
                System.err.println("BREAKING LOOP");
                break;
            }
        }
    }

    private void execute() {
        String wherePartOfSql = " (LAST_RUN IS NULL OR ALARM_STATUS='RINGING' OR DATETIME('NOW','localtime') > DATETIME(substr(LAST_RUN,0,20), '+'|| RUN_EVERY_X_DAYS ||' DAY'))";
        List<Reminder> reminders = reminderRepository.list(wherePartOfSql);
        System.err.println("############# found " + reminders.size() + " reminder" + (reminders.size() == 1 ? "" : "s"));
        if (reminders.size() == 0) {
            //nothing to do
            return;
        }
        try {
            executeRemindersSending(reminders);
        } catch (NullPointerException e) {
            System.err.println("Processing reminders failed. NullPointerException");
            String response = mailer.sendMessage("Processing reminders failed. ", "NullPointerException");
            if (response != null) {
                System.err.println("Processing reminders failed and sending this failure by e-mail also failed. NullPointerException");
            }
            return;
        } catch (Exception e) {
            System.err.println("Processing reminders failed. " + e.getMessage());
            String response = mailer.sendMessage("Processing reminders failed", e.getMessage());
            if (response != null) {
                System.err.println("Processing reminders failed and sending this failure by e-mail also failed. " + e.getMessage());
            }
            return;
        }
        for (Reminder reminder : reminders) {
            Reminder reminderBefore = reminderRepository.read(reminder.getId().toString());
            try {
                reminder.setLastRun(UniversalDateTime.now());
                if (reminder.getAlarmStatus() == AlarmStatus.ON) {
                    reminder.setAlarmStatus(AlarmStatus.RINGING);
                }
                reminderRepository.update(reminder);
                actionLogRepository.persistActionLog(ActionType.UPDATE, reminderBefore, reminder, reminder, null);
            } catch (Exception e) {
                String error = e == null ? "NullPointerException" : e.getMessage();
                e.printStackTrace();
                if (error.contains("Could not connect to")) {
                    break;
                }

            } finally {
                if (reminder.toJsonObject().toMinimalString().equals(reminder.toJsonObject().toMinimalString())) {
                    //nothing to do
                } else {

                }
            }

        }
    }

    private void executeRemindersSending(List<Reminder> reminders) {
        String emailSubject = "New reminders";
        String body = null;

        Div div = new Div();
        div.add(new WebElement("p").setInnerText("<i>list of new reminders</i>"));

        List<Reminder> remindersWithHighestUrgency = new ArrayList<>();
        List<Reminder> remindersWithHighUrgency = new ArrayList<>();
        List<Reminder> remindersWithMediumUrgency = new ArrayList<>();
        List<Reminder> remindersWithLowUrgency = new ArrayList<>();
        List<Reminder> remindersWithLowestUrgency = new ArrayList<>();
        List<Reminder> remindersWithUnspecifiedUrgency = new ArrayList<>();
        List<Reminder> remindersWithOtherUrgency = new ArrayList<>();

        List<Reminder> finalReminders = new ArrayList<>();
        for (Reminder r : reminders) {
            switch (r.getUrgency()) {
                case HIGHEST:
                    remindersWithHighestUrgency.add(r);
                    break;
                case HIGH:
                    remindersWithHighUrgency.add(r);
                    break;
                case MEDIUM:
                    remindersWithMediumUrgency.add(r);
                    break;
                case LOW:
                    remindersWithLowUrgency.add(r);
                    break;
                case LOWEST:
                    remindersWithLowestUrgency.add(r);
                    break;
                case UNSPECIFIED:
                    remindersWithUnspecifiedUrgency.add(r);
                    break;
                default: {
                    System.err.println("Unknown Urgency type: " + r.getUrgency() + ". But added to the mail report.");
                    remindersWithOtherUrgency.add(r);
                }
            }
        }
        Collections.shuffle(remindersWithHighestUrgency);
        Collections.shuffle(remindersWithHighUrgency);
        Collections.shuffle(remindersWithMediumUrgency);
        Collections.shuffle(remindersWithLowUrgency);
        Collections.shuffle(remindersWithLowestUrgency);
        Collections.shuffle(remindersWithUnspecifiedUrgency);
        Collections.shuffle(remindersWithOtherUrgency);

        finalReminders.addAll(remindersWithHighestUrgency);
        finalReminders.addAll(remindersWithHighUrgency);
        finalReminders.addAll(remindersWithMediumUrgency);
        finalReminders.addAll(remindersWithLowUrgency);
        finalReminders.addAll(remindersWithLowestUrgency);
        finalReminders.addAll(remindersWithUnspecifiedUrgency);
        finalReminders.addAll(remindersWithOtherUrgency);

        addTable(div, finalReminders);

        iterateAllElements(div);
        body = div.toString();

//            String str = body;
//            BufferedWriter writer = null;
//            try {
//                writer = new BufferedWriter(new FileWriter("/home/robertvokac/Desktop/" + UniversalDateTime.now().toLong() + ".html"));
//                writer.write(str);
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


        //System.err.println(body);
        String response = mailer.sendMessage(emailSubject, body);
        if (response != null) {
            throw new OctagonException(response);
        }
    }

    private void addTable(Div div, List<Reminder> reminders) {
        Table table = new Table();
        div.add(table);

        table.getAttributes().setAllowAnyAttribute(true);
        table.add(new Attribute("style", "border:2px solid black;border-collapse: collapse;margin-bottom:50px;"));

        Tr trHeader = new Tr();
        trHeader.add(
                new Th("#"),
                new Th("ID"),
                new Th("Type"),
                new Th("Object id"),
                new Th("Name"),
                new Th("Alarm Status"),
                new Th("Ring Volume"),
                new Th("Comment"),
                new Th("Urgency"),
                new Th("Run every x days"),
                new Th("Last run"),
                new Th("Shut down")
        );

        table.add(trHeader);

        int rowNumber = 1;
        for (Reminder r : reminders) {
            Tr tr = new Tr();
            tr.add(new Td(String.valueOf(rowNumber++)));
            String href0 = "read?className=" + "Reminder" + "&id=" + r.getId().toString();
            tr.add(new Td(new A(href0, r.getId().toString()).setInnerText(r.getId().toString()).toString()));

            tr.add(new Td(r.getType() == null ? "" : r.getType()));
            tr.add(new Td(r.getObjectId() == null ? "" : r.getObjectId().toString()));

            String name = "";

            if (r.getObjectId() != null && r.getType() != null) {
                String type = r.getType();
                String foreignKey = type.substring(0, 1).toLowerCase() + type.substring(1);

                String label = labelRepository.getLabel(foreignKey, r.getObjectId().toString());
                String href = "read?className=" + type + "&id=" + r.getObjectId().toString();
                String finalValue = new A(href, label).setInnerText(label).toString();
                name = finalValue;
            }
            tr.add(new Td(name));
            tr.add(new Td(r.getAlarmStatus() == null ? "" : r.getAlarmStatus().name()));
            int ringVolumeInt = r.getRingVolume() == null ? 3 : r.getRingVolume();
            StringBuilder sb = new StringBuilder();
            if (r.getAlarmStatus() != AlarmStatus.OFF) {
                for (int i = 1; i <= ringVolumeInt; i++) {
                    sb.append("*");
                }
            }
            boolean alarmIsOn = r.getAlarmStatus() == AlarmStatus.RINGING || r.getAlarmStatus() == AlarmStatus.ON;
            if (alarmIsOn) {
                tr.getAttributes().setAllowAnyAttribute(true);
                tr.add(new Attribute("style", "background:red;font-size:150%;"));
            }
            String ringVolume = sb.toString();
            tr.add(new Td(ringVolume));

            tr.add(new Td(r.getComment() == null ? "" : r.getComment()));
            tr.add(new Td(r.getUrgency().name()));
            tr.add(new Td(r.getRunEveryXDays().toString()));
            UniversalDateTime lastRun = r.getLastRun();
            tr.add(new Td(lastRun == null ? "" : lastRun.toString()));
            if (alarmIsOn) {
                String href2 = "update?className=" + "Reminder" + "&id=" + r.getId().toString() + "&alarmStatus=OFF";
                tr.add(new Td(new A(href2, r.getId().toString()).setInnerText("Shut down")));
            } else {
                tr.add(new Td(""));
            }
            table.add(tr);
        }
    }

    public void stopExecutor() {
        System.err.println("STOPPING");
        stop.set(true);
        System.err.println("STOPPING DONE");
    }

    private void iterateAllElements(Element e) {
        for (Element el : e.getElements().getList()) {
            iterateAllElements(el);
        }
        String elementName = e.getElementName();
        if (elementName.equals("td") || elementName.equals("th")) {
            e.getAttributes().setAllowAnyAttribute(true);
            e.add(new Attribute("style", "border: 1px solid black"));
        }
        if (e.getElementName().equals("noelement")) {
            NoElement ne = (NoElement) e;
            String innerText = ne.getPlainText();
            if (innerText != null && innerText.contains("href=")) {
                innerText = innerText.replaceAll("href=\"", "href=\"" + "http://localhost:8080/octagon/ui/");
            }
            ne.setPlainText(innerText);
        }
    }

}


