package DataTypes;

import java.util.ArrayList;
import java.util.List;

public class STS3Packet extends Packet {
	
	private int delay = 0;
	private int sourceWavelength = 0;
	private int destWavelength = 0;
	private List<STS1Packet> packets = new ArrayList<>();
	
	/**
	 * One STS-3 Packet will contains Three STS-1 Packet
	 */
	private static int Packetlimit = 3;
	
	/**
	 * Create a new SONET frame to be sent on the network
	 * @param	sts the Packet inside the frame
	 */
	public STS3Packet(STS1Packet sts1,STS1Packet sts2,STS1Packet sts3 ){
		this.packets.add(sts1);
		this.packets.add(sts2);
		this.packets.add(sts3);
	}

	/**
	 * Gets the STS1 packets out of this frame
	 * @return	the STS1 packets inside this frame
	 */
	public List<STS1Packet> getPackets(){
		return this.packets;
	}
	
	/**
	 * Returns the total amount of delay this frame has experienced during it's travel
	 * @return	 the total delay this frame experienced
	 */
	public int getDelay(){
		return this.delay;
	}

	/**
	 * Increases the delay that this frame has encountered during it's travel
	 * @param	delay the additional delay to be added
	 */
	public void addDelay(int delay){
		
	}
}
