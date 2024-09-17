package NetworkElements;

import DataTypes.*;

public class OtoOLink {
	private OpticalNIC source=null, dest=null;
	private Boolean linkCut = false;
	
	/**
	 * Constructs a new Optical to Optical link from a source to a destination
	 * @param	source the source the link is connected to
	 * @param	dest the destination the link is connected to
	 */
	public OtoOLink(OpticalNIC source, OpticalNIC dest){	
		if(source == dest)
			System.out.println("Error: You cannot create a link to your own interface");
		if(source == null)
			System.out.println("Error: You cannot create a link that doesn't have a source");
		if(dest == null)
			System.out.println("Error: You cannot create a link that doesn't have a destination");
		
		this.source = source;
		this.dest = dest;
		
		this.source.setOutLink(this);
		this.dest.setInLink(this);
	}
	
	/**
	 * Returns the source NIC for this link. i.e. the only NIC that can put data onto this link
	 * @return	source NIC of this link
	 */
	public OpticalNIC getSource(){
		return this.source;
	}
	
	/**
	 * Returns the destination NIC for this link. i.e. the NIC where data put on this link will be delivered to
	 * @return	destination NIC for this link
	 */
	public OpticalNIC getDest(){
		return this.dest;
	}
	
	/**
	 * Sets that this link has been cut. Making both ends senSe there have been a loss of light
	 * (as SONET sends packets even when there is no data to be sent)
	 * 
	 */
	public void cutLink(){
		this.source.senseLight(false);
		this.dest.senseLight(false);
		this.linkCut = true;
	}
	
	/**
	 * Sets the link as being uncut. i.e. the link is now working fine
	 */
	public void uncutLink(){
		this.source.senseLight(true);
		this.dest.senseLight(true);
		this.linkCut = false;
	}
	
	/**
	 * Sends data from the source NIC joined to this link, to the dest NIC joined to this link
	 * @param	packet the packet to be sent
	 * @param	wavelength the wavelength the packet is sent on
	 * @param	source the source NIC, to ensure the NIC is joined to this link
	 * (non-Javadoc)
	 * @see NetworkElements.OtoOLinkTA#sendData(DataTypes.STS3packet, int, NetworkElements.OpticalNICTA)
	 */
	public void sendData(Packet packet, int wavelength, OpticalNIC source){
		if(dest==null)
			System.err.println("Error (OtoOLink): You tried to send data down a line that doesn't go anywhere");
		else if(this.source != source)
			System.err.println("Error (OtoOLink: You tried to send data down a line you are not connected to");
		else if(this.linkCut)
			System.err.println("Error (OtoOLink: You tried to send data down a line that is cut");
		else{
			this.dest.receiveData(packet, wavelength);
		}
	}
}
