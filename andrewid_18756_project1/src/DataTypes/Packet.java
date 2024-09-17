package DataTypes;

import java.util.List;

/**
 * Abstract class Packet
 * It will be implemented as STS1Packet work between HomeRouter and SONETDXC
 * and STS3Packet work between SONETDXCs
 */
public abstract class Packet {
	
	public abstract int getDelay();
	public abstract List<STS1Packet> getPackets();
}
