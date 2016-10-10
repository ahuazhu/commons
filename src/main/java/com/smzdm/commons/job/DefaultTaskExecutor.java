package com.smzdm.commons.job;

import com.smzdm.commons.thread.Threads;

/**
 * Created by zhengwenzhu on 16/10/10.
 */
public class DefaultTaskExecutor implements TaskExecutor {
    @Override
    public void execute(TaskContext taskContext) {
        Threads.forPool().getCachedThreadPool("TaskExecutor").execute(new ProxyTask(taskContext));
    }

    static class ProxyTask implements Runnable {
        private TaskContext taskContext;
        ProxyTask(TaskContext taskContext) {
            this.taskContext = taskContext;
        }

        @Override
        public void run() {
            Task task = taskContext.getTask();
            TaskParser taskParser = taskContext.getTaskParser();
            TaskStore taskStore = taskContext.getTaskStore();
            try {
                Runnable target = taskParser.parse(task);
                target.run();
                task.success();
            } catch (Exception e) {
                task.fail(e.getMessage());
            } finally {
                task.triedOnce();
                taskStore.store(task);
            }
        }
    }
}
