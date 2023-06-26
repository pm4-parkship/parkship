import { Button, Grid, TextField, useMediaQuery } from '@mui/material';
import React, { useState } from 'react';
import { DatePicker } from '@mui/x-date-pickers';
import TagBar from './tag-bar';
import { SearchParameters } from '../../../pages/search';
import { startOfTomorrow } from 'date-fns';
import { DUMMY_TAGS, TagData } from '../../models';
import { Theme } from '@mui/material/styles';

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
    setSelectedTag((tags) => tags.filter((tag) => tag.id !== key));
  };

  const isSmallScreen = useMediaQuery((theme: Theme) =>
    theme.breakpoints.down('sm')
  );
  const isExtraSmallScreen = useMediaQuery((theme: Theme) =>
    theme.breakpoints.down('xs')
  );

  return (
    <Grid
      container
      justifyContent="center"
      display="flex"
      spacing={1}
      rowSpacing={2}
    >
      <Grid item md={8} lg={5} xl={5} sm={8} xs={11}>
        <TextField
          label={'Suchbegriff'}
          fullWidth={true}
          value={searchTerm}
          onChange={(event) => setSearchTerm(event.currentTarget.value)}
        />
      </Grid>
      <Grid
        container
        item
        md={10}
        lg={5}
        xl={5}
        sm={12}
        xs={11}
        justifyContent="center"
        spacing={1}
      >
        <Grid item md={4} sm={4} xs={6}>
          <DatePicker
            label="von"
            onChange={(newDate) => newDate && setFromDate(newDate)}
            value={fromDate}
            disablePast
            renderInput={(props) => <TextField {...props} required={true} />}
          />
        </Grid>
        <Grid item md={4} sm={4} xs={6}>
          <DatePicker
            label="bis"
            onChange={(newValue) => newValue && setToDate(newValue)}
            value={toDate}
            disablePast
            minDate={fromDate}
            renderInput={(props) => <TextField {...props} required={true} />}
          />
        </Grid>{' '}
        <Grid
          item
          md={1}
          xs={12}
          style={{
            textAlign: 'center' // this does the magic
          }}
        >
          <Button style={{ flex: 1 }} variant="contained" onClick={onSubmit}>
            suchen
          </Button>
        </Grid>
      </Grid>

      <Grid item md={10} hidden={isSmallScreen || isExtraSmallScreen}>
        <TagBar
          options={DUMMY_TAGS}
          addTag={addTag}
          handleDelete={handleDelete}
          selected={selectedTags}
        ></TagBar>
      </Grid>
    </Grid>
  );
};

export default SearchBar;
