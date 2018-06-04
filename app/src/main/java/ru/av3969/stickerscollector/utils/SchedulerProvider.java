package ru.av3969.stickerscollector.utils;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler ui();

    Scheduler io();
}
