import TableComponent from "../table/table-component";
import React from "react";
import { RowDataType } from "../table/table-row";
import { SearchResultModel } from "../../models/search/search-result.model";
import { Link, Typography } from "@mui/material";
import { formatDate } from "../../date/date-formatter";
import { makeStyles } from "@mui/styles";

interface SearchParkingLotTableProps {
  parkingLots: SearchResultModel[];
  onRowClick: (parkingLot: SearchResultModel) => void;
  onCreateBookingClick: (parkingLot: SearchResultModel) => void;
}

const SearchParkingLotTable = ({
                                 onRowClick,
                                 parkingLots,
                                 onCreateBookingClick
                               }: SearchParkingLotTableProps) => {
  const classes = useStyles();

  const headerNames = [
    "Bezeichnung",
    "Ort",
    "Besitzer",
    "verfügbar",
    "reservieren"
  ];

  const bookNowLink = (searchItem: SearchResultModel) => (
    <div className={classes.bookLink}
         onClick={(e) => {
           e.stopPropagation();
           onCreateBookingClick(searchItem);
         }}
    >
      <Typography variant={"body2"}>{"reservieren"}</Typography>
    </div>
  );

  const onSelectParkingLot = (data: string[]) => {
    const name = data[0];
    const selected = parkingLots.find((value) => value.name.includes(name));
    if (!selected) return;
    onRowClick(selected);
  };

  const mappedResult: Array<RowDataType> = parkingLots.map((item) => {
    return [
      `${item.name}`,
      `${item.address}`,
      `${item.owner}`,
      `${formatDate(new Date(item.from))} - ${formatDate(new Date(item.to))}`,
      bookNowLink(item)
    ];
  });

  return (
    <div>
      <TableComponent
        data={mappedResult}
        headerNames={headerNames}
        onRowClick={onSelectParkingLot}
        paginationLabel={"Angebote pro Seite"}
        onColumnClick={() => true}
      ></TableComponent>
    </div>
  );
};

const useStyles = makeStyles((theme) => ({
  bookLink: {
    color: theme.palette.primary.main,
    textDecorationStyle:'solid',
    '&:hover': {
      cursor: 'pointer',
      textDecorationLine:'underline',
    }
  }
}));

export default SearchParkingLotTable;
