package com.wl.pluto.component.optimization;

/**
 * @author szy
 * Created on 2019-09-18
 * @function
 */
/*public  class LeakReportService extends DisplayLeakService {

    @SuppressWarnings("ThrowableNotThrown")
    @Override
    protected void afterDefaultHandling(@NonNull HeapDump heapDump, @NonNull AnalysisResult result, @NonNull String leakInfo) {
        if (!result.leakFound || result.excludedLeak) {
            return;
        }
        try {
            Exception exception = new Exception("Memory Leak from LeakCanary");
            exception.setStackTrace(result.leakTraceAsFakeException().getStackTrace());
            Sentry.capture(exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}*/
