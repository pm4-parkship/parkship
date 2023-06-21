import { Grid } from '@mui/material';
import Typography from '@mui/material/Typography';
import { TextFieldElement } from 'react-hook-form-mui';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import dynamic from 'next/dynamic';
import { StepProps } from './create-parking-lot-stepper';

const Step2 = ({ navigation, parkingLot, handleNext }: StepProps) => {
  const [coords, setCoords] = useState([
    parkingLot.latitude,
    parkingLot.longitude
  ]);

  const formSchema = z.object({
    address: z.string().min(1, `Bitte geben Sie eine Adresse ein!`),
    addressNr: z.string().optional()
  });

  const { handleSubmit, control } = useForm({
    resolver: zodResolver(formSchema),
    mode: 'onSubmit',
    defaultValues: {
      address: parkingLot.address,
      addressNr: parkingLot.addressNr
    }
  });
  const Map = dynamic(() => import('./parking-lot-create-map'), {
    ssr: false
  });

  return (
    <form
      onSubmit={handleSubmit((data) => {
        parkingLot.address = data.address;
        parkingLot.addressNr = data.addressNr;
        parkingLot.latitude = coords[0];
        parkingLot.longitude = coords[1];
        handleNext(parkingLot);
      })}
    >
      <Grid container justifyContent="left" alignItems="center" spacing={3}>
        <Grid item xs={12} sm={12} md={1}>
          <Typography variant="h6">Strasse:*</Typography>
        </Grid>
        <Grid item xs={9} md={6}>
          <TextFieldElement
            required
            fullWidth
            placeholder="Adresse"
            id="address"
            name="address"
            control={control}
          />
        </Grid>
        <Grid item xs={3} md={2}>
          <TextFieldElement
            required
            placeholder="Nr"
            id="addressNr"
            name="addressNr"
            control={control}
          />
        </Grid>
        <Grid item xs={12} width={100}>
          <Typography variant="h6">
            Koordinaten (Lat: {coords[0].toFixed(4)}, Long:{' '}
            {coords[1].toFixed(4)})
          </Typography>
          <Map onPositionChange={setCoords} coords={coords}></Map>
        </Grid>
      </Grid>
      {navigation()}
    </form>
  );
};

export default Step2;
