package com.smzdm.commons.job;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengwenzhu on 2016/11/3.
 */
public class FixTimeSchedulerTest {


    static class PrintTaskStore implements TaskStore {

        private Map<Integer, Task> tasks = new HashMap<Integer, Task>();

        public PrintTaskStore() {
//            private int taskId;
//            private String taskName;
//            private String handler;
//            private String argument;
//            private String taskType;
//            private String status;
//            private long nextTime;
//            private int retriedTimes;
//            private int maxRetryTimes;
//            private String message;
//            private long lastTime;
//            private long maxInterval;
            DefaultTask task1 = new DefaultTask();
            task1.setTaskId(1);
            task1.setTaskName("task1");
            task1.setHandler("System.out.println");
            task1.setArgument("count");
            task1.setTaskType(DefaultTask.Type.FIXED_RETRY.name());
            task1.setMaxInterval(1500);
            task1.setMaxRetryTimes(5);

            tasks.put(1, task1);
        }

        @Override
        public void store(Task task) {
            tasks.put(task.getTaskId(), task);
        }

        @Override
        public List<Task> load() {
            return new ArrayList<Task>(tasks.values());
        }
    }

    static class PrintTaskParser implements TaskParser {

        @Override
        public Runnable parse(final Task task) {

            return new Runnable() {
                @Override
                public void run() {
                    if (task instanceof DefaultTask) {
                        DefaultTask t = (DefaultTask) task;
                        if ("System.out.println".equals(t.getHandler())) {
                            t.setMessage(t.getArgument());
                            System.out.println(t.getArgument());
                        }
                    }

                }
            };
        }
    }

    @Test
    public void testFixTime() throws IOException {
        FixTimeScheduler scheduler = new FixTimeScheduler();
        scheduler.setJobName("Cycle print");
        scheduler.setTaskParser(new PrintTaskParser());
        scheduler.setTaskStore(new PrintTaskStore());
        scheduler.setIntervalInSecond(1);

        scheduler.start();
//        //
//            private String jobName = "FixedTimeScheduler";
//        private TaskExecutor taskExecutor = new DefaultTaskExecutor();
//        private int intervalInMinutes;
//        private long nextScheduleTimestamp;
//        private TaskStore taskStore;
//        private TaskParser taskParser;
//        private volatile boolean shutdown = false;


        System.in.read();

        scheduler.shutdown();
    }

    public static void main(String[] args) throws IOException {
        new FixTimeSchedulerTest().testFixTime();


    }


}