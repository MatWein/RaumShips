package com.mw.raumships.common.entities;

public interface PuddleJumperEntityConstants {
    int ID = 1001;
    String NAME = "PuddleJumper";

    int RENDER_DISTANCE = 100;
    float THIRD_PERSON_DISTANCE = 8.5F;
    float COCKPIT_CAMERA_Z_OFFSET = -1.5F;

    float BOUNDING_BOX_WIDTH = 2.5F;
    float BOUNDING_BOX_HEIGHT = 2.5F;
    int MAX_PASSANGERS = 2;

    float FINAL_AIRSHIP_SPEED_TURN = 0.18F;
    float FINAL_AIRSHIP_SPEED_FORWARD = 0.04F;
    float FINAL_AIRSHIP_SPEED_UP = 0.004F;
    float FINAL_AIRSHIP_SPEED_DOWN = 0.004F;
    float SPEED_MODIFIER = 0.02F;
    float MOMENTUM = 0.9F;
}
