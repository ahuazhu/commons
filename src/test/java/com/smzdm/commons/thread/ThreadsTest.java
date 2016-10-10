package com.smzdm.commons.thread;

import static org.junit.Assert.*;

/**
 * Created by zhengwenzhu on 16/10/9.
 */
public class ThreadsTest {

    @org.junit.Test
    public void testForGroup() throws Exception {
        Threads.forGroup("Test").start(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (;;) {
                    System.out.println("Hi " + i ++);
                }
            }
        });
    }
}