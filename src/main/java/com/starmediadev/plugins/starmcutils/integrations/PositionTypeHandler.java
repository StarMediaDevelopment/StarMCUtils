package com.starmediadev.plugins.starmcutils.integrations;

import com.starmediadev.data.model.DataTypeHandler;
import com.starmediadev.data.model.DataType;
import com.starmediadev.plugins.starmcutils.util.Position;

public class PositionTypeHandler extends DataTypeHandler<Position> {
    public PositionTypeHandler() {
        super(Position.class, DataType.VARCHAR, 1000);
    }

    public Object serializeSql(Object object) {
        if (object instanceof Position position) {
            return position.getX() + "," + position.getY() + "," + position.getZ() + "," + position.getYaw() + "," + position.getPitch();
        }
        return null;
    }

    public Position deserialize(Object object) {
        if (object instanceof String str) {
            String[] split = str.split(",");
            int x = Integer.parseInt(split[0]);
            int y = Integer.parseInt(split[1]);
            int z = Integer.parseInt(split[2]);
            float yaw = Float.parseFloat(split[3]);
            float pitch = Float.parseFloat(split[4]);
            return new Position(x, y, z, yaw, pitch);
        }
        return null;
    }
}
