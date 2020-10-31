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


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;


class QueueListener extends DownloadListener1 {
    private static final String TAG = "QueueListener";

    @Override
    public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
        final String status = "taskStart";
        TagUtil.saveStatus(task, status);

    }

    @Override
    public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {
        final String status = "retry";
        TagUtil.saveStatus(task, status);
    }

    @Override
    public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {
        final String status = "connected";
        TagUtil.saveStatus(task, status);
        TagUtil.saveOffset(task, currentOffset);
        TagUtil.saveTotal(task, totalLength);

    }

    @Override
    public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
        final String status = "progress";
        TagUtil.saveStatus(task, status);
        TagUtil.saveOffset(task, currentOffset);

        Log.i(TAG, "progress " + task.getId() + " with ");

    }

    @Override
    public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
        final String status = cause.toString();
        TagUtil.saveStatus(task, status);

    }
}