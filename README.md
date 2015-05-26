openfireLBS
===========

This is Openfire LBS(Location Based Service) plugin based on private protocol as the following but not based on XEP-0080(http://xmpp.org/extensions/xep-0080.html).


XMPP LBS Extension
==

1 Get users near me with my GEO location

REQUEST

    <iq id="c911" type="get" from="chris@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location">
		<item user="chris" lon="22.323009" lat="29.098763"/>
	</query>
    </iq>

RESPONSE

    <iq id="c919" type="result" to="chris@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location">
		<item user="chris1" lon="22.323009" lat="29.098763" sex="0" online="30min"/>
		<item user="chris2" lon="22.323009" lat="29.098763" sex="0" online="30min"/>
		<item user="chris3" lon="22.323009" lat="29.098763" sex="0" online="30min"/>
		... ...
	</query>
    </iq>
    
2 Upload my GEO location 

REQUEST

    <iq id="c912" type="set" from="chris@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location">
		<item user="chris" lon="22.323009" lat="29.098763"/>
	</query>
    </iq>

RESPONSE

    <iq id="c912" type="result" to="chris@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location"/>
    </iq>


3 Share my GEO location to friend 

REQUEST

    <iq id="c913" type="set" from="chris@im.nodexy.com/TCL-S960" to="lena@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location">
		<item user="chris" lon="22.323009" lat="29.098763"/>
	</query>
    </iq>

RESPONSE

    <iq id="c913" type="result" to="chris@im.nodexy.com/TCL-S960" from="lena@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location"/>
    </iq>


WARNING
==
The source code has no functional testing , pls DO NOT use it in your business production.
