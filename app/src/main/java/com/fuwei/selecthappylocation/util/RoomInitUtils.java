package com.fuwei.selecthappylocation.util;

import android.content.Context;

import com.fuwei.selecthappylocation.model.IRoom;
import com.fuwei.selecthappylocation.model.RoomDataItem;
import com.fuwei.selecthappylocation.model.RoomViewItem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RoomInitUtils {

    public static List<IRoom> initRooms(Context context, List<RoomDataItem> roomItems) {

        List<IRoom> rooms = new ArrayList<>();
        int length = roomItems.size();

        for (int i = 0; i < length; i++) {
            RoomViewItem rvi = new RoomViewItem(context, roomItems.get(i));
            rooms.add(rvi);
        }

        return rooms;
    }
}
