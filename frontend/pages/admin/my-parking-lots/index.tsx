import { User } from 'pages/api/user';
import { useEffect, useState } from 'react';
import { logger } from 'src/logger';
import { ParkingLotModel } from 'src/models';
import TableComponent from '../../../src/components/table/table-component';
import { RowDataType } from 'src/components/table/table-row';
import { formatDate } from 'src/date/date-formatter';
import { ParkingLotReservationModel } from 'src/models/parking-lot-reservations/parking-lot-reservations.model';

const fetchMyParkingLotReservations = async (
  user: User
): Promise<ParkingLotReservationModel> => {
  return await fetch('/backend/parking-lot/reservations', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      return data;
    }
  });
};

const headerNames = [
  'ID',
  'Parkplatzname',
  'Ort',
  'reserviert von',
  'gebucht von - bis',
  'Status'
];

const MyParkingLotsPage = ({ user }) => {
  const [test, setTest] = useState<RowDataType[]>([]);

  useEffect(() => {
    fetchMyParkingLotReservations(user)
      .then((result: ParkingLotReservationModel) => {
        logger.log('result: ', result);

        // cast to RowDataType
        const mappedResult: Array<RowDataType> = result.past.map((item) => {
          return [
            `${item.id}`,
            `${item.parkingLot.name}`,
            `${item.tenant.name}`,
            `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
            `${formatDate(new Date(item.from))} - ${formatDate(
              new Date(item.to)
            )}`,
            `${item.reservationState}`
          ];
        });

        const mapped2Result: Array<RowDataType> =  result.current.map((item) => {
          return [
            `${item.id}`,
            `${item.parkingLot.name}`,
            `${item.tenant.name}`,
            `${item.parkingLot.address} ${item.parkingLot.addressNr}`,
            `${formatDate(new Date(item.from))} - ${formatDate(new Date(item.to))}`,
            `${item.reservationState}`
          ];
        });

        setTest(mapped2Result.concat(mappedResult));

        logger.log("test: ", test);
      })
      .catch((error) => logger.log(error));
  }, []);

  return (
    <>
      <TableComponent
        data={test}
        headerNames={headerNames}
        onRowClick={(e) => logger.log(e)}
      ></TableComponent>
      <div>hello</div>
    </>
  );
};

export default MyParkingLotsPage;
