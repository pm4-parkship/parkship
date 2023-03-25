import React from 'react';
import {useRouter} from 'next/router';
import {Grid} from '@mui/material';
import {makeStyles} from '@mui/styles';
import Searchbar from "../src/components/search-parkspace-bar/searchbar";
import Tagsbar from "../src/components/search-parkspace-bar/tags-bar";

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

                    <Searchbar></Searchbar>
                </Grid>
                <Grid item xs={10}>

                    <Tagsbar></Tagsbar>
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
