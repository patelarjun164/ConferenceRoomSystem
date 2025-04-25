package com.hackmech.service;

import com.hackmech.dao.EquipmentDao;
import com.hackmech.dao.RoomDao;
import com.hackmech.model.Equipment;
import com.hackmech.model.Room;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RoomService {
    private final RoomDao roomDao = new RoomDao();
    private final EquipmentDao equipmentDao = new EquipmentDao();

    public void saveOrUpdateRoom(Room room, String[] equipmentIds) {
        Set<Equipment> equipmentSet = new HashSet<>();
        if (equipmentIds != null) {
            for (String eqIdStr : equipmentIds) {
                int eqId = Integer.parseInt(eqIdStr);
                System.out.println(eqId);
                Equipment equipment = equipmentDao.getById(eqId);
                if (equipment != null) {
                    System.out.println(equipment.getName() + " added!");
                    equipmentSet.add(equipment);
                }
            }
        }

        room.setEquipment(equipmentSet);

        if (room.getId() == 0) {
            roomDao.saveRoom(room);
        } else {
            roomDao.updateRoom(room);
        }
    }

    public Room getRoomById(int id) {
        return roomDao.getRoomById(id);
    }

    public List<Room> fetchAllRooms() {
        return roomDao.getAllRooms();
    }

    public void deleteRoom(int id) {
        roomDao.deleteRoom(id);
    }
}
