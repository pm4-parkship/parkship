import { zodResolver } from '@hookform/resolvers/zod';
import { Box, Button, Grid, Modal, Typography } from '@mui/material';
import { makeStyles } from '@mui/styles';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import {
  CheckboxButtonGroup,
  DatePickerElement,
  TextFieldElement
} from 'react-hook-form-mui';
import { logger } from 'src/logger';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { CreateParkingLotModel, OfferModel } from '../../models';
import TagBar, { TagData } from '../search-bar/tag-bar';
import { off } from 'process';

const dummyTags: TagData[] = [
  { key: 0, label: 'überdacht' },
  { key: 1, label: 'im Schatten' },
  { key: 2, label: 'Ladestation' },
  { key: 3, label: 'barrierefrei' },
  { key: 4, label: 'Garage' }
];

interface CreateParkingModalProps {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  addParkingLot: (value: CreateParkingLotModel, offers: OfferModel[]) => void;
  owner: string;
  ownerId: number;
}

export const CreateParkingModal = ({
  showModal = true,
  setShowModal,
  addParkingLot,
  owner,
  ownerId
}: CreateParkingModalProps) => {
  const classes = useStyles();

  const [selectedTags, setSelectedTag] = useState<TagData[]>([]);
  const [offerCount, setOfferCount] = useState<number>(2);
  const maxOffers = 5;

  const offerSchema = z.object({
    startDateOne: z.date(),
    endDateOne: z.date(),
    days: z
      .array(z.object({ id: z.number(), label: z.string() }))
      .min(1, 'Bitte min 1 Tag auswählen')
  });

  const formSchema = z.object({
    parkingName: z.string().min(1, `Bitte geben Sie eine Parkplatznummer ein!`),
    address: z.string().min(1, `Bitte geben Sie eine Adresse ein!`),
    addressNr: z.string().optional(),
    price: z.number().optional(),
    description: z.string().optional(),
    offers: z.array(offerSchema).min(1, 'Bitte min 1 Angebot auswählen'),
    tags: z.string().optional()
  });

  const addTag = (tag: TagData) => {
    setSelectedTag([...selectedTags, tag]);
  };

  const handleDelete = (key: number) => {
    setSelectedTag((tags) => tags.filter((tag) => tag.key !== key));
  };

  const customErrorMap = () => {
    return (issue: ZodIssueOptionalMessage, ctx: ErrorMapCtx) => {
      if (issue.code === z.ZodIssueCode.invalid_string) {
      }
      logger.log(issue);
      return { message: ctx.defaultError };
    };
  };

  const { handleSubmit, control } = useForm({
    resolver: zodResolver(formSchema, {
      errorMap: customErrorMap()
    }),
    mode: 'onSubmit',
    defaultValues: {
      parkingName: '',
      address: '',
      addressNr: '',
      price: 0,
      startDateOne: '',
      endDateOne: '',
      description: '',
      days: []
    }
  });

  type ParkingCreationSchema = z.infer<typeof formSchema>;

  const handleFormSubmit = async (data: ParkingCreationSchema) => {
    logger.log('here ', data);
    const newParkingLot: CreateParkingLotModel = {
      ownerId: ownerId,
      name: data.parkingName,
      address: data.address,
      addressNr: data.addressNr || '',
      description: data.description || '',
      price: data.price || 0,
      tags: selectedTags.map((value) => value.label)
    };

    const offers = data.offers.map(
      (offer) => (offer.days = offer.days.map((day) => day.id))
    );
    const offersOld: OfferModel[] = [
      {
        from: data.startDateOne,
        to: data.endDateOne,
        monday: data.days.filter((e) => e.id === 0).length > 0,
        tuesday: data.days.filter((e) => e.id === 1).length > 0,
        wednesday: data.days.filter((e) => e.id === 2).length > 0,
        thursday: data.days.filter((e) => e.id === 3).length > 0,
        friday: data.days.filter((e) => e.id === 4).length > 0,
        saturday: data.days.filter((e) => e.id === 5).length > 0,
        sunday: data.days.filter((e) => e.id === 6).length > 0
      }
    ];
    addParkingLot(newParkingLot, offersOld);
  };

  return (
    <Modal
      disablePortal
      disableEnforceFocus
      disableAutoFocus
      open={showModal}
      onClose={() => setShowModal(false)}
    >
      <Box className={classes.boxRoot}>
        <form
          className={classes.form}
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
                // label="Bezeichnung:"s
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

            <Grid
              container
              justifyContent="left"
              alignItems="center"
              spacing={3}
            >
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

            <Grid
              container
              justifyContent="left"
              alignItems="center"
              spacing={3}
            >
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

            <Button onClick={() => {setOfferCount(offerCount+1)}}>
              Add offer time
            </Button>

            <Button onClick={() => setOfferCount(offerCount-1)}>
              Remove offer time
            </Button>

            {Object.keys(offerCount).map((key) => {
              return (
                <div key={key}>
                  {' '}
                  <Grid
                    container
                    justifyContent="left"
                    alignItems="center"
                    columnSpacing={3}
                    rowSpacing={0}
                  >
                    <Grid item xs={4}>
                      <Typography variant="h6" className={classes.input}>
                        Buchbarer Zeitraum:*{' '}
                      </Typography>
                    </Grid>

                    <Grid item xs={4}>
                      <DatePickerElement
                        required
                        label="von"
                        name="startDateOne"
                        control={control}
                        className={classes.input}
                      />
                    </Grid>
                    <Grid item xs={4}>
                      <DatePickerElement
                        required
                        label="bis"
                        name="endDateOne"
                        control={control}
                        className={classes.input}
                      />
                    </Grid>
                    <Grid
                      item
                      style={{
                        display: 'inline-flex',
                        alignItems: 'center',
                        columnGap: '1rem'
                      }}
                    >
                      <Typography variant="h6" className={classes.input}>
                        an:
                      </Typography>
                      <CheckboxButtonGroup
                        name="days"
                        returnObject
                        row
                        control={control}
                        options={[
                          {
                            id: 1,
                            label: 'Mo'
                          },
                          {
                            id: 2,
                            label: 'Di'
                          },
                          {
                            id: 3,
                            label: 'Mi'
                          },
                          {
                            id: 4,
                            label: 'Do'
                          },
                          {
                            id: 5,
                            label: 'Fr'
                          },
                          {
                            id: 6,
                            label: 'Sa'
                          },
                          {
                            id: 7,
                            label: 'So'
                          }
                        ]}
                      />
                    </Grid>
                  </Grid>
                </div>
              );
            })}

            <Grid
              container
              justifyContent="left"
              alignItems="center"
              spacing={3}
            >
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
              <Button type={'submit'} variant={'contained'}>
                Speichern
              </Button>
            </Grid>
          </Grid>
        </form>
      </Box>
    </Modal>
  );
};

const useStyles = makeStyles((theme) => ({
  form: {
    width: '80%'
  },
  boxRoot: {
    position: 'absolute',
    display: 'flex',
    justifyContent: 'center',
    overflow: 'scroll',
    textOverflow: 'ellipsis',
    padding: '20px',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '80%',
    maxHeight: '90%',
    [theme.breakpoints.down('sm')]: {
      width: '90%',
      borderRadius: '0%'
    },
    backgroundColor: theme.palette.background.default,
    borderRadius: '4px'
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
