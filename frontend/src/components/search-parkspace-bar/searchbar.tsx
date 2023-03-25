import {Button, Grid, TextField} from "@mui/material";
import React, {useState} from "react";
import {DatePicker, LocalizationProvider} from "@mui/x-date-pickers";
import dayjs, {Dayjs} from "dayjs";
import {AdapterDayjs} from "@mui/x-date-pickers/AdapterDayjs";

const Searchbar = () => {

    const [fromDate, setFromDate] = useState<Dayjs | null>(dayjs(Date.now()));
    const [toDate, setToDate] = useState<Dayjs | null>(dayjs(Date.now()));

    return <Grid
        container
        justifyContent="center"
        display="flex"
        spacing={5}
    >
        <Grid item xs={5}>
            <TextField label={"Suchbegriff"} fullWidth={true}/>
        </Grid>
        <Grid item xs={2}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker label="von" onChange={(newValue) => setFromDate(newValue)}
                            value={fromDate}
                            renderInput={props => <TextField {...props} />}/>
            </LocalizationProvider>
        </Grid>
        <Grid item xs={2}>
            <LocalizationProvider dateAdapter={AdapterDayjs}>
                <DatePicker label="bis" onChange={(newValue) => setToDate(newValue)}
                            value={toDate}
                            renderInput={props => <TextField {...props} />}/>
            </LocalizationProvider>
        </Grid>
        <Grid item xs={1}>
            <Button
                variant="contained"
            >
                suchen
            </Button>
        </Grid>

    </Grid>;
};

export default Searchbar;