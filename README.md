openfireLBS
===========

This is Openfire LBS plugin.


XMPP LBS Extension
==

REQUEST

    <iq id="c919" type="get" from="chris@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location">
		<item user="chris" lon="22.323009" lat="29.098763"/>
	</query>
</iq>



RESPONSE

    <iq id="c919" type="result" from="chris@im.nodexy.com/TCL-S960">
	<query xmlns="com.nodexy.im.openfire.location">
		<item user="chris1" lon="22.323009" lat="29.098763" sex="0" online="30min"/>
		<item user="chris2" lon="22.323009" lat="29.098763" sex="0" online="30min"/>
		<item user="chris3" lon="22.323009" lat="29.098763" sex="0" online="30min"/>
		... ...
	</query>
</iq>



WARNING
==
The source code has no functional testing , pls DO NOT use it in your business production.
