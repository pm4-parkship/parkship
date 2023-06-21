import {
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Select,
  SelectChangeEvent
} from '@mui/material';
import React, { useEffect } from 'react';
import {
  ReservationState,
  ReservationStateLabel
} from '../../models/reservation/reservation.model';
import { ReservationFilterData } from '../../../pages/my-reservation';

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250
    }
  }
};

interface ReservationFilterProps {
  updateFilter: (data: ReservationFilterData) => void;
}

const ReservationFilter = ({ updateFilter }: ReservationFilterProps) => {
  const [reservationState, setReservationState] = React.useState<string[]>([]);
  const handleChange = (event: SelectChangeEvent<typeof reservationState>) => {
    const {
      target: { value }
    } = event;
    setReservationState(typeof value === 'string' ? value.split(',') : value);
  };

  useEffect(() => {
    const states = new Set(
      reservationState.map(
        (value) => ReservationState[value as keyof typeof ReservationState]
      )
    );
    updateFilter({ states: states });
  }, [reservationState]);

  return (
    <Grid
      container
      justifyContent="left"
      display="flex"
      columnSpacing={2}
      alignItems={'center'}
      padding={2}
    >
      <Grid item xs={12} md={3}>
        <FormControl sx={{ m: 1 }} fullWidth>
          <InputLabel id="reservation-state-label">
            Reservationsstatus
          </InputLabel>
          <Select
            labelId="reservation-state-label"
            id="reservation-state"
            multiple
            value={reservationState}
            onChange={handleChange}
            input={<OutlinedInput label="Reservationsstatus" />}
            MenuProps={MenuProps}
            fullWidth
          >
            {Object.entries(ReservationState).map(([key, state]) => (
              <MenuItem key={key} value={key}>
                {ReservationStateLabel.get(state)}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Grid>
    </Grid>
  );
};

export default ReservationFilter;
