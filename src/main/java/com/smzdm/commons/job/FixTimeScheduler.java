package com.smzdm.commons.job;

import java.util.List;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class FixTimeScheduler {
    public static final int ONE_SECOND = 1000;
    public static final int MILLIS_OF_ONE_MIN = 60 * 1000;

    private String jobName = "FixedTimeScheduler";

    private TaskExecutor taskExecutor = new DefaultTaskExecutor();

    private int intervalInMinutes;

    private long nextScheduleTimestamp;

    private TaskStore taskStore;
    private TaskParser taskParser;
    private volatile boolean shutdown = false;


    public FixTimeScheduler() {
        nextScheduleTimestamp = System.currentTimeMillis();
    }


    public void start() {
        new Thread(new Runnable() {
            public void run() {
                while (!shutdown) {
                    if (System.currentTimeMillis() > nextScheduleTimestamp) {
                        List<Task> tasks = taskStore.load();
                        for (Task task : tasks) {
                            taskExecutor.execute(new TaskContext(task, taskStore, taskParser));
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

    private long nextTIme() {
        return nextScheduleTimestamp + intervalInMinutes * MILLIS_OF_ONE_MIN;
    }


    public int getIntervalInMinutes() {
        return intervalInMinutes;
    }

    public void setIntervalInMinutes(int intervalInMinutes) {
        this.intervalInMinutes = intervalInMinutes;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setTaskStore(TaskStore taskStore) {
        this.taskStore = taskStore;
    }

    public void setTaskParser(TaskParser taskParser) {
        this.taskParser = taskParser;
    }
}
