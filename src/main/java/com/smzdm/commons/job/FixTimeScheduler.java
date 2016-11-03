package com.smzdm.commons.job;

import java.util.List;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class FixTimeScheduler {
    public static final int ONE_SECOND = 1000;
    public static final int MILLIS_OF_ONE_SECOND = 1000;

    private String jobName = "FixedTimeScheduler";

    private TaskExecutor taskExecutor = new DefaultTaskExecutor();

    private long intervalInSecond;

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
                        nextScheduleTimestamp = nextTime();
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

    public void setTaskStore(TaskStore taskStore) {
        this.taskStore = taskStore;
    }

    public void setTaskParser(TaskParser taskParser) {
        this.taskParser = taskParser;
    }
}
