
import NetworkElements.*;

import java.util.ArrayList;
import java.util.List;

import DataTypes.*;

public class Topology {

	private int time = 0;
	private List<Switch> allSwitch = new ArrayList<Switch>();

	public void twoDXC() {
		/*
		 * Setup the network
		 */
		System.out.println("Setting up two DXCs");

		// Create two SONET DXCs
		SONETDXC DXC1 = new SONETDXC("00:11:22");
		SONETDXC DXC2 = new SONETDXC("88:77:66");
		allSwitch.add(DXC1);
		allSwitch.add(DXC2);

		// tell DXCs a wavelength to add/drop on (in this case their own frequencies)
		DXC1.addDropWavelength(1310);
		DXC2.addDropWavelength(1490);

		// tell DXC 1 the wavelength each DXC is add/dropping on
		DXC1.addDestinationFrequency("00:11:22", 1310);
		DXC1.addDestinationFrequency("88:77:66", 1490);

		// tell DXC 2 the wavelength each DXC is add/dropping on
		DXC2.addDestinationFrequency("00:11:22", 1310);
		DXC2.addDestinationFrequency("88:77:66", 1490);

		// Create an interface for each DXC
		OpticalNIC nicDXC11 = new OpticalNIC(DXC1);
		nicDXC11.setID(11);
		OpticalNIC nicDXC12 = new OpticalNIC(DXC1);
		nicDXC12.setID(12);
		OpticalNIC nicDXC21 = new OpticalNIC(DXC2);
		nicDXC21.setID(21);
		OpticalNIC nicDXC22 = new OpticalNIC(DXC2);
		nicDXC22.setID(22);

		// Create two-uni directional links between the DXCs
		OtoOLink OneToTwo1 = new OtoOLink(nicDXC11, nicDXC21);
		OtoOLink TwoToOne1 = new OtoOLink(nicDXC21, nicDXC11);
		OtoOLink OneToTwo2 = new OtoOLink(nicDXC12, nicDXC22);
		OtoOLink TwoToOne2 = new OtoOLink(nicDXC22, nicDXC12);

		/*
		 * Test question 1: Sent a frame on the network
		 */
		DXC1.create(new STS1Packet(" hello 1490 ", 1490));
		DXC1.create(new STS1Packet(" hello 1310 ", 1310));
		DXC1.create(new STS1Packet(" hello 1490 ", 1490));
		DXC1.create(new STS1Packet(" hello 1490 ", 1490));

		for (int i = 0; i < 10; i++) {
			tock();
		}

		/*
		 * Test Question 2: Link broken with UPSR restoration
		 * 
		 * OneToTwo1.cutLink();
		 */

		/*
		 * Test Question 3: add in the HomeRouter connected to DXC1
		 * The DXC will use STS-3 to aggregate 3 STS-1 packets
		 * The new DXC payload has limited length of 5 characters
		 * Additional characters should be put into new STS1 packet
		 * 
		 * Feel free to create your own test case
		 * 
		 * HomeRouter DXC1hr1 = new HomeRouter("DXC1hr1");
		 * HomeRouter DXC1hr2 = new HomeRouter("DXC1hr2");
		 * HomeRouter DXC1hr3 = new HomeRouter("DXC1hr3");
		 * allSwitch.add(DXC1hr1);
		 * allSwitch.add(DXC1hr2);
		 * allSwitch.add(DXC1hr3);
		 * 
		 * OpticalNIC nicDXC1hr1 = new OpticalNIC(DXC1hr1);
		 * OpticalNIC nichr1DXC1 = new OpticalNIC(DXC1);
		 * OpticalNIC nicDXC1hr2 = new OpticalNIC(DXC1hr2);
		 * OpticalNIC nichr2DXC1 = new OpticalNIC(DXC1);
		 * OpticalNIC nicDXC1hr3 = new OpticalNIC(DXC1hr3);
		 * OpticalNIC nichr3DXC1 = new OpticalNIC(DXC1);
		 * 
		 * OtoOLink Hr1ToDXC1_1 = new OtoOLink(nichr1DXC1, nicDXC1hr1);
		 * OtoOLink Hr1ToDXC1_2 = new OtoOLink(nichr2DXC1, nicDXC1hr2);
		 * OtoOLink Hr1ToDXC1_3 = new OtoOLink(nichr3DXC1, nicDXC1hr3);
		 * 
		 * DXC1hr1.create(new RouterSPE("aaaaab",1490));
		 * DXC1hr2.create(new RouterSPE("cccccd",1490));
		 * DXC1hr3.create(new RouterSPE("eeeeef",1490));
		 * 
		 * output:
		 * (SONETDXC) 88:77:66 has received a STS1 packet at the sink on wavelength 1490
		 * [ Payload:aaaaa, Source:1310 , Delay:1]
		 * (SONETDXC) 88:77:66 has received a STS1 packet at the sink on wavelength 1490
		 * [ Payload:b, Source:1310 , Delay:1]
		 * (SONETDXC) 88:77:66 has received a STS1 packet at the sink on wavelength 1490
		 * [ Payload:ccccc, Source:1310 , Delay:1]
		 * (SONETDXC) 88:77:66 has received a STS1 packet at the sink on wavelength 1490
		 * [ Payload:d, Source:1310 , Delay:1]
		 * (SONETDXC) 88:77:66 has received a STS1 packet at the sink on wavelength 1490
		 * [ Payload:eeeee, Source:1310 , Delay:1]
		 * (SONETDXC) 88:77:66 has received a STS1 packet at the sink on wavelength 1490
		 * [ Payload:f, Source:1310 , Delay:1]
		 * 
		 */

		for (int i = 0; i < 10; i++) {
			tock();
		}

	}

	/**
	 * Time tock, the different time tock will not count to test output
	 * as long as you have the correct STS1 packet information
	 */
	public void tock() {
		System.out.println("** TIME = " + time + " **");
		time++;

		for (int i = 0; i < this.allSwitch.size(); i++)
			allSwitch.get(i).sendPackets();
	}

	public static void main(String args[]) {
		Topology go = new Topology();
		go.twoDXC();
	}
}
