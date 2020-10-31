/*
 * Copyright (c) 2017 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wl.pluto.arch.download.queue;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rxtool.RxFileTool;
import rxtool.view.RxToast;

public class QueueController {
    private static final String TAG = "QueueController";
    private List<DownloadTask> taskList = new ArrayList<>();
    private DownloadContext context;
    private final QueueListener listener = new QueueListener();

    private File queueDir;

    public void initTasks(@NonNull Context context, @NonNull String parentPath, @NonNull List<String> urlList,  @NonNull DownloadContextListener listener) {

        final String path = RxFileTool.getRootPath().getAbsolutePath() + File.separator + "bkhb" + File.separator + "cache" + File.separator + "video" + File.separator;

        final DownloadContext.QueueSet set = new DownloadContext.QueueSet();
        if(RxFileTool.createOrExistsDir(path)){
            final File parentFile = new File(path, parentPath);
            this.queueDir = parentFile;

            set.setParentPathFile(parentFile);
            set.setMinIntervalMillisCallbackProcess(200);
        }else {
            RxToast.info("文件不存在");
            return;
        }


        final DownloadContext.Builder builder = set.commit();

        for(String item: urlList){
            DownloadTask boundTask = builder.bind(item);
            TagUtil.saveTaskName(boundTask, item);
        }

        builder.setListener(listener);
        this.context = builder.build();
        this.taskList = Arrays.asList(this.context.getTasks());
    }

    public void deleteFiles() {
        if (queueDir != null) {
            String[] children = queueDir.list();
            if (children != null) {
                for (String child : children) {
                    if (!new File(queueDir, child).delete()) {
                        Log.w("QueueController", "delete " + child + " failed!");
                    }
                }
            }

            if (!queueDir.delete()) {
                Log.w("QueueController", "delete " + queueDir + " failed!");
            }
        }

        for (DownloadTask task : taskList) {
            TagUtil.clearProceedTask(task);
        }
    }

    public void setPriority(DownloadTask task, int priority) {
        final DownloadTask newTask = task.toBuilder().setPriority(priority).build();
        this.context = context.toBuilder().bindSetTask(newTask).build();
        newTask.setTags(task);
        TagUtil.savePriority(newTask, priority);
        this.taskList = Arrays.asList(this.context.getTasks());
    }

    public void start(boolean isSerial) {
        this.context.start(listener, isSerial);
    }

    public void stop() {
        if (this.context.isStarted()) {
            this.context.stop();
        }
    }

    int size() {
        return taskList.size();
    }
}