import { Grid, Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { CheckboxButtonGroup, DatePickerElement } from 'react-hook-form-mui';
import { z } from 'zod';
import { FC } from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { OfferModel } from '../../../models';

interface OfferProps {
  onValuesChange: (values: OfferModel) => void;
}

export const offerSchema = z.object({
  startDate: z.date(),
  endDate: z.date(),
  days: z
    .array(z.object({ id: z.number(), label: z.string() }))
    .min(1, 'Bitte min 1 Tag auswählen')
});

export type OfferType = z.infer<typeof offerSchema>;

export const OfferComponent: FC<OfferProps> = ({ onValuesChange }) => {
  const classes = useStyles();

  const { handleSubmit, control } = useForm({
    resolver: zodResolver(offerSchema),
    mode: 'onChange',
    defaultValues: {
      startDate: new Date(),
      endDate: new Date(),
      days: []
    }
  });

  const handleFormSubmit = (data: OfferType) => {
    const offerModel: OfferModel = {
      from: data.startDate,
      to: data.endDate,
      monday: data.days.filter((e) => e.id === 0).length > 0,
      tuesday: data.days.filter((e) => e.id === 1).length > 0,
      wednesday: data.days.filter((e) => e.id === 2).length > 0,
      thursday: data.days.filter((e) => e.id === 3).length > 0,
      friday: data.days.filter((e) => e.id === 4).length > 0,
      saturday: data.days.filter((e) => e.id === 5).length > 0,
      sunday: data.days.filter((e) => e.id === 6).length > 0
    };
    onValuesChange(offerModel);
  };

  return (
    <>
      <form
        style={{ width: '80%' }}
        onChange={handleSubmit((data) => handleFormSubmit(data))}
      >
        <Grid
          container
          justifyContent="left"
          alignItems="center"
          columnSpacing={3}
          rowSpacing={0}
        >
          <Grid item xs={4}>
            <Typography variant="h6" className={classes.input}>
              Buchbarer Zeitraum:*
            </Typography>
          </Grid>

          <Grid item xs={4}>
            <DatePickerElement
              required
              label="von"
              disablePast
              name={`startDate`} // <== Changed here
              control={control}
              className={classes.input}
            />
          </Grid>
          <Grid item xs={4}>
            <DatePickerElement
              required
              label="bis"
              name={`endDate`} // <== Changed here
              control={control}
              disablePast
              className={classes.input}
            />
          </Grid>
          <Grid
            item
            style={{
              display: 'inline-flex',
              alignItems: 'center',
              columnGap: '1rem'
            }}
          >
            <Typography variant="h6" className={classes.input}>
              an:
            </Typography>
            <CheckboxButtonGroup
              name={`days`}
              returnObject
              row
              control={control}
              options={[
                {
                  id: 1,
                  label: 'Mo'
                },
                {
                  id: 2,
                  label: 'Di'
                },
                {
                  id: 3,
                  label: 'Mi'
                },
                {
                  id: 4,
                  label: 'Do'
                },
                {
                  id: 5,
                  label: 'Fr'
                },
                {
                  id: 6,
                  label: 'Sa'
                },
                {
                  id: 7,
                  label: 'So'
                }
              ]}
            />
          </Grid>
        </Grid>
      </form>
    </>
  );
};

const useStyles = makeStyles((theme) => ({
  input: {
    marginBottom: '15px'
    // height: '60px'
  }
}));
