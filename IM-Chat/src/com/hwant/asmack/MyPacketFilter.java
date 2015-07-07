package com.hwant.asmack;

import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class MyPacketFilter implements PacketFilter {

	@Override
	public boolean accept(Packet packet) {
		//发消息
		if(packet instanceof Message){
			
		}
		return true;
	}

}
