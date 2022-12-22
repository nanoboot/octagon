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

package org.nanoboot.octagon.batch.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.nanoboot.octagon.entity.api.LabelRepository;
import org.nanoboot.octagon.entity.api.RepositoryRegistry;
import org.nanoboot.octagon.plugin.actionlog.api.ActionLogRepository;
import org.nanoboot.octagon.core.exceptions.OctagonException;
import org.nanoboot.octagon.core.utils.Mailer;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicBoolean;
import org.nanoboot.octagon.plugin.api.core.Batch;
import org.nanoboot.octagon.plugin.api.core.PluginStub;
import org.nanoboot.octagon.plugin.api.factories.PluginLoader;

/**
 *
 * @author <a href="mailto:robertvokac@nanoboot.org">Robert Vokac</a>
 * @since 0.1.0
 */
public class BatchExecutorImpl extends Thread {

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
    @Setter
    private PluginLoader pluginLoader;

    private ActionLogRepository actionLogRepository;

    public BatchExecutorImpl() {
        setName(getClass().getName());
        setDaemon(true);
    }

    @Override
    public void run() {
        if (!enabled) {
            return;
        }

        if (repositoryRegistry == null) {
            throw new OctagonException("Spring dependency repositoryRegistry is not satisfied (repositoryRegistry is null).");
        } else {
            actionLogRepository = (ActionLogRepository) repositoryRegistry.find("ActionLog");
        }
        System.out.println("Batch executor starting.");
        List<String> batchClasses = new ArrayList<>();
        for (PluginStub ps : pluginLoader.getPluginStubs()) {
            for (String b : ps.getBatchClasses()) {
                System.out.println("Adding batch " + b);
                batchClasses.add(b);
            }
        }
        for (String bc : batchClasses) {
            try {
                startBatch(bc);
            } catch (Exception e) {
                String msg = "Starting batch " + bc + " failed: " + e.getMessage();
                System.err.println(msg);
            }
        }

    }

    public void stopExecutor() {
        System.err.println("STOPPING");
        stop.set(true);
        System.err.println("STOPPING DONE");
    }

    private void startBatch(String bc) {
        System.out.println("Starting batch " + bc);
        Class batchClazz;
        try {
            batchClazz = Class.forName(bc);
        } catch (ClassNotFoundException ex) {
            throw new OctagonException(ex.getMessage());
        }
        Constructor constructor;
        try {
            constructor = batchClazz.getConstructor();
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new OctagonException(ex.getMessage());
        }
        Batch batch;
        try {
            batch = (Batch) constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new OctagonException(ex.getMessage());
        }
        batch.addObjectDependency(RepositoryRegistry.class.getName(), repositoryRegistry);
        batch.addObjectDependency(ActionLogRepository.class.getName(), actionLogRepository);
        Thread thread = new Thread(batch);
        thread.start();
    }

}
