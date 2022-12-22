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

package org.nanoboot.octagon.whining.impl;

import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.QueryRepository;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.entity.api.SqlExplorer;
import org.nanoboot.octagon.entity.api.SqlQueryRepository;
import org.nanoboot.octagon.entity.classes.Query;
import org.nanoboot.octagon.entity.classes.SqlQuery;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.utils.QueryDescription;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.plugin.whining.api.WhiningAssignmentRepository;
import org.nanoboot.octagon.plugin.whining.api.WhiningRepository;
import org.nanoboot.octagon.plugin.whining.classes.Whining;
import org.nanoboot.octagon.plugin.whining.classes.WhiningAssignment;
import org.nanoboot.powerframework.json.JsonObject;
import org.nanoboot.powerframework.time.duration.Duration;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.powerframework.time.utils.TimeUnit;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import org.nanoboot.powerframework.web.html.WebElement;
import org.nanoboot.powerframework.web.html.attributes.Id;
import org.nanoboot.powerframework.web.html.tags.*;
import org.nanoboot.powerframework.xml.Attribute;
import org.nanoboot.powerframework.xml.Element;
import org.nanoboot.powerframework.xml.NoElement;
import org.nanoboot.octagon.html.tables.SqlExplorerTable;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.core.utils.Mailer;
import lombok.Setter;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class WhiningExecutorImpl extends Thread {
    @Setter
    private RepositoryRegistry repositoryRegistry;

    @Setter
    private LabelRepository labelRepository;
    @Setter
    private Mailer mailer;
    @Setter
    private SqlExplorer sqlExplorer;
    @Setter
    private AtomicBoolean stop = new AtomicBoolean(false);
    @Setter
    private String env;
    @Setter
    private Boolean enabled;

    private WhiningRepository whiningRepository;
    private WhiningAssignmentRepository whiningAssignmentRepository;
    private QueryRepository queryRepository;
    private SqlQueryRepository sqlQueryRepository;
    private ActionLogRepository actionLogRepository;

    @Deprecated
    private TempLogTool tempLogTool;

    public WhiningExecutorImpl() {
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
            whiningRepository = (WhiningRepository) repositoryRegistry.find("Whining");
            whiningAssignmentRepository = (WhiningAssignmentRepository) repositoryRegistry.find("WhiningAssignment");
            queryRepository = (QueryRepository) repositoryRegistry.find("Query");
            sqlQueryRepository = (SqlQueryRepository) repositoryRegistry.find("SqlQuery");
            actionLogRepository = (ActionLogRepository) repositoryRegistry.find("ActionLog");
        }
        System.out.println("Whining starting.");


        while (true) {
            //fillActionLog();

            //if (true)break;
            UniversalDateTime udt = UniversalDateTime.now();
            int year = udt.getYear();
            int month = udt.getMonth();
            int day = udt.getDay();
            int hour = udt.getHour();
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

            try {
                //long ms = 10000;
                long ms = Double.valueOf(untilFullHour.toTotal(TimeUnit.MILLISECOND)).intValue();
                if (!"prod".equals(env)) {
                    ms = 10000;
                }
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            execute();

            if (stop.get()) {
                System.err.println("BREAKING LOOP");
                break;
            }
        }
    }

    @Deprecated
    private void fillActionLog() {
        System.err.println("#####");
        int i = 0;
        List<Repository> allRepositories = tempLogTool.getAllRepositories();
        for (Repository repo : allRepositories) {
            String name = repo.getClassOfType().getSimpleName();
            System.err.println(name);
            List<Object> list = repo.list(" 1 = 1 ");
            for (Object o : list) {
                Entity e = (Entity) o;
                //System.err.println(e.toJsonObject().toPrettyString());
                actionLogRepository.persistActionLog(ActionType.CREATE, null, e, e, null);
            }
            i++;
            System.err.println("temp log tool: done: " + i + " / " + allRepositories.size());
        }

        System.err.println("temp log tool: done all: " + i + " / " + allRepositories.size());
    }

    private void execute() {
        String wherePartOfSql = " ENABLED = 1 AND LAST_FAILURE IS NULL AND (LAST_RUN IS NULL OR DATETIME('NOW','localtime') > DATETIME(substr(LAST_RUN,0,20), '+'|| RUN_EVERY_X_DAYS ||' DAY'))";
        List<Whining> whinings = whiningRepository.list(wherePartOfSql);
        System.err.println("############# found " + whinings.size() + " whining(s)");
        for (Whining whining : whinings) {
            Whining whiningBefore = whiningRepository.read(whining.getId().toString());
            try {
                executeWhining(whining);
                whining.setLastRun(UniversalDateTime.now());
                whiningRepository.update(whining);
                actionLogRepository.persistActionLog(ActionType.UPDATE, whiningBefore, whining, whining, null);
            } catch (Exception e) {
                String error = e == null ? "NullPointerException" : e.getMessage();
                e.printStackTrace();
                whining.setLastFailure(UniversalDateTime.now());
                whining.setLastFailureMsg(HtmlUtils.htmlEscape(error));
                whiningRepository.update(whining);
                actionLogRepository.persistActionLog(ActionType.UPDATE, whiningBefore, whining, whining, null);
                if (error.contains("Could not connect to")) {
                    break;
                }

            } finally {
                if (whiningBefore.toJsonObject().toMinimalString().equals(whining.toJsonObject().toMinimalString())) {
                    //nothing to do
                } else {

                }
            }

        }
    }

    private void executeWhining(Whining whining) {
        System.err.println("%%%" + whining.toString());
        String whiningId = whining.getId().toString();

        String emailSubject = whining.getEmailSubject();
        String body = null;

        Div div = new Div();
        div.add(new WebElement("p").setInnerText(whining.getEmailDescriptiveText()));

        List<WhiningAssignment> whiningAssignments = whiningAssignmentRepository.list("whining_id='" + whiningId + "' ORDER BY SORTKEY DESC");
        if (!whining.getSendEvenIfNothingFound() && whiningAssignments.isEmpty()) {
            //nothing to do;
            return;
        }

        div.add(new H1().setInnerText("Content"));

        Ul contentList = new Ul();
        contentList.getAttributes().setAllowAnyAttribute(true);
        contentList.add(new Attribute("style", "font-size:150%;"));
        div.add(contentList);

        int tableNumber = 0;
        for (WhiningAssignment wa : whiningAssignments) {
            if (wa.getQueryId() == null && wa.getSqlQueryId() == null) {
                throw new OctagonException("Both query and sql query are null. One of them must be set.");
            }
            tableNumber++;
            if (wa.getQueryId() != null) {
                Query q = queryRepository.read(wa.getQueryId().toString());
                QueryDescription queryDescription = new QueryDescription(new JsonObject(q.getData()), repositoryRegistry);
                String where = queryDescription.getSql();
                String tableName = NamingConventionConvertor.convert(Character.toLowerCase(q.getType().charAt(0)) + q.getType().substring(1), NamingConvention.JAVA, NamingConvention.DATABASE);
                String sql = "select * from " + tableName + " where " + where;

                String finalName = tableNumber + ". " + q.getName();
                addTable(div, sql, q.getType(), finalName, q.getDescription(), q.getTodo(), null, "query");

                contentList.add(new Li(new A("#ch" + tableNumber, finalName).setInnerText(finalName).toString()));

                continue;
            }
            if (wa.getSqlQueryId() != null) {
                SqlQuery q = sqlQueryRepository.read(wa.getSqlQueryId().toString());
                String sql = q.getSql();

                String finalName = tableNumber + ". " + q.getName();
                addTable(div, sql, q.getType(), finalName, q.getDescription(), q.getTodo(), q.getForeignKeysLegend(), "sql query");

                contentList.add(new Li(new A("#ch" + tableNumber, finalName).setInnerText(finalName).toString()));

                continue;
            }

        }

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

    private void addTable(Div div, String sql, String type, String name, String description, String todo, String foreignKeysLegend, String queryType) {
        String[][] result = sqlExplorer.execute(sql);

        Element h1 = new H1().setInnerText(name);

        String chNumber = name.split("\\. ")[0];
        h1.add(new Id("ch" + chNumber));
        div.add(h1);

        div.add(
                new WebElement("b", "Description: "),
                new WebElement("span", description == null ? "No description" : description),
                new Br()
        );
        div.add(
                new WebElement("b", "Query type: "),
                new WebElement("span", queryType == null ? "Undefined" : queryType),
                new Br()
        );
        div.add(
                new WebElement("b", "Todo: "),
                new WebElement("span", todo == null ? "Undefined" : todo),
                new Br()
        );

        SqlExplorerTable table = new SqlExplorerTable(result, labelRepository, type, foreignKeysLegend);
        div.add(table);
        table.getAttributes().setAllowAnyAttribute(true);
        table.add(new Attribute("style", "border:2px solid black;border-collapse: collapse;margin-bottom:50px;"));
        table.getElements().getList().remove(0);
        table.getElements().getList().remove(0);
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
            System.out.println("innerText=" + innerText);

            if (innerText != null && innerText.contains("href=") && !innerText.contains("href=\"#")) {
                innerText = innerText.replaceAll("href=\"", "href=\"" + "http://localhost:8080/octagon/ui/");
            }
            ne.setPlainText(innerText);
        }
    }

}


