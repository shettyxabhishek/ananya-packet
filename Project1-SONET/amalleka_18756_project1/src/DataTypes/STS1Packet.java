package DataTypes;

import java.util.List;
import NetworkElements.*;

public class STS1Packet extends Packet{

	private String payload = null;
	private int delay = 0;
	private int destWavelength = 0;
	private int sourceWavelength = 0;
	private OpticalNIC nic = null;
	
	/**
	 * Assume one STS1 packet will have payload of a string length 5
	 */
	private static int payloadLengthLimit = 5;
	
	public STS1Packet(String payload, int destWavelength){
		this.payload = payload;
		this.destWavelength = destWavelength;
	}
	
	public String getPayload(){
		return payload;
	}
	
	public int getDest(){
		return destWavelength;
	}
	
	public void setSource(int sourceWavelength){
		this.sourceWavelength = sourceWavelength;
	}
	
	public int getSource(){
		return this.sourceWavelength;
	}
	
	/**
	 * Increases the delay that this SPE has encountered during it's travel
	 * @param	delay the additional delay to be added
	 */
	public void addDelay(int delay){
		
	}
	
	/**
	 * Returns the total amount of delay this SPE has experienced during it's travel
	 * @return	 the total delay this SPE experienced
	 */
	public int getDelay(){
		return this.delay;
	}
	
	/**
	 * Returns a clone of this SPE object
	 */
	public STS1Packet clone(){ return null; }
	
	public String toString(){
		return "[ Payload:" + this.payload + ", Source:" + this.sourceWavelength 
						+ ", Delay:" + this.delay + "]";
	}

	
	/**
	 * There method is only for STS3 Packet
	 */
	@Override
	public List<STS1Packet> getPackets() {
		// TODO Auto-generated method stub
		return null;
	}
}
