package com.smzdm.commons.job;

import java.util.List;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class FixTimeScheduler <T extends Task> {
    public static final int ONE_SECOND = 1000;
    public static final int MILLIS_OF_ONE_SECOND = 1000;

    private String jobName = "FixedTimeScheduler";

    private TaskExecutor taskExecutor = new DefaultTaskExecutor();

    private long intervalInSecond;

    private long nextScheduleTimestamp;

    private TaskStore<T> taskStore;
    private TaskParser<T> taskParser;
    private volatile boolean shutdown = false;


    public FixTimeScheduler() {
        nextScheduleTimestamp = System.currentTimeMillis();
    }


    public void start() {
        new Thread(new Runnable() {
            public void run() {
                while (!shutdown) {
                    if (System.currentTimeMillis() > nextScheduleTimestamp) {
                        nextScheduleTimestamp = nextTime();
                        List<T> tasks = taskStore.load();
                        for (T task : tasks) {
                            taskExecutor.execute(new TaskContext<T>(task, taskStore, taskParser));
                        }
                    } else {
                        try {
                            Thread.sleep(ONE_SECOND);
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }

                }
            }
        }).start();
    }

    public void shutdown() {
        shutdown = true;
    }

    private long nextTime() {
        return nextScheduleTimestamp + intervalInSecond * MILLIS_OF_ONE_SECOND;
    }


    public long getIntervalInSecond() {
        return intervalInSecond;
    }

    public void setIntervalInSecond(long intervalInSecond) {
        this.intervalInSecond = intervalInSecond;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setTaskStore(TaskStore<T> taskStore) {
        this.taskStore = taskStore;
    }

    public void setTaskParser(TaskParser<T> taskParser) {
        this.taskParser = taskParser;
    }
}
