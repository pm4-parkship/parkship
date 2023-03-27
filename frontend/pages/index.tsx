import React from 'react';
import {useRouter} from 'next/router';
import {Grid} from '@mui/material';
import {makeStyles} from '@mui/styles';
import SearchBar from "../src/components/search-bar/search-bar";
import TagBar, {TagData} from "../src/components/search-bar/tag-bar";
import {logger} from "../src/logger";



const Index = () => {
    const classes = useStyles();
    const router = useRouter();

    return (
        <div className={classes.superRoot}>
            <Grid
                container
                justifyContent="center"
                display="flex"
                spacing={2}
            >
                <Grid item xs={12}>
                    <SearchBar fetchParkspace={logger.log}></SearchBar> /*@todo */
                </Grid>

            </Grid>
        </div>
    );
};

const useStyles = makeStyles((theme) => ({
    superRoot: {
        maxWidth: '2000px',
        margin: '0 auto'
    },
}));

export default Index;
