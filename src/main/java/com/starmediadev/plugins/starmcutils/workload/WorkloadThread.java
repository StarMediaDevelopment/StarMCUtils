package com.starmediadev.plugins.starmcutils.workload;

import com.google.common.collect.Queues;
import com.starmediadev.nmswrapper.NMS;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayDeque;

public class WorkloadThread extends BukkitRunnable {
    
    private final ArrayDeque<Workload> workloadDeque;
    private NMS nms;
    
    public WorkloadThread(NMS nms) {
        workloadDeque = Queues.newArrayDeque();
        this.nms = nms;
    }
    
    public void addWorkload(Workload workload) {
        this.workloadDeque.add(workload);
    }
    
    @Override
    public void run() {
        float averageTickTime = nms.getAverageTickTime();
        while (!workloadDeque.isEmpty() && averageTickTime < 35) {
            workloadDeque.poll().compute();
        }
    }
    
    public WorkloadThread start(JavaPlugin plugin) {
        this.runTaskTimer(plugin, 0L, 1L);
        return this;
    }
}
