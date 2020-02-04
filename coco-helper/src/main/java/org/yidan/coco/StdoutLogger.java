package org.yidan.coco;

public class StdoutLogger implements Logger {
    @Override
    public void error(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void error(String message) {
        System.err.println(message);
    }

    @Override
    public void info(String message) {
        System.out.print(message);
    }
}
