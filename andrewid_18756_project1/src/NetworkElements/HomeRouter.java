package NetworkElements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import DataTypes.*;

public class HomeRouter extends Switch {

	private String address = null;
	private OpticalNIC nic = null;
	
	private Queue<STS1Packet> sendBuffer = new LinkedList<>();
	
	/**
	 * Construct a new  HomeRouter with a given address
	 * @param	address the address of the new HomeRouter
	 */
	public HomeRouter(String address){
		if(address==null || address.equals(""))
			System.err.println("Error (HomeRouter): The router must have an address");
		
		this.address = address;
	}
	
	public void addNIC(OpticalNIC nic){
		this.nic = nic;
	}
	
	/**
	 * create a STS1 Packet  from this router to the linked DXC
	 * @param	payload to put in the buffer
	 */
	public void create(STS1Packet payload){
		
		System.out.println("(HomeRouter) " + address + " has created a STS1Packet to wavelength " + payload.getDest()
				+ "with payload" + payload.getPayload());
		checkSegmentation(payload);
	}
	
	/**
	 * STS1Packet has the length of 5 on its payload
	 * Check the payload length, process segmentation if necessary and create new STS1packets to buffer when necessary
	 * @param	payload to put in the buffer
	 */
	public void checkSegmentation(STS1Packet payload){
		
		
	}
	
	/**
	 * Send out the STS1Packets from buffer
	 */
	public void sendPackets(){
		
	}
	
	/**
	 * Gets the string address of this router
	 * @return	the address of this router
	 */
	public String getAddress(){
		return this.address;
	}
	
}
