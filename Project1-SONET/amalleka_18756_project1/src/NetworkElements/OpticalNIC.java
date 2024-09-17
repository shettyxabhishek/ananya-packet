package NetworkElements;

import java.util.ArrayList;
import java.util.List;

import DataTypes.*;

public class OpticalNIC {
	private Switch parent = null;
	private OtoOLink inLink = null, outLink = null;
	private Boolean trace = true;
	private Boolean isOnRing = true;
	private int id = -1;
	private OpticalNIC workingNIC = null;
	private	OpticalNIC protectionNIC = null;
	private Boolean hasError = false;
	private Boolean isClockwise = false;
	
	/**
	 * Creates a new Optical NIC
	 * @param	parent the parent router for this NIC
	 */
	public OpticalNIC(Switch parent){
		if(parent==null)
			System.err.println("Error: You cannot create an interface with no parent");
		
		this.parent = parent;
		this.parent.addNIC(this);
		
	}
	
	/**
	 * Set the incoming optical link to this optical NIC
	 * @param	inLink the incoming optical link
	 */
	public void setInLink(OtoOLink inLink){
		if(inLink.getSource() != this && inLink.getDest() == this)
			this.inLink = inLink;
		else
			System.err.println("Error (OpticalNIC): You tried to assign an Optical in link that was not created for this interface");
	}
	
	/**
	 * Set the outgoing optical link to this optical NIC
	 * @param	outLink the outgoing optical link
	 */
	public void setOutLink(OtoOLink outLink){
		if(outLink.getSource() == this && outLink.getDest() != this)
			this.outLink = outLink;
		else
			System.err.println("Error (OpticalNIC): You tried to assign an Optical out link that was not created for this interface");
	}
	
	/**
	 * Sets the ID for this optical NIC. Each NIC in a router should have a unique ID so that it can
	 * be distinguished from other NICs in the router
	 * @param	id the id of this NIC
	 */
	public void setID(int id){
		this.id = id;
	}
	
	/**
	 * Returns the ID of this NIC
	 * @return	ID of the NIC
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * Returns the router that this NIC is in
	 * @return parent NIC
	 */
	public Switch getParent(){
		return this.parent;
	}
	
	/**
	 * Set if this NIC goes in a clockwise direction. Could be one way of telling the shortest path since
	 * we know the network design
	 * @param	clockwise does the NIC go clockwise
	 */
	public void setClockwise(Boolean clockwise){
		this.isClockwise = clockwise;
	}
	
	/**
	 * Returns whether this NIC goes in a clockwise direction
	 * @return	does this NIC go clockwise
	 */
	public Boolean getIsClockwise(){
		return this.isClockwise;
	}
	
	/**
	 * Set of this NIC is on a SONET ring. If the NIC is part of the ring wavelengths will be forwarded on
	 * it regardless of the wavelength destination
	 * @param	isOnRing is this NIC on a SONET ring
	 */
	public void setIsOnRing(Boolean isOnRing){
		this.isOnRing = isOnRing;
	}
	
	/**
	 * Returns whether or no this NIC is part of a SONET ring. If it is part of a ring then all wavelengths
	 * should be forwarded onto this NIC
	 * @return	is this NIC on a SONET ring
	 */
	public Boolean getIsOnRing(){
		return this.isOnRing;
	}
	
	/**
	 * Sets whether or not this NIC is a protection NIC for a working NIC
	 * @param	workingNIC The working NIC for this backup NIC
	 */
	public void setIsProtection(OpticalNIC workingNIC){
		if(this.parent != workingNIC.parent)
			System.err.println("Error (OpticalNIC): The working NIC and protection must be in the same router");
		
		this.workingNIC = workingNIC;
	}
	
	/**
	 * Returns the working NIC of this NIC, or null if this card is not a protection NIC
	 * @return	the working NIC for this protection NIC
	 */
	public OpticalNIC getWorkingNIC(){
		return this.workingNIC;
	}
	
	/**
	 * Sets whether or not this NIC has a protection NIC
	 * @param	protectionNIC The protection NIC for this working NIC
	 */
	public void setIsWorking(OpticalNIC protectionNIC){
		if(this.parent != protectionNIC.parent)
			System.err.println("Error (OpticalNIC): The working NIC and protection must be in the same router");
		
		this.protectionNIC = protectionNIC;
	}
	
	/**
	 * Returns the protection NIC of this NIC, or null if this card does not have a protection NIC
	 * @return	the protection NIC for this working NIC
	 */
	public OpticalNIC getProtectionNIC(){
		return this.protectionNIC;
	}
	
	/**
	 * Sets whether or not this NIC is in an error state for some reason
	 * @param	hasError does this NIC have an error
	 */
	public void setHasError(Boolean hasError){
		System.out.println("(OpticalNIC) " + this.parent.getAddress() + " " + this.getID() + ": Set error state: " + hasError);
		this.hasError = hasError;
	}
	
	/**
	 * Gets whether or not this NIC is in an error state
	 * @return	whether this NIC is in an error state
	 */
	public Boolean getHasError(){
		return this.hasError;
	}
	
	/**
	 * Tells this NIC if it is sensing light through the cable
	 * @param	light does the link have light
	 */
	public void senseLight(Boolean light){
		if(light==true)
			this.hasError = false;
		else
			this.hasError = true;
	}
	
	/**
	 * Sends a packet to the link that is joined to the output of this NIC
	 * @param	packet the packet to be sent
	 * @param	wavelength the wavelength the packet will be sent on
	 */
	public Boolean sendPacket(Packet packet, int wavelength){
		if(packet instanceof STS3Packet && packet.getPackets().size()!=3){
			System.err.println("Error (OpticalNIC): you are trying to send a DXC Packet not in 3 STS1packets");
			return false;
		}
		
		if(this.outLink == null){
			System.err.println("Error (OpticalNIC): you are trying to send a frame on an interface with no out connection:");
			return false;
		}
		if(this.getHasError()){
			System.err.println("Error (OpticalNIC): you are trying to send a frame on an interface that is in an error state");
			return false;
		}
		else{
			outLink.sendData(packet, wavelength, this);
			return true;
		}
	}
	
	/**
	 * This method is run when a packet is received from the input link joined to this NIC. It is then
	 * passed to the SONET router for routing
	 * @param	packet the frame received
	 * @param	wavelength the wavelength it was received on
	 */
	public void receiveData(Packet packet, int wavelength){
		if(this.trace)
			System.out.println("Trace (OpticalNIC): router:" + this.parent.getAddress() + " NICid: " + this.getID() + " received data");
	
		if(this.trace)
		System.out.println("Trace (OpticalNIC): " + this.parent.getAddress() + " has received a Packet on " + wavelength
				+ " packet delay: " + packet.getDelay());
		
		this.parent.receivePackets(packet, wavelength, this);
	}
}
