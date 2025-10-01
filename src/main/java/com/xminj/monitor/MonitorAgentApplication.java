package com.xminj.monitor;


import io.micronaut.context.annotation.Mixin;
import io.micronaut.runtime.Micronaut;

public class MonitorAgentApplication {
    //@Mixin()
    public static void main(String[] args) {
          Micronaut.run(MonitorAgentApplication.class);
    }
}
