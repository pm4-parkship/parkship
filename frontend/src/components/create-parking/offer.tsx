import {
  Checkbox,
  Grid,
  IconButton,
  TextField,
  Typography
} from '@mui/material';
import { makeStyles } from '@mui/styles';
import React, { FC, useEffect, useState } from 'react';
import { OfferModel } from '../../models';
import DeleteIcon from '@mui/icons-material/Delete';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import FormControlLabel from '@mui/material/FormControlLabel';

interface OfferProps {
  onValuesChange: (values: OfferModel) => void;
  deleteOffer: () => void;
  offerData: OfferModel;
  disable: boolean;
}

export const Offer: FC<OfferProps> = ({
  onValuesChange,
  deleteOffer,
  offerData,
  disable
}) => {
  const classes = useStyles();

  const [startDate, setStartDate] = useState<Date>(offerData.from);
  const [endDate, setEndDate] = useState<Date>(offerData.to);
  const [selectedDays, setSelectedDays] = useState<number[]>([
    offerData.monday ? 1 : 0,
    offerData.tuesday ? 2 : 0,
    offerData.wednesday ? 3 : 0,
    offerData.thursday ? 4 : 0,
    offerData.friday ? 5 : 0,
    offerData.saturday ? 6 : 0,
    offerData.sunday ? 7 : 0
  ]);
  useEffect(() => {
    const offerModel: OfferModel = {
      id: offerData.id,
      from: startDate,
      to: endDate,
      monday: selectedDays.includes(1),
      tuesday: selectedDays.includes(2),
      wednesday: selectedDays.includes(3),
      thursday: selectedDays.includes(4),
      friday: selectedDays.includes(5),
      saturday: selectedDays.includes(6),
      sunday: selectedDays.includes(7)
    };

    onValuesChange(offerModel);
  }, [startDate, endDate, selectedDays]);

  const handleDaySelection = (day: number) => {
    const updatedSelectedDays = selectedDays.includes(day)
      ? selectedDays.filter((d) => d !== day)
      : [...selectedDays, day];

    setSelectedDays(updatedSelectedDays);
  };

  return (
    <>
      <Grid
        container
        justifyContent="left"
        alignItems="center"
        columnSpacing={3}
      >
        <Grid item xs={6} sm={2}>
          <DatePicker
            disabled={disable}
            label="von"
            disablePast={!disable}
            value={startDate}
            onChange={(e) => {
              e && setStartDate(e);
              if (e && endDate < e) e && setEndDate(e);
              // handleDataChange();
            }}
            className={classes.input}
            renderInput={(props) => <TextField {...props} required={true} />}
          />
        </Grid>
        <Grid item xs={6} sm={2}>
          <DatePicker
            disabled={disable}
            label="bis"
            minDate={startDate}
            value={endDate}
            onChange={(e) => {
              e && setEndDate(e);
            }}
            className={classes.input}
            renderInput={(props) => <TextField {...props} required={true} />}
          />
        </Grid>
        <Grid
          container
          item
          style={{
            display: 'inline-flex',
            alignItems: 'center',
            columnGap: '1rem'
            // padding: 0
          }}
          sm={8}
        >
          <Typography
            variant="h6"
            className={classes.input}
            style={{ margin: 0 }}
          >
            an:
          </Typography>
          <Grid item spacing={2}>
            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(1)}
                  onChange={() => handleDaySelection(1)}
                  disabled={disable}
                />
              }
              label="Mo"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(2)}
                  onChange={() => handleDaySelection(2)}
                  disabled={disable}
                />
              }
              label="Di"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(3)}
                  onChange={() => handleDaySelection(3)}
                  disabled={disable}
                />
              }
              label="Mi"
            />
          </Grid>
          <Grid item>
            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(4)}
                  onChange={() => handleDaySelection(4)}
                  disabled={disable}
                />
              }
              label="Do"
            />
            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(5)}
                  onChange={() => handleDaySelection(5)}
                  disabled={disable}
                />
              }
              label="Fr"
            />

            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(6)}
                  onChange={() => handleDaySelection(6)}
                  disabled={disable}
                />
              }
              label="Sa"
            />

            <FormControlLabel
              control={
                <Checkbox
                  checked={selectedDays.includes(7)}
                  onChange={() => handleDaySelection(7)}
                  disabled={disable}
                />
              }
              label="So"
            />
          </Grid>

          <IconButton
            aria-label="delete"
            color={'primary'}
            onClick={deleteOffer}
            disabled={disable}
          >
            <DeleteIcon />
          </IconButton>
        </Grid>
      </Grid>
    </>
  );
};

const useStyles = makeStyles(() => ({
  input: {
    marginBottom: '15px'
  }
}));
