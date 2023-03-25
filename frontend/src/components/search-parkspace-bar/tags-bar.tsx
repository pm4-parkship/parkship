import {Chip, IconButton, Paper, Stack} from "@mui/material";
import React, {useState} from "react";
import {styled} from "@mui/styles";
import AddIcon from '@mui/icons-material/Add';

interface TagData {
    key: number;
    label: string;
}

const ListItem = styled('li')(({theme}) => ({
    margin: theme.spacing(0.5),
}));
const Tagsbar = () => {

    const [tagData, setTagData] = React.useState<readonly TagData[]>([
        {key: 0, label: 'überdacht'},
        {key: 1, label: 'im Schatten'},
        {key: 2, label: 'Ladestation'},
        {key: 3, label: 'barrierefrei'},
        {key: 4, label: 'Garage'},
    ]);
    const handleDelete = (key: number) => {
        setTagData((tags) => tags.filter((tag) => tag.key !== key));
    };

    const openTagList = () => {
        return;
    };
    return <Stack direction="row" spacing={1}
                  sx={{
                      display: 'flex',
                      justifyContent: 'left',
                      alignItems: 'center',
                      flexWrap: 'wrap',
                      listStyle: 'none',
                      p: 0.5,
                      m: 0,
                  }}
                  component="ul"
    >
        {tagData.map((data) => {

            return (
                <ListItem key={data.key}>
                    <Chip
                        style={{backgroundColor: "#e0e0e0"}}
                        color={"default"}
                        label={data.label}
                        onDelete={({key = data.key}) => handleDelete(key)}
                    />
                </ListItem>
            );
        })}
        <ListItem>
            <IconButton aria-label="delete" onClick={()=>openTagList()} size="small">
                <AddIcon/>
            </IconButton>
        </ListItem>
    </Stack>;
};

export default Tagsbar;