import { User } from 'pages/api/user';
import { useEffect, useState } from 'react';
import { logger } from 'src/logger';
import { ParkingLotModel } from 'src/models';

const fetchMyParkingLotReservations = async (
  user: User
): Promise<ParkingLotModel[]> => {
  return await fetch('/backend/reservations', {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${user.token}`
    }
  }).then(async (response) => {
    if (response.ok) {
      const data = await response.json();
      logger.log(data);
      return data;
    }
  });
};

const MyParkingLotsPage = ({ user }) => {

  const [myParkingLotReservations, setMyParkingLotReservations] = useState<ParkingLotModel[]>([]);

  useEffect(() => {
    fetchMyParkingLotReservations(user)
      .then((result: ParkingLotModel[]) => {
        //const myResults = result?.filter( reservation => reservation.owner.email == user.username);
        //logger.log(myResults);
        setMyParkingLotReservations(result);
        logger.log("result: ", result);
      })
      .catch((error) => logger.log('error'));
  }, []);

  return (
    <>
    <div>hello</div>
      {myParkingLotReservations?.map((reservation) => {
        return <div key={reservation.id}>{reservation.id}</div>;
      })}
    </>
  );
};

export default MyParkingLotsPage;
