package com.nodexy.im.openfire.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.Element;
import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.openfire.IQHandlerInfo;
import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.auth.UnauthorizedException;
import org.jivesoftware.openfire.handler.IQHandler;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;
import org.xmpp.packet.PacketError;

/**
 * Location IQHandler
 * 
 * @author Chris, node@github
 * 
 */
public class LocationHandler extends IQHandler {

	private static final String NAME_SPACE = "com.nodexy.im.openfire.location";
	private static final String SQL_UPDATE_LOCATION = "INSERT INTO `ofLocation` (`username`, `updatetime`, `lon`, `lat`) VALUES (?,NOW(), ?,?);";
	private static final double LON_DELTA = 0.0009; // ~ 100m
	private static final double LAT_DELTA = 0.0009; // ~ 100m 
	private static final String SQL_USERS_NEARME = String.format("select * from ofLocation where (abs(lon-?)<= %f ) AND (abs(lat-? <= %f);" 
				,LON_DELTA,LAT_DELTA);

	private IQHandlerInfo info;
	private Connection openfireConn;

	public LocationHandler(String moduleName) {
		super(moduleName);
		try {
			this.openfireConn = DbConnectionManager.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		};
		info = new IQHandlerInfo(moduleName, NAME_SPACE);
	}

	@Override
	public IQHandlerInfo getInfo() {
		return info;
	}

	@Override
	public IQ handleIQ(IQ packet) throws UnauthorizedException {
		
		System.out.println(">>>>>>>>>>>>> RECV IQ: " + packet.toXML()); // XXX
		
		// get users near me(from JID)
		if (IQ.Type.get.equals(packet.getType())) {
			return getUsersNearme(packet);
		// set from JID's location to ...
		} else if (IQ.Type.set.equals(packet.getType())) {
			JID to = packet.getTo();
			
			// send from JID's location to to JID
			if (to.getNode() != null && !to.getNode().equals("")){  
				XMPPServer.getInstance().getIQRouter().route(packet); // route to another user 
				
				return IQ.createResultIQ(packet);
			// send from JID's location to server , and update ofLocation  
			}else{ 
				return updateLocation(packet);
			}
		} else {
			IQ reply = IQ.createResultIQ(packet);
			reply.setType(IQ.Type.error);
			reply.setError(PacketError.Condition.bad_request);
			return reply;
		}
	}
	
	/**
	 * 
	 * 
	 * @param packet
	 * @return
	 */
	private IQ getUsersNearme(IQ packet) {

		IQ reply = IQ.createResultIQ(packet);
		
		JID from = packet.getFrom();
		
		Element iq = packet.getChildElement();
		Element item = iq.element("item");
		Double myLon = Double.parseDouble(item.attributeValue("lon"));
		Double myLat = Double.parseDouble(item.attributeValue("lat"));
		
		// XXX: update user location firstly 
		insertLocation(myLon,myLat,from.getNode());
		
		// find users near me 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = openfireConn.prepareStatement(SQL_USERS_NEARME);
			pstmt.setDouble(1, myLon);
			pstmt.setDouble(2, myLon);
			pstmt.setDouble(3, myLat);
			pstmt.setDouble(4, myLat);
			rs = pstmt.executeQuery();
			String username = null;
			double nearLon = 0;
			double nearLat = 0;
			while (rs.next()) {  
				username = rs.getString("username");
				nearLon = rs.getDouble("lon");
				nearLat = rs.getDouble("lat");
				Element e = iq.addElement("item");
				e.addAttribute("user", username);
				e.addAttribute("lon", Double.toString(nearLon));
				e.addAttribute("lat", Double.toString(nearLat));
			}
			reply.setChildElement(iq);
		} catch (SQLException e1) {
			reply.setType(IQ.Type.error);
			reply.setError(PacketError.Condition.internal_server_error);
			e1.printStackTrace();
		}
		return reply;
	}

	private IQ updateLocation(IQ packet) {
		IQ reply = IQ.createResultIQ(packet);

		Element iq = packet.getChildElement();
		JID from = packet.getFrom();
		String username = from.getNode();

		Element item = iq.element("item");
		Double myLon = Double.parseDouble(item.attributeValue("lon"));
		Double myLat = Double.parseDouble(item.attributeValue("lat"));

		boolean f = insertLocation(myLon,myLat,username);
		if (f){
			// reply.setChildElement(iq);
		}else{
			reply.setType(IQ.Type.error);
			reply.setError(PacketError.Condition.internal_server_error);
		}
		
		return reply;
	}
	
	private boolean insertLocation(Double myLon, double myLat, String username){
		boolean f = false;
		PreparedStatement pstmt = null;
		try {
			pstmt = openfireConn.prepareStatement(SQL_UPDATE_LOCATION);
			pstmt.setDouble(1, myLon);
			pstmt.setDouble(2, myLat);
			pstmt.setString(3, username);
			pstmt.executeUpdate();

			f = true;
		} catch (SQLException e1) {
			f = false;
			e1.printStackTrace();
		}
		return f;
	}

}
