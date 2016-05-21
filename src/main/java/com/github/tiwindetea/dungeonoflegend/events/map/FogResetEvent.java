package com.github.tiwindetea.dungeonoflegend.events.map;

/**
 * Created by maxime on 5/20/16.
 */
public class FogResetEvent extends MapEvent {

	public FogResetEvent() {
	}

    @Override
    public MapEventType getSubType() {
        return MapEventType.FOG_RESET_EVENT;
    }
}
