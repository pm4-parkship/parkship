import { Stack, Typography } from '@mui/material';
import React from 'react';

import TagItem from './tag-item';
import TagMenu, { TagMenuProps } from './tag-menu';

export interface TagbarProps extends TagMenuProps {
  handleDelete: (arg: number) => void;
}

const TagBar = ({ options, selected, addTag, handleDelete }: TagbarProps) => {
  return (
    <Stack
      direction="row"
      spacing={1}
      sx={{
        display: 'flex',
        justifyContent: 'left',
        alignItems: 'center',
        flexWrap: 'wrap',
        listStyle: 'none',
        p: 0.5,
        m: 0,
        minHeight: 50
      }}
      component="ul"
    >
      <Typography>Tags:</Typography>
      <TagMenu options={options} selected={selected} addTag={addTag} />

      {selected.map((data) => {
        return (
          <TagItem
            key={data.id}
            data={data}
            handleDelete={handleDelete}
          ></TagItem>
        );
      })}
    </Stack>
  );
};

export default TagBar;
