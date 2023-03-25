import SearchBar from "../../src/components/search-bar/search-bar";
import {logger} from "../../src/logger";
import React from "react";
import {Grid} from "@mui/material";
import {TagData} from "../../src/components/search-bar/tag-bar";

export interface SearchParameters {
    searchTerm: string,
    fromDate: string,
    toDate: string,
    tags: TagData[]
}

const SearchPage = () =>{

    const fetchParkingSpots = (searchParameters:SearchParameters) => {
        logger.log(searchParameters);
    } ;

    return <Grid>
        <Grid item xs={12}>
            <SearchBar fetchParkingSpots={fetchParkingSpots}></SearchBar>
        </Grid>

        <Grid item xs={12}> {/*todo add result and loading*/}
            loading and result here
        </Grid>
    </Grid>;
};

export default SearchPage;