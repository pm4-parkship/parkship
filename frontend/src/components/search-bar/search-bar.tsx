import { Button, Grid, TextField } from '@mui/material';
import React, { useState } from 'react';
import { DatePicker } from '@mui/x-date-pickers';
import TagBar, { TagData } from './tag-bar';
import { SearchParameters } from '../../../pages/search';
import { startOfTomorrow } from 'date-fns';

const dummyTags: TagData[] = [
  { key: 0, label: 'überdacht' },
  { key: 1, label: 'im Schatten' },
  { key: 2, label: 'Ladestation' },
  { key: 3, label: 'barrierefrei' },
  { key: 4, label: 'Garage' }
];

const SearchBar = (props: {
  makeOnSearch: (arg: SearchParameters) => void;
}) => {
  const [fromDate, setFromDate] = useState<Date>(startOfTomorrow());
  const [toDate, setToDate] = useState<Date>(startOfTomorrow());
  const [searchTerm, setSearchTerm] = useState<string>('');
  const [selectedTags, setSelectedTag] = React.useState<TagData[]>([]);

  const onSubmit = () => {
    const searchParam: SearchParameters = {
      searchTerm: searchTerm,
      fromDate: fromDate,
      toDate: toDate,
      tags: selectedTags
    };
    props.makeOnSearch(searchParam);
  };

  const addTag = (tag: TagData) => {
    setSelectedTag([...selectedTags, tag]);
  };

  const handleDelete = (key: number) => {
    setSelectedTag((tags) => tags.filter((tag) => tag.key !== key));
  };

  return (
    <Grid
      container
      justifyContent="center"
      display="flex"
      spacing={1}
      rowSpacing={2}
    >
      <Grid item md={5} lg={5} xl={5} sm={8}>
        <TextField
          label={'Suchbegriff'}
          fullWidth={true}
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.currentTarget.value)}
        />
      </Grid>

      <Grid item md={2} sm={5}>
        <DatePicker
          label="von"
          onChange={(newDate) => newDate && setFromDate(newDate)}
          value={fromDate}
          disablePast
          renderInput={(props) => <TextField {...props} required={true} />}
        />
      </Grid>
      <Grid item md={2} sm={5}>
        <DatePicker
          label="bis"
          onChange={(newValue) => newValue && setToDate(newValue)}
          value={toDate}
          disablePast
          minDate={fromDate}
          renderInput={(props) => <TextField {...props} required={true} />}
        />
      </Grid>
      <Grid item xs={1}>
        <Button variant="contained" onClick={onSubmit}>
          suchen
        </Button>
      </Grid>

      <Grid item md={10}>
        <TagBar
          options={dummyTags}
          addTag={addTag}
          handleDelete={handleDelete}
          selected={selectedTags}
        ></TagBar>
      </Grid>
    </Grid>
  );
};

export default SearchBar;
