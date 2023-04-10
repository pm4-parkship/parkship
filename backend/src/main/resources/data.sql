INSERT  INTO  "user"("name","surname") VALUES ('Robin','Meier');
INSERT INTO "user"("name", "surname") VALUES ('John', 'Doe');
INSERT INTO "user"("name", "surname") VALUES ('Jane', 'Smith');
INSERT INTO "user"("name", "surname") VALUES ('Michael', 'Brown');
INSERT INTO "user"("name", "surname") VALUES ('Sarah', 'Jones');
INSERT INTO "user"("name", "surname") VALUES ('Alexander', 'Smith');
INSERT INTO "user"("name", "surname") VALUES ('Lisa', 'Miller');
INSERT INTO "user"("name", "surname") VALUES ('Daniel', 'Williams');
INSERT INTO "user"("name", "surname") VALUES ('Emily', 'Brown');
INSERT INTO "user"("name", "surname") VALUES ('Joshua', 'Hernandez');

INSERT INTO "parking_lot"("address","address_nr","description","floor","latitude","longitude","nr","price","state","user_id") VALUES('Pearl Street','4','In the shade, next to the main door',3,4.0,6.0,'5',3.0,'Free',1);
INSERT INTO "parking_lot"("address", "address_nr", "description", "floor", "latitude", "longitude", "nr", "price", "state", "user_id") VALUES ('Main Street', '10', 'Near the entrance gate', 1, 3.0, 5.0, '7', 150.0, 'Available', FLOOR(RAND() * 10) + 1);
INSERT INTO "parking_lot"("address", "address_nr", "description", "floor", "latitude", "longitude", "nr", "price", "state", "user_id") VALUES ('Park Avenue', '20', 'Close to the elevator', 2, 4.0, 6.0, '12', 250.0, 'Occupied', FLOOR(RAND() * 10) + 1);
INSERT INTO "parking_lot"("address", "address_nr", "description", "floor", "latitude", "longitude", "nr", "price", "state", "user_id") VALUES ('Green Street', '30', 'Next to the stairs', 3, 5.0, 7.0, '18', 200.0, 'Available', FLOOR(RAND() * 10) + 1);
INSERT INTO "parking_lot"("address", "address_nr", "description", "floor", "latitude", "longitude", "nr", "price", "state", "user_id") VALUES ('Elm Street', '40', 'In the corner', 4, 6.0, 8.0, '24', 300.0, 'Occupied', FLOOR(RAND() * 10) + 1);


INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-05', '2023-04-06', 1, 1);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-06', '2023-04-07', 2, 2);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-07', '2023-04-08', 3, 3);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-08', '2023-04-09', 4, 4);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-09', '2023-04-10', 5, 5);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-10', '2023-04-11', 1, 6);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-11', '2023-04-12', 2, 7);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-12', '2023-04-13', 3, 8);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-13', '2023-04-14', 4, 9);
INSERT INTO "reservation" ("from", "to", "parking_lot_id", "user_id") VALUES ('2023-04-14', '2023-04-15', 5, 10);
