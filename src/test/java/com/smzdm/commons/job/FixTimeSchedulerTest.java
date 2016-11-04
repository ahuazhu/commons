package com.smzdm.commons.job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengwenzhu on 2016/11/3.
 */
public class FixTimeSchedulerTest {


    static class PrintTaskStore implements TaskStore<DefaultTask> {

        private Map<Integer, DefaultTask> tasks = new HashMap<Integer, DefaultTask>();

        public PrintTaskStore() {
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
        public void store(DefaultTask task) {
            tasks.put(task.getTaskId(), task);
        }


        @Override
        public List<DefaultTask> load() {
            return new ArrayList<DefaultTask>(tasks.values());
        }
    }

    static class PrintTaskParser implements TaskParser<DefaultTask> {

        @Override
        public Runnable parse(final DefaultTask task) {

            return new Runnable() {
                @Override
                public void run() {
                    if ("System.out.println".equals(task.getHandler())) {
                        task.setMessage(task.getArgument());
                        System.out.println(task.getArgument());
                    }
                }

            };
        }
    }

    public void testFixTime() throws IOException {
        FixTimeScheduler scheduler = new FixTimeScheduler();
        scheduler.setJobName("Cycle print");
        scheduler.setTaskParser(new PrintTaskParser());
        scheduler.setTaskStore(new PrintTaskStore());
        scheduler.setIntervalInSecond(1);

        scheduler.start();
        System.in.read();

        scheduler.shutdown();
    }

    public static void main(String[] args) throws IOException {
        new FixTimeSchedulerTest().testFixTime();


    }


}