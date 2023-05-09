import { Box, Button, Grid, Modal, Typography } from '@mui/material';
import React, { useState } from 'react';
import { ErrorMapCtx, z, ZodIssueOptionalMessage } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import {
  CheckboxButtonGroup,
  DatePickerElement,
  TextFieldElement
} from 'react-hook-form-mui';
import { logger } from 'src/logger';
import { makeStyles } from '@mui/styles';
import TagBar, { TagData } from '../search-bar/tag-bar';
import { CreateParkingLotModel } from '../../models';

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
  addParkingLot: (value: CreateParkingLotModel) => void;
  owner: string;
}

export const CreateParkingModal = ({
  showModal = true,
  setShowModal,
  addParkingLot,
  owner
}: CreateParkingModalProps) => {
  const classes = useStyles();

  const [selectedTags, setSelectedTag] = useState<TagData[]>([]);

  const formSchema = z.object({
    parkingName: z.string().min(1, `Bitte geben Sie eine Parkplatznummer ein!`),
    address: z.string().min(1, `Bitte geben Sie eine Adresse ein!`),
    addressNr: z.string().optional(),
    price: z.number().optional(),
    startDateOne: z.date(),
    endDateOne: z.date(),
    description: z.string().optional(),
    days: z.literal(true),
    days: z
      .array(z.object({ id: z.number(), label: z.string() }))
      .length(2, 'Bitte mehr als 2 Tage angeben'),
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
    const body: CreateParkingLotModel = {
      name: data.parkingName,
      address: data.address,
      addressNr: data.addressNr || '',
      description: data.description,
      price: data.price || 0,
      offer: [
        {
          from: data.startDateOne, // todo
          to: data.endDateOne,
          monday: false,
          tuesday: false,
          wednesday: false,
          thursday: false,
          friday: false,
          saturday: false,
          sunday: false
        }
      ],
      tags: selectedTags.map((value) => value.label)
    };
    return;
    addParkingLot(body);
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
          <Grid
            container
            rowSpacing={1}
            direction="column"
            justifyContent="center"
            alignItems="left"
          >
            <Grid item>
              <Typography variant="h6">Bezeichnung:</Typography>
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

            <Grid item>
              <Typography variant="h6">{`Besitzer: ${owner}`}</Typography>
            </Grid>
            <Grid item>
              <Grid
                container
                justifyContent="left"
                alignItems="center"
                spacing={3}
              >
                <Grid item>
                  <Typography variant="h6" className={classes.input}>
                    Wo:
                  </Typography>
                </Grid>
                <Grid item xs={3}>
                  <TextFieldElement
                    required
                    fullWidth
                    id="address"
                    label="Adresse: "
                    name="address"
                    control={control}
                    className={classes.input}
                  />
                </Grid>
                <Grid item xs={1}>
                  <TextFieldElement
                    required
                    id="addressNr"
                    label="Nr."
                    name="addressNr"
                    control={control}
                    className={classes.input}
                  />
                </Grid>
              </Grid>
            </Grid>

            <Grid item>
              <Grid
                container
                justifyContent="left"
                alignItems="center"
                spacing={3}
              >
                <Grid item>
                  <Typography variant="h6" className={classes.input}>
                    Kosten [CHFr. / Tag]:{' '}
                  </Typography>
                </Grid>
                <Grid item xs={1}>
                  <TextFieldElement
                    required
                    fullWidth
                    id="price"
                    label="Kosten: "
                    name="price"
                    type={'number'}
                    control={control}
                    className={classes.input}
                  />
                </Grid>
              </Grid>
            </Grid>

            <Grid item>
              <Grid
                container
                justifyContent="left"
                alignItems="center"
                columnSpacing={3}
                rowSpacing={0}
              >
                <Grid item xs={2}>
                  <Typography variant="h6" className={classes.input}>
                    Buchbarer Zeitraum:{' '}
                  </Typography>
                </Grid>
                <Grid item xs={2}>
                  <DatePickerElement
                    required
                    label="von"
                    name="startDateOne"
                    control={control}
                    className={classes.input}
                  />
                </Grid>
                <Grid item xs={2}>
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
                    className={classes.input}
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
            </Grid>

            <Grid item>
              <Typography variant="h6">Beschreibung:</Typography>
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
            <Grid item>
              <TagBar
                options={dummyTags}
                addTag={addTag}
                handleDelete={handleDelete}
                selected={selectedTags}
              />
            </Grid>
            <Grid item>
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
    borderRadius: '0%',
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
