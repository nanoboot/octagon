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

package org.nanoboot.octagon.plugin.reminder.classes;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.core.EntityAttribute;
import org.nanoboot.octagon.entity.core.EntityAttributeType;
import org.nanoboot.powerframework.time.moment.UniversalDateTime;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@Data
public class Reminder implements Entity {
    public static final int RING_VOLUME_MIN = 1;
    public static final int RING_VOLUME_MAX = 5;
    protected static List<EntityAttribute> SCHEMA;

    /**
     * UUID identification of this entity.
     */
    private UUID id;

    //    /**
//     * example: ProductGroup
//     */
    private String type;
    private UUID objectId;

    private String comment;
    private Urgency urgency;

    private Integer runEveryXDays;
    private UniversalDateTime lastRun;
    private AlarmStatus alarmStatus;
    private Integer ringVolume;


    @Override
    public String getName() {
        if (type == null) {
            return comment;
        }
        return type + " with id " + objectId;
    }

    @Override
    public void validate() {
        if (alarmStatus != AlarmStatus.OFF && alarmStatus != null) {
            if (ringVolume == null) {
                throw new OctagonException("RingVolume must be set.");
            }
            if (ringVolume < RING_VOLUME_MIN) {
                throw new OctagonException("RingVolume must be greater or equal to " + RING_VOLUME_MIN + ".");
            }
            if (ringVolume > RING_VOLUME_MAX) {
                throw new OctagonException("RingVolume must be less or equal to " + RING_VOLUME_MAX + ".");
            }
        }
        if (alarmStatus == null || alarmStatus == AlarmStatus.OFF) {
            if (ringVolume != null) {
                throw new OctagonException("Alarm status is OFF. RingVolume must not be set.");
            }
        }
        if ((type == null && objectId != null)) {
            throw new OctagonException("Type is not set, but object id is set.");
        }
        if ((type != null && objectId == null)) {
            throw new OctagonException("Object id is not set, but type is set.");
        }
    }

    @Override
    public void loadFromMap(Map<String, String> map) {
        setType(getStringParam("type", map));
        setObjectId(getUuidParam("objectId", map));

        setComment(getStringParam("comment", map));
        String urgencyParam = getStringParam("urgency", map);
        setUrgency(urgencyParam == null ? null : Urgency.valueOf(urgencyParam));

        setRunEveryXDays(getIntegerParam("runEveryXDays", map));
        setLastRun(getUniversalDateTimeParam("lastRun", map));

        String alarmStatusParam = getStringParam("alarmStatus", map);
        setAlarmStatus(alarmStatusParam == null ? null : AlarmStatus.valueOf(alarmStatusParam));
        setRingVolume(getIntegerParam("ringVolume", map));
    }

    public Class getEntityClass() {
        return getClass();
    }

    @Override
    public String[] toStringArray() {
        return new String[]{
                id == null ? "" : id.toString(),

                type == null ? "" : type,
                objectId == null ? "" : objectId.toString(),

                comment == null ? "" : comment,
                urgency == null ? "" : urgency.name(),

                runEveryXDays == null ? "" : runEveryXDays.toString(),
                lastRun == null ? "" : lastRun.toString(),

                alarmStatus == null ? "" : alarmStatus.name(),
                ringVolume == null ? "" : ringVolume.toString(),

        };
    }

    @Override
    public List<EntityAttribute> getSchema() {
        if (SCHEMA == null) {
            SCHEMA = new ArrayList<>();

            SCHEMA.add(EntityAttribute.getIdEntityAttribute());
            SCHEMA.add(new EntityAttribute("type").withMandatory(false));
            SCHEMA.add(new EntityAttribute("objectId", EntityAttributeType.UUID).withMandatory(false));

            SCHEMA.add(new EntityAttribute("comment"));
            SCHEMA.add(new EntityAttribute("urgency",
                    Arrays.stream(Urgency.values()).map(Urgency::name).collect(Collectors.toList()))
                    .withDefaultValue(Urgency.UNSPECIFIED.name()).withMandatory(true));

            SCHEMA.add(new EntityAttribute("runEveryXDays", EntityAttributeType.INTEGER).withMandatory(true).withDefaultValue("7"));
            SCHEMA.add(new EntityAttribute("lastRun", EntityAttributeType.UNIVERSAL_DATE_TIME));

            SCHEMA.add(new EntityAttribute("alarmStatus",
                    Arrays.stream(AlarmStatus.values()).map(AlarmStatus::name).collect(Collectors.toList()))
                    .withDefaultValue(AlarmStatus.OFF.name()).withMandatory(false).withHelp("OFF > alarm turned off\nON > alarm waiting for reminder, once the reminder reaches its run, its alarm status becomes RINGING\nRINGING alarm sounding."));

            SCHEMA.add(new EntityAttribute("ringVolume", EntityAttributeType.INTEGER).withMandatory(false).withHelp("Range " + RING_VOLUME_MIN + "-" + RING_VOLUME_MIN));
        }
        return SCHEMA;
    }

}
