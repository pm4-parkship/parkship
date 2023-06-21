import { IconButton, Menu, MenuItem } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import React from 'react';
import { TagData } from '../../models';

export interface TagMenuProps {
  options: TagData[];
  selected: TagData[];
  addTag: (arg: TagData) => void;
}

const TagMenu = ({ options, selected, addTag }: TagMenuProps) => {
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

  const open = Boolean(anchorEl);
  const handleClickListItem = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuItemClick = (key: number) => {
    const tag = options.find((value) => value.id == key);
    if (tag) addTag(tag);
    if (options.length == selected.length) setAnchorEl(null);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };
  return (
    <div>
      <IconButton
        aria-label="delete"
        onClick={handleClickListItem}
        size="small"
      >
        <AddIcon />
      </IconButton>
      <Menu
        id="lock-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          'aria-labelledby': 'lock-button',
          role: 'listbox'
        }}
      >
        {options
          .filter((value) => !selected.some((item) => item.id === value.id))
          .map((option) => (
            <MenuItem
              key={option.id}
              onClick={() => handleMenuItemClick(option.id)}
            >
              {option.name}
            </MenuItem>
          ))}
      </Menu>
    </div>
  );
};
export default TagMenu;
