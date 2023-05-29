import { Typography } from '@mui/material';
import React from 'react';

interface NoDataProps {
  resultSize: number;
  text: string;
}

const NoData = ({ resultSize, text }: NoDataProps) =>
  resultSize == 0 ? (
    <Typography justifyContent="center" alignItems="center" textAlign="center">
      {text}
    </Typography>
  ) : null;

export default NoData;
