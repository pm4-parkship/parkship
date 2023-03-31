import { Chip } from '@mui/material';
import React from 'react';
import { styled } from '@mui/styles';
import { TagData } from './tag-bar';

const ListItem = styled('li')(({ theme }) => ({
  margin: theme.spacing(0.5)
}));

interface TagItemProps {
  data: TagData;
  handleDelete: (arg: number) => void;
}

const TagItem = ({ data, handleDelete }: TagItemProps) => {
  return (
    <ListItem>
      <Chip
        style={{ backgroundColor: '#e0e0e0' }}
        color={'default'}
        label={data.label}
        onDelete={({ key = data.key }) => handleDelete(key)}
      />
    </ListItem>
  );
};

export default TagItem;
