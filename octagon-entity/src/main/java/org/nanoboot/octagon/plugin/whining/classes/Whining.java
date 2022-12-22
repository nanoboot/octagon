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

package org.nanoboot.octagon.plugin.whining.classes;

import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.powerframework.time.duration.Duration;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import org.nanoboot.powerframework.time.utils.TimeUnit;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class Whining implements Entity {
    private static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    private String emailSubject;
    private String emailDescriptiveText;
    private Boolean enabled;
    private Boolean sendEvenIfNothingFound;

    private Integer runEveryXDays;
    private String cronExpression;
    private UniversalDateTime lastRun;

    private UniversalDateTime lastFailure;
    private String lastFailureMsg;

    public boolean isEligibleToBeExecuted() {
        if (lastRun == null || lastFailure != null) {
            return true;
        }
        UniversalDateTime lastRunPlusRunEveryXDays = lastRun.plusDuration(Duration.of(runEveryXDays, TimeUnit.DAY));
        System.err.println("lastRunPlusRunEveryXDays=" + lastRunPlusRunEveryXDays);
        if (UniversalDateTime.now().compareTo(lastRunPlusRunEveryXDays) > 1) {
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return emailSubject;
    }

    @Override
    public void validate() {
        if (runEveryXDays == null && cronExpression == null) {
            throw new OctagonException("runEveryXDays and cronExpression are both empty. One of them must be set.");
        }
        if (runEveryXDays != null && cronExpression != null) {
            throw new OctagonException("runEveryXDays and cronExpression are both set. Only one of them can be set.");
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setEmailSubject(getStringParam("emailSubject", map));
        setEmailDescriptiveText(getStringParam("emailDescriptiveText", map));
        setEnabled(getBooleanParam("enabled", map));
        setSendEvenIfNothingFound(getBooleanParam("sendEvenIfNothingFound", map));

        setRunEveryXDays(getIntegerParam("runEveryXDays", map));
        setCronExpression(getStringParam("cronExpression", map));
        setLastRun(getUniversalDateTimeParam("lastRun", map));

        setLastFailure(getUniversalDateTimeParam("lastFailure", map));
        setLastFailureMsg(getStringParam("lastFailureMsg", map));
    }

    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),

                emailSubject == null ? "" : emailSubject,
                emailDescriptiveText == null ? "" : emailDescriptiveText,
                enabled == null ? "" : convertBooleanToString(enabled),
                sendEvenIfNothingFound == null ? "" : convertBooleanToString(sendEvenIfNothingFound),

                runEveryXDays == null ? "" : runEveryXDays.toString(),
                cronExpression == null ? "" : cronExpression,
                lastRun == null ? "" : lastRun.toString(),

                lastFailure == null ? "" : lastFailure.toString(),
                lastFailureMsg == null ? "" : lastFailureMsg,
        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());

            SCHEMA.add(new EntityAttribute("emailSubject").withMandatory(true));
            SCHEMA.add(new EntityAttribute("emailDescriptiveText"));
            SCHEMA.add(new EntityAttribute("enabled", EntityAttributeType.BOOLEAN).withDefaultValue("0"));
            SCHEMA.add(new EntityAttribute("sendEvenIfNothingFound", EntityAttributeType.BOOLEAN).withDefaultValue("1"));

            SCHEMA.add(new EntityAttribute("runEveryXDays", EntityAttributeType.INTEGER));
            SCHEMA.add(new EntityAttribute("cronExpression"));
            SCHEMA.add(new EntityAttribute("lastRun", EntityAttributeType.UNIVERSAL_DATE_TIME));

            SCHEMA.add(new EntityAttribute("lastFailure", EntityAttributeType.UNIVERSAL_DATE_TIME));
            SCHEMA.add(new EntityAttribute("lastFailureMsg"));
        }
        return SCHEMA;
    }
    @Override
    public String[] getRelatedListsForEntity() {
        return new String[]{"getAssignedQueriesForWhining", "getAssignedSqlQueriesForWhining"};
    }
    public String[] getRelatedActionsForEntity() {
        return new String[]{
                "Assign query or sql query:create?className=WhiningAssignment&whiningId=",
                "Assigned queries and sql queries:list?className=WhiningAssignment&filter_whiningId=",
        };
    }
}
