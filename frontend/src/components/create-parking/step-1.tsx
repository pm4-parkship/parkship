import { Grid } from '@mui/material';
import Typography from '@mui/material/Typography';
import { TextFieldElement } from 'react-hook-form-mui';
import { z } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { useEffect } from 'react';
import { StepProps } from './create-parking-lot-stepper';

interface Step1Props extends StepProps {
  owner: string;
}

const Step1 = ({ owner, navigation, parkingLot, handleNext }: Step1Props) => {
  useEffect(() => {
    setValue('parkingName', parkingLot.name);
    setValue('price', parkingLot.price);
  }, [parkingLot]);

  const formSchema = z.object({
    parkingName: z.string().min(1, `Bitte geben Sie eine Bezeichnung ein!`),
    price: z.number().optional()
  });
  const { handleSubmit, control, setValue } = useForm({
    resolver: zodResolver(formSchema),
    mode: 'onSubmit'
  });

  return (
    <form
      onSubmit={handleSubmit((data) => {
        parkingLot.name = data.parkingName;
        parkingLot.price = data.price;
        handleNext(parkingLot);
      })}
    >
      <Grid container spacing={3}>
        <Grid item container justifyContent="left" alignItems="center">
          <Grid item xs={4} sm={3} md={2}>
            <Typography variant="h6">Besitzer: </Typography>
          </Grid>
          <Grid item>
            <Typography variant="h6">{owner}</Typography>
          </Grid>
        </Grid>
        <Grid item container justifyContent="left" alignItems="center">
          <Grid item xs={5} sm={3} md={2}>
            <Typography variant="h6">Bezeichnung:*</Typography>
          </Grid>
          <Grid item xs={12} sm={5}>
            <TextFieldElement
              required
              fullWidth
              id="parkingName"
              label="Bezeichnung:"
              name="parkingName"
              control={control}
            />
          </Grid>
        </Grid>
        <Grid item container justifyContent="left" alignItems="center">
          <Grid item xs={7} sm={3} md={2}>
            <Typography variant="h6">Preis [CHFr. / Tag]:</Typography>
          </Grid>
          <Grid item xs={12} sm={5}>
            <TextFieldElement
              fullWidth
              id="price"
              label="Kosten [CHFr. / Tag]:"
              name="price"
              control={control}
              type={'number'}
            />
          </Grid>
        </Grid>
      </Grid>
      {navigation()}
    </form>
  );
};
export default Step1;
