--
-- Table `sign_set`
--
CREATE TABLE `sign_set` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT NOT NULL);

INSERT INTO `sign_set` (`_id`, `name`) VALUES ('1', 'Mandatory Sign');
INSERT INTO `sign_set` (`_id`, `name`) VALUES ('2', 'Warning Sign');
INSERT INTO `sign_set` (`_id`, `name`) VALUES ('3', 'Information Sign');
INSERT INTO `sign_set` (`_id`, `name`) VALUES ('4', 'Route Sign');
INSERT INTO `sign_set` (`_id`, `name`) VALUES ('5', 'Road Marking');
INSERT INTO `sign_set` (`_id`, `name`) VALUES ('6', 'Sign Recognition');

--
-- Table `sign`
--

CREATE TABLE `sign` (`_id` INTEGER PRIMARY KEY AUTOINCREMENT, `sign_set` INTEGER NOT NULL, `description` TEXT NOT NULL, `image` BLOB, FOREIGN KEY(`sign_set`) REFERENCES `sign_set`(`_id`));

INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('1', '1', 'Stop and give way', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('2', '1', 'Give way to traffic on major road or roundabout', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('3', '1', 'No entry for vehicles', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('4', '1', 'No motor vehicles', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('5', '1', 'No trucks', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('6', '1', 'No handcarts', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('7', '1', 'No animal-drawn vehicles', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('8', '1', 'No pedestrians', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('9', '1', 'No rickshaws', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('10', '1', 'No cycles', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('11', '1', 'No tractors or slow-moving vehicles', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('12', '1', 'No vehicles carrying explosives', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('13', '2', 'Crossroads', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('14', '2', 'Major road ahead', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('15', '2', 'Side road right', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('16', '2', 'Staggered junction', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('17', '2', 'T junction', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('18', '2', 'Y junction', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('19', '2', 'Traffic merges from left', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('20', '2', 'Merge with traffic from right', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('21', '2', 'Roundabout', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('22', '2', 'Sharp bend to the right', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('23', '2', 'Hairpin bend to right', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('24', '2', 'Double bend first left', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('25', '3', 'No through road', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('26', '3', 'Pedestrian crossing', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('27', '3', 'Parking place', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('28', '3', 'Filling station', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('29', '3', 'Breakdown service', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('30', '3', 'Telephone', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('31', '3', 'Overnight accommodation', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('32', '3', 'First-aid post', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('33', '3', 'Hospital', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('34', '3', 'Refreshments', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('35', '3', 'Restaurant', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('36', '3', 'Picnic site', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('37', '4', 'Direction sign (National Highways)', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('38', '4', 'Direction sign (Minor Routes)', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('39', '4', 'Diversion sign', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('40', '4', 'Route confirmation sign', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('41', '4', 'Advance direction sign (minor routes)', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('42', '4', 'Advance direction sign (National Highways)', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('43', '4', 'Advance direction sign (alternative style)', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('44', '4', 'Advance direction sign (mounted overhead)', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('45', '5', 'Stop', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('46', '5', 'Single track road', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('47', '5', 'No parking', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('48', '5', 'School', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('49', '5', 'End', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('50', '5', 'One way', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('51', '5', 'Accident', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('52', '5', 'Give way', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('53', '5', 'Road closed', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('54', '5', '40 km/h', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('55', '5', 'Flooding', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('56', '5', 'Time period', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('57', '5', 'Except buses', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('58', '5', 'No entry', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('59', '5', 'Dual carriageway', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('60', '5', 'Single track bridge', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('61', '5', 'Distance over which hazard or restriction extends', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('62', '5', 'Distance', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('63', '5', 'Car', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('64', '5', 'Truck', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('65', '5', 'Bus', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('66', '5', 'Motorbike', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('67', '5', 'Disabled', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('68', '5', 'Baby taxi', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('69', '5', 'Pedal cycle', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('70', '5', 'Rickshaw', NULL);
INSERT INTO `sign` (`_id`, `sign_set`, `description`, `image`) VALUES ('71', '5', 'Arrow to the right', NULL);

--
-- Indexes
--
CREATE INDEX fk_sign_sign_set ON sign(sign_set ASC);
