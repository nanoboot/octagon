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

package org.nanoboot.octagon.entity.impl.repos;

import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.entity.api.MapperApi;
import org.nanoboot.octagon.entity.classes.EntityLabel;
import org.nanoboot.octagon.entity.core.ActionType;
import org.nanoboot.octagon.entity.core.Entity;
import org.nanoboot.octagon.entity.api.QuestionRepository;
import org.nanoboot.octagon.entity.api.Repository;
import org.nanoboot.octagon.entity.utils.EntityLabelCache;
import org.nanoboot.powerframework.reflection.ReflectionUtils;
import org.nanoboot.powerframework.utils.NamingConvention;
import org.nanoboot.powerframework.utils.NamingConventionConvertor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.nanoboot.octagon.entity.api.EntityRegistryEntry;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
@AllArgsConstructor
public class RepositoryImpl<T extends Entity> implements Repository<T> {

    private static final org.apache.logging.log4j.Logger LOG = LogManager.getLogger(RepositoryImpl.class);

    protected final MapperApi<T> mapper;
    private final Class type;
    @Getter
    @Setter
    protected QuestionRepository questionRepository;
    @Getter
    @Setter
    protected RepositoryRegistry repositoryRegistry;

    private EntityLabelCache labelCache;

    public RepositoryImpl(MapperApi<T> mapper, Class type) {
        this.mapper = mapper;
        this.type = type;
    }

    @Override
    public void create(T t) {
        String mandatoryFieldsCheck = t.hasAllMandatoryFields();
        if (mandatoryFieldsCheck != null) {
            throw new OctagonException("Mandatory field " + mandatoryFieldsCheck + " is missing.");
        }

        t.validate();
        processQuestions(t, ActionType.CREATE);

        t.validateCreate();
        t.autofillIfNeeded(t, repositoryRegistry);
        mapper.create(t);
    }

    private void processQuestions(T t, ActionType actionType) {
        List<String> questions = t.listQuestionsToAsk();
        if (questions == null || questions.isEmpty()) {
            //nothing to do
            return;
        }
        if (this.questionRepository == null) {
            throw new OctagonException("questionRepository == null");
        }
        Map<String, String> answers = questionRepository.ask(questions, t);
        t.validateQuestionAnswers(answers, actionType);
    }

    @Override
    public T read(String id) {
        return mapper.read(id);
    }

    @Override
    public void update(T updated) {
        update(updated, true);
    }

    @Override
    public void updateWithoutValidatioon(T updated) {
        update(updated, false);
    }

    private void update(T updated, boolean withValidation) {
        if (updated.getId() == null) {
            throw new OctagonException("Cannot update, id was not given.");
        }

        if (withValidation) {
            updated.validate();
            processQuestions(updated, ActionType.UPDATE);
        }

        T current = read(updated.getId().toString());
        if (withValidation) {
            try {
                current.validateUpdate(updated);
            } catch (Exception e) {
                throw new OctagonException("Update of this " + current.getEntityName() + " is not possible. " + e);
            }
        }
        String mandatoryFieldsCheck = updated.hasAllMandatoryFields();
        if (mandatoryFieldsCheck != null) {
            throw new OctagonException("Mandatory field " + mandatoryFieldsCheck + " is missing.");
        }
        updated.autofillIfNeeded(updated, repositoryRegistry);
        mapper.update(updated);
    }

    @Override
    public void delete(String id) {
        T toBeDeleted = read(id);

        toBeDeleted.validateDelete();
        processQuestions(toBeDeleted, ActionType.CREATE);

        mapper.delete(id);
    }

    @Override
    public List<T> list(String sqlWherePart) {
        String defaultWherePart = " 1 = 1 ";

        if (sqlWherePart == null) {
            sqlWherePart = defaultWherePart;
        } else {
            //nothing to do
        }

        if (sqlWherePart.equals(defaultWherePart)) {
            Class thisClass = getClassOfType();
            Constructor constructor = ReflectionUtils.getConstructor(thisClass);
            Entity emptyInstance = (Entity) ReflectionUtils
                    .newInstance(constructor);
            if (emptyInstance == null) {
                System.err.println("emptyInstance is null");
            } else {
                if (emptyInstance.getDefaultOrder() != null) {
                    String defaultOrderArray[] = emptyInstance.getDefaultOrder().split(",");
                    StringBuilder sb = new StringBuilder();
                    int lastIndex = defaultOrderArray.length - 1;
                    for (int i = 0; i < defaultOrderArray.length; i++) {
                        String string = defaultOrderArray[i].trim();
                        String array[] = string.split(" ");
                        String columnName = array[0];
                        String ascDesc = array.length > 1 ? array[1] : "asc";

                        String dbColumnName = NamingConventionConvertor.convert(columnName, NamingConvention.JAVA_FIELD, NamingConvention.DATABASE);
                        sb.append(dbColumnName + " " + ascDesc);
                        if (i < lastIndex) {
                            sb.append(",");
                        }
                    }

                    sqlWherePart = sqlWherePart + " ORDER BY " + sb.toString();
                }
            }

        } else {
            System.err.println("!!!+++ sqlWherePart=" + sqlWherePart);
        }

        List<T> result = mapper.list(sqlWherePart);

        System.out.println("Going to filter (size=" + result.size() + "): " + sqlWherePart);
        return result;
    }

    @Override
    public Class<T> getClassOfType() {
        return type;
    }

    @Override
    public String getKey() {
        return getClassOfType().getSimpleName();
    }

    @Override
    public String getOldKey() {
        try {
            return getClassOfType().newInstance().getOldEntityClass();
        } catch (InstantiationException | IllegalAccessException ex) {
            System.out.println("###: getClassOfType().newInstance().getOldEntityClass() FAILED." + ex.getMessage());
            Logger.getLogger(EntityRegistryEntry.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getLabel(String id) {
        String defaultLabel = id + " (label not found)";
        if (labelCache == null) {
            labelCache = new EntityLabelCache(mapper.getLabels());
        }
        if (labelCache.getMinutesSinceLastReset() > 60) {
            List<EntityLabel> list = mapper.getLabels();
            if (list == null || list.isEmpty()) {
                throw new OctagonException("Repository : label list returned null or empty for entity name " + getClassOfType().getSimpleName());
            } else {
                this.labelCache.reset(list);
            }
        }

        if (!labelCache.hasId(id)) {
            String label = mapper.getLabel(id);
            if (label == null) {
                return defaultLabel + " getLabelFor returned null";
            }
            labelCache.put(id, label);
        }
        String label = labelCache.getLabelForId(id);
        if (label == null) {
            String msg = "RepositoryImpl<" + getClassOfType().getSimpleName() + ">.getLabel(" + id + "=" + label;
            throw new OctagonException(msg);
        }
        return label;

    }

    @Override
    public List<EntityLabel> getLabels() {
        return mapper.getLabels();
    }

}
