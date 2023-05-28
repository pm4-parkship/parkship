import { zodResolver } from '@hookform/resolvers/zod';
import { Box, Button, Divider, Grid, Modal, Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { useEffect, useState } from 'react';
import { useForm } from 'react-hook-form';
import { TextFieldElement } from 'react-hook-form-mui';
import { toast } from 'react-toastify';
import { z } from 'zod';
import { CreateParkingLotModel, OfferModel } from '../../models';
import TagBar, { TagData } from '../search-bar/tag-bar';
import { OfferComponent } from './bookings/bookings';
import Link from '../link/link';
import dynamic from 'next/dynamic';

const ParkingLotCreateMap = dynamic(() => import('./parking-lot-create-map'), {
  ssr: false
});

const dummyTags: TagData[] = [
  { key: 0, label: 'überdacht' },
  { key: 1, label: 'im Schatten' },
  { key: 2, label: 'Ladestation' },
  { key: 3, label: 'barrierefrei' },
  { key: 4, label: 'Garage' }
];

interface CreateParkingModalProps {
  addParkingLot: (value: CreateParkingLotModel, offers: OfferModel[]) => void;
  owner: string;
}

export const CreateParkingModal = ({
  addParkingLot,
  owner
}: CreateParkingModalProps) => {
  const classes = useStyles();

  const [coords, setCoords] = useState([0, 0]);

  const [selectedTags, setSelectedTag] = useState<TagData[]>([]);
  const [offerCount, setOfferCount] = useState<number>(2);
  const maxOffers = 5;

  const [offersStored, setOffersStored] = useState<OfferModel[]>([]);

  const formSchema = z.object({
    parkingName: z.string().min(1, `Bitte geben Sie eine Parkplatznummer ein!`),
    address: z.string().min(1, `Bitte geben Sie eine Adresse ein!`),
    addressNr: z.string().optional(),
    price: z.number().optional(),
    description: z.string().optional(),
    tags: z.any().optional()
  });

  const addTag = (tag: TagData) => {
    setSelectedTag([...selectedTags, tag]);
  };

  const handleDelete = (key: number) => {
    setSelectedTag((tags) => tags.filter((tag) => tag.key !== key));
  };

  const { handleSubmit, control } = useForm({
    resolver: zodResolver(formSchema),
    mode: 'onSubmit',
    defaultValues: {
      parkingName: '',
      address: '',
      addressNr: '',
      price: 0,
      description: '',
      tags: []
    }
  });

  type ParkingCreationSchema = z.infer<typeof formSchema>;

  const handleFormSubmit = async (data: ParkingCreationSchema) => {
    const createData: CreateParkingLotModel = {
      latitude: coords[0],
      longitude: coords[1],
      address: data.address,
      addressNr: data.addressNr || '',
      description: data.description || '',
      tags: selectedTags.map((tag) => tag.label),
      name: data.parkingName,
      price: data.price || 0
    };
    addParkingLot(createData, offersStored);
  };

  const handleOfferDataSubmit = (data: OfferModel) => {
    setOffersStored([...offersStored, data]);
  };

  return (
    <Box className={classes.boxRoot}>
      <form
        style={{ width: '80%' }}
        onSubmit={handleSubmit((data) => handleFormSubmit(data))}
      >
        <Grid container justifyContent="left" alignItems="center">
          <Grid item xs={4}>
            <Typography variant="h6">Bezeichnung:*</Typography>
          </Grid>
          <Grid item xs={8} sx={{ pt: 1, pl: 1 }}>
            <TextFieldElement
              required
              fullWidth
              id="parkingName"
              label="Bezeichnung:"
              name="parkingName"
              control={control}
              className={classes.input}
            />
          </Grid>

          <Grid item container justifyContent="left" alignItems="center">
            <Grid xs={4}>
              <Typography variant="h6">Besitzer: </Typography>
            </Grid>
            <Grid xs={6} sx={{ mx: 2, mb: 2 }}>
              <Typography variant="h6">{owner}</Typography>
            </Grid>
          </Grid>

          <Grid container justifyContent="left" alignItems="center" spacing={3}>
            <Grid item xs={4}>
              <Typography variant="h6" className={classes.input}>
                Wo:*
              </Typography>
            </Grid>
            <Grid item xs={6}>
              <TextFieldElement
                required
                fullWidth
                id="address"
                name="address"
                control={control}
                className={classes.input}
              />
            </Grid>
            <Grid item xs={2}>
              <TextFieldElement
                required
                id="addressNr"
                name="addressNr"
                control={control}
                className={classes.input}
              />
            </Grid>
          </Grid>

          <Grid container justifyContent="left" alignItems="center" spacing={3}>
            <Grid item xs={4}>
              <Typography variant="h6" className={classes.input}>
                Kosten [CHFr. / Tag]:*{' '}
              </Typography>
            </Grid>
            <Grid item xs={3}>
              <TextFieldElement
                required
                sx={{ maxWidth: 250 }}
                fullWidth
                id="price"
                name="price"
                type={'number'}
                control={control}
                className={classes.input}
              />
            </Grid>
          </Grid>
          <Divider variant="middle" />
          <Grid item xs={12} width={100}>
            <Typography variant="h6">Karte (Lat: {coords[0].toFixed(4)}, Lng: {coords[1].toFixed(4)})</Typography>
            <ParkingLotCreateMap onPositionChange={setCoords}/>
          </Grid>
          {/* <div
              style={{
                display: 'flex',
                justifyContent: 'center',
                width: '100%',
                marginBottom: '1rem'
              }}
            > */}
          <Grid container justifyContent="left" alignItems="center" spacing={3}>
            <Grid item xs={4}></Grid>
            <Grid item xs={4}>
              <Button
                variant="outlined"
                type="button"
                onClick={() => {
                  if (offerCount < maxOffers) {
                    setOfferCount(offerCount + 1);
                  } else {
                    toast.error('Maximal 5 Angebote möglich');
                  }
                }}
                disabled={offerCount >= 5}
              >
                Buchbaren Zeitraum hinzufügen
              </Button>
            </Grid>

            <Grid item xs={4}>
              <Button
                variant="outlined"
                type="button"
                onClick={() => setOfferCount(offerCount - 1)}
                disabled={offerCount == 1}
              >
                Buchbaren Zeitraum entfernen
              </Button>
            </Grid>
          </Grid>

          {Array.from({ length: offerCount }, (_, i) => i + 1).map((key) => {
            return (
              <OfferComponent
                key={key}
                onValuesChange={handleOfferDataSubmit}
              />
            );
          })}

          <Grid container justifyContent="left" alignItems="center" spacing={3}>
            <Grid item xs={4}>
              <Typography variant="h6">Beschreibung:</Typography>
            </Grid>
            <Grid xs={12} sx={{ ml: 3 }}>
              <TextFieldElement
                fullWidth
                multiline
                rows={4}
                id="description"
                label="Beschreibung: "
                name="description"
                control={control}
              />
            </Grid>
          </Grid>
          <Grid item>
            <TagBar
              options={dummyTags}
              addTag={addTag}
              handleDelete={handleDelete}
              selected={selectedTags}
            />
          </Grid>
          <Grid
            item
            xs={12}
            container
            justifyContent="right"
            alignItems="right"
          >
            <Link href='/my-parking-lot'>
              <Button variant={'outlined'}>Abbrechen</Button>
            </Link>
            <Button type={'submit'} variant={'contained'}>
              Speichern
            </Button>
          </Grid>
        </Grid>
      </form>
    </Box>
  );
};

const useStyles = makeStyles((theme) => ({
  boxRoot: {
    display: 'flex',
    justifyContent: 'center'
  },

  header: {
    display: 'flex',
    marginBottom: '20px',
    alignItems: 'center'
  },
  closeIconOnHeader: {
    position: 'absolute',
    right: '2%',
    float: 'right'
  },
  closeIcon: {
    height: '20px',
    width: '20px',
    '&:hover': {
      opacity: '50%'
    }
  },
  input: {
    marginBottom: '15px'
    // height: '60px'
  }
}));
