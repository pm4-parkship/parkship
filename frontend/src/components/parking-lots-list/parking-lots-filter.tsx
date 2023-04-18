import React, { useEffect, useState } from 'react';
import {
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Select,
  SelectChangeEvent,
  TextField
} from '@mui/material';
import { ParkingLotsFilterData } from '../../../pages/admin/parking-lots';
import { logger } from '../../logger';
import { ParkingLotState } from '../../models';

interface ParkingLotsFilterProps {
  updateFilter: (data: ParkingLotsFilterData) => void;
}

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

const ParkingLotsFilter = ({ updateFilter }: ParkingLotsFilterProps) => {
  const [searchTerm, setSearchTerm] = useState<string>('');

  const [parkingLotState, setParkingLotState] = React.useState<string[]>([]);
  const handleChange = (event: SelectChangeEvent<typeof parkingLotState>) => {
    logger.log(event);
    const {
      target: { value }
    } = event;
    setParkingLotState(typeof value === 'string' ? value.split(',') : value);
  };

  useEffect(() => {
    const states = new Set(
      parkingLotState.map(
        (value) => ParkingLotState[value as keyof typeof ParkingLotState]
      )
    );
    updateFilter({ states: states, searchTerm: searchTerm });
  }, [parkingLotState, searchTerm]);

  return (
    <Grid
      container
      justifyContent="space-between"
      display="flex"
      spacing={1}
      rowSpacing={2}
      padding={2}
    >
      <Grid item md={5} lg={5} xl={5} sm={8}>
        <TextField
          label={'Suchbegriff'}
          fullWidth={true}
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.currentTarget.value)}
        />
      </Grid>
      <Grid item xs={3}>
        <FormControl fullWidth>
          <InputLabel id="parking-lot-state-label">Parkplatzstatus</InputLabel>
          <Select
            labelId="parking-lot-state-label"
            id="parking-lot-state"
            multiple
            value={parkingLotState}
            onChange={handleChange}
            input={<OutlinedInput label="Parkplatzstatus" />}
            MenuProps={MenuProps}
            fullWidth
          >
            {Object.entries(ParkingLotState).map(([key, state]) => (
              <MenuItem key={key} value={key}>
                {state}
              </MenuItem>
            ))}
          </Select>
        </FormControl>
      </Grid>
    </Grid>
  );
};

export default ParkingLotsFilter;
