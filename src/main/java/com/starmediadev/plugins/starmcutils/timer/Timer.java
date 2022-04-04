package com.starmediadev.plugins.starmcutils.timer;

import com.starmediadev.plugins.starmcutils.updater.UpdateType;
import com.starmediadev.plugins.starmcutils.util.ReturnableCallback;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Timer {
    private static List<Timer> timers = new ArrayList<>();
    
    public Map<UpdateType, Long> lastUpdates = new HashMap<>();
    private ReturnableCallback<TimerSnapshot, Boolean> callback;
    private boolean cancelled = false;
    private long length = -1;
    private long paused = -1;
    private boolean running = false;
    private long time;

    public Timer(ReturnableCallback<TimerSnapshot, Boolean> callback) {
        this.callback = callback;
        timers.add(this);
    }

    public static void startTimerUpdater(JavaPlugin plugin) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
            @Override
            public synchronized void run() {
                Iterator<Timer> iterator = timers.iterator();
                while (iterator.hasNext()) {
                    Timer timer = iterator.next();
                    if (timer.isCancelled()) {
                        iterator.remove();
                        continue;
                    }
                    
                    if (timer.isPaused()) {
                        continue;
                    }
                    
                    if (timer.getTimeLeft() <= 0) {
                        continue;
                    }
                    
                    long currentTime = System.currentTimeMillis();

                    List<UpdateType> types = new ArrayList<>();
                    for (UpdateType type : UpdateType.values()) {
                        long last = timer.lastUpdates.getOrDefault(type, 0L);
                        if (currentTime - last >= type.getLength()) {
                            timer.lastUpdates.put(type, currentTime);
                            types.add(type);
                        }
                    }
                    timer.count();
    
                    TimerSnapshot timerSnapshot = new TimerSnapshot(timer, timer.time, types.toArray(new UpdateType[0]));
                    Boolean value = timer.getCallback().callback(timerSnapshot);
                    if (!value) {
                        iterator.remove();
                    }
                }
            }
        }, 0L, 1L);
    }
    
    public static List<Timer> getTimers() {
        return timers;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }

    public void count() {
        if (!running) {
            return;
        }
        time -= 50;
    }
    
    public ReturnableCallback<TimerSnapshot, Boolean> getCallback() {
        return callback;
    }
    
    public static String formatTime(int time) {
        final int minutes = time / 60;
        time -= minutes * 60;
        String s = "";
        if (minutes < 10) {
            s = s + "0" + minutes + "m";
        } else if (minutes >= 10) {
            s = s + minutes + "m";
        }
        if (time < 10 && time >= 0) {
            s = s + "0" + time + "s";
        } else if (time >= 10) {
            s = s + time + "s";
        }
        return s;
    }

    public static String formatLongerTime(int time) {
        int minutes = (time / 60);
        time -= minutes * 60;
        int hours = (minutes / 60);
        minutes -= hours * 60;
        return (hours > 0 ? (hours < 10 ? "0" : "") + hours + "h" : "") + (minutes < 10 ? "0" : "") + minutes + "m" + (time < 10 ? "0" : "") + time + "s";
    }

    public static String formatTimeShort(long seconds) {
        long mins = seconds / 60L;
        long secs = seconds % 60L;
        String text = "";
        if (mins > 0L) {
            text = text + mins + "m";
        }
        if (secs > 0L) {
            text = text + secs + "s";
        }
        return text;
    }

    public void reset() {
        setLength(length);
    }
    
    public boolean isPaused() {
        return this.paused > -1;
    }

    public void setLength(long l) {
        this.length = l;
        this.time = l;
    }
    
    public void addLength(long l) {
        this.length += l;
        this.time += l;
    }
    
    public void setTime(long l) {
        if (l > this.length) {
            throw new IllegalArgumentException("The new time cannot be greather than the length");
        }
        
        this.time = l;
    }
    
    public void addTime(long l) {
        setTime(this.time + l);
    }
    
    //TODO remove time and length

    public void setPaused(boolean paused) {
        if (!paused) {
            setLength(getTimeLeft());
            this.paused = -1;
        } else {
            this.paused = System.currentTimeMillis();
        }
        running = !paused;
    }

    public long getTimeLeft() {
        return time;
    }

    public Timer run(long length) {
        setLength(length);
        run();
        return this;
    }

    public Timer run() {
        if (length < 0) {
            throw new NullPointerException("There is no length for the timer");
        }
        running = true;
        return this;
    }

    public int getSecondsElapsed() {
        return toSeconds(getTimeElapsed());
    }

    public static int toSeconds(long milliseconds) {
        return (int) Math.floor(milliseconds / 1000.0);
    }

    public long getTimeElapsed() {
        return length - time;
    }

    public boolean hasElapsed() {
        return hasElapsed(this.length);
    }

    public boolean hasElapsed(long length) {
        return getTimeElapsed() >= length;
    }

    public boolean isRunning() {
        return running;
    }

    public int getSecondsLeft() {
        return toSeconds(getTimeLeft());
    }

    public long getLength() {
        return length;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
