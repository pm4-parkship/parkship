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
import { logger } from '../../logger';
import { ParkingLotModel } from '../../models';
import { MyParkingLotsFilterData } from '../../../pages/my-parking-lot';

interface ParkingLotsFilterProps {
  updateFilter: (filter: MyParkingLotsFilterData) => void;
  parkingLots: ParkingLotModel[];
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

const ParkingLotsFilter = ({
  updateFilter,
  parkingLots
}: ParkingLotsFilterProps) => {
  const [searchTerm, setSearchTerm] = useState<string>('');

  const [parkingLotName, setParkingLotName] = React.useState<string[]>([]);
  const handleChange = (event: SelectChangeEvent<typeof parkingLotName>) => {
    logger.log(event);
    const {
      target: { value }
    } = event;
    setParkingLotName(typeof value === 'string' ? value.split(',') : value);
  };

  useEffect(() => {
    updateFilter({ names: new Set(parkingLotName), searchTerm: searchTerm });
  }, [parkingLotName, searchTerm]);

  return (
    <Grid
      container
      justifyContent="space-between"
      display="flex"
      spacing={1}
      rowSpacing={2}
      // padding={2}
    >
      <Grid item md={5} lg={5} xl={5} sm={8}>
        <TextField
          label={'Suchbegriff'}
          fullWidth
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.currentTarget.value)}
        />
      </Grid>
      <Grid item xs={3}>
        <FormControl fullWidth>
          <InputLabel id="parking-lot-name-label">Parkplatz</InputLabel>
          <Select
            labelId="parking-lot-name-label"
            id="parking-lot-name"
            multiple
            value={parkingLotName}
            onChange={handleChange}
            input={<OutlinedInput label="Parkplatz" />}
            MenuProps={MenuProps}
            fullWidth
          >
            {parkingLots &&
              parkingLots.map((lot) => (
                <MenuItem key={lot.id} value={lot.name}>
                  {lot.name}
                </MenuItem>
              ))}
          </Select>
        </FormControl>
      </Grid>
    </Grid>
  );
};

export default ParkingLotsFilter;
