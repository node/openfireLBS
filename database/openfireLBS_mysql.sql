INSERT INTO ofVersion (name, version) VALUES ('openfireLBS', 0);

CREATE TABLE `ofLocation` (
  `id` int(11) NOT NULL auto_increment,
  `username` varchar(100) NOT NULL,
  `updatetime` datetime NOT NULL,
  `lon` double NOT NULL,
  `lat` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF-8;

