package com.wl.pluto.arch.download;

import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;

import java.io.File;

import rxtool.RxLogTool;

/**
 * @author szy
 * Created on 2019-09-18
 * @function
 */
public class DownloadTool {

    private static DownloadTool instance = new DownloadTool();

    private OnTaskCompleteListener taskCompleteListener;

    public void setTaskCompleteListener(OnTaskCompleteListener taskCompleteListener) {
        this.taskCompleteListener = taskCompleteListener;
    }

    private DownloadTool() {

    }

    public static DownloadTool getInstance() {
        return instance;
    }

    //下载资源的任务管理器
    private DownloadTask task;

    /**
     * 初始化下载任务
     */
    public void initTask(File parentFile, String fileName, String rul) {
        //rul 下载地址 , parentFile 父目录
        task = new DownloadTask.Builder(rul, parentFile).setFilename(fileName)
                // the minimal interval millisecond for callback progress
                .setMinIntervalMillisCallbackProcess(16)
                // ignore the same task has already completed in the past.
                .setPassIfAlreadyCompleted(false).build();

        final boolean started = task.getTag() != null;
        if (started) {
            // to cancel
            task.cancel();
        } else {
            // to start
            startTask();
            // mark
            task.setTag("mark-task-started");
        }

    }


    private void initStatus(ProgressBar progressBar) {
        if (progressBar != null) {
            final StatusUtil.Status status = StatusUtil.getStatus(task);
            if (status == StatusUtil.Status.COMPLETED) {
                progressBar.setProgress(progressBar.getMax());
            }
            final BreakpointInfo info = StatusUtil.getCurrentInfo(task);
            if (info != null) {
                ProgressUtil.calcProgressToView(progressBar, info.getTotalOffset(), info.getTotalLength());
            }
        }
    }

    private long totalLength;
    private void startTask() {

        task.enqueue(new DefaultDownloadListener() {
            private String readableTotalLength;
            @Override
            public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {
                totalLength = info.getTotalLength();
                readableTotalLength = Util.humanReadableBytes(totalLength, true);
                RxLogTool.i("---->", "totalLength = " + totalLength);
                RxLogTool.i("---->", "readableTotalLength = " + readableTotalLength);
            }


            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {
                if(taskCompleteListener != null){
                    taskCompleteListener.onTaskProgress(currentOffset, totalLength);
                }
            }


            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @org.jetbrains.annotations.Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
                task.setTag(null);
                if (taskCompleteListener != null) {
                    taskCompleteListener.onTaskComplete();
                }
            }
        });
    }

    public interface OnTaskCompleteListener {
        void onTaskComplete();

        void onTaskProgress(long progress, long total);
    }
}
