package com.fuwei.selecthappylocation.http;

import com.fuwei.selecthappylocation.event.Event;

/**
 * Created by collin on 2015-10-02.
 */
public interface ReqListener {
    void onUpdate(Event event, Object obj);
}
