import { Box, Button, Grid, Modal, Typography } from '@mui/material';
import React from 'react';
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

const dummyTags: TagData[] = [
  { key: 0, label: 'überdacht' },
  { key: 1, label: 'im Schatten' },
  { key: 2, label: 'Ladestation' },
  { key: 3, label: 'barrierefrei' },
  { key: 4, label: 'Garage' }
];
export const CreateParkingModal = ({
  showModal = true,
  setShowModal,
  addParkingLot
}: {
  showModal: boolean;
  setShowModal: (value: boolean) => void;
  addParkingLot: (value: any) => void;
}) => {
  const classes = useStyles();

  const [selectedTags, setSelectedTag] = React.useState<TagData[]>([]);
  const formSchema = z.object({
    parkingName: z.string().min(1),
    address: z.string().min(1),
    addressNr: z.string().min(1),
    price: z.string().min(1),
    startDateOne: z.date().optional(),
    endDateOne: z.date().optional(),
    description: z.string().optional(),
    days: z.any().optional()
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
        if (issue.path.includes('parkingName')) {
          return {
            message: `Bitte geben Sie eine Parkplatznummer ein!`
          };
        }

        if (issue.path.includes('address')) {
          return {
            message: `Bitte geben Sie eine Adresse ein!`
          };
        }
        if (issue.path.includes('addressNr')) {
          return {
            message: `Bitte geben Sie eine Adressnummer ein!`
          };
        }

        if (issue.path.includes('addressNr')) {
          return {
            message: `Bitte geben Sie einen Preis ein!`
          };
        }
      }
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
      price: '',
      startDateOne: '',
      endDateOne: '',
      description: ''
    }
  });

  type ParkingCreationSchema = z.infer<typeof formSchema>;

  const handleFormSubmit = async (data: ParkingCreationSchema) => {
    logger.log('here ', data);
    const body = {
      parkingName: data.parkingName,
      address: data.address,
      addressNr: data.addressNr,
      startDateOne: data.startDateOne,
      endDateOne: data.endDateOne,
      description: data.description,
      days: data.days
    };

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
            xs={12}
            rowSpacing={3}
            direction="column"
            justifyContent="center"
            alignItems="left"
          >
            <Grid item>
              <Typography variant="h6">Parkplatznummer:</Typography>
              <TextFieldElement
                required
                fullWidth
                id="parkingName"
                label="Parkplatznummer: "
                name="parkingName"
                control={control}
                style={{ marginTop: '10px', height: '60px' }}
              />
            </Grid>

            <Grid item>
              <Typography variant="h6">Besitzer: Benjamin Blümchen</Typography>
            </Grid>
            <Grid item>
              <Grid
                container
                justifyContent="left"
                alignItems="center"
                spacing={3}
              >
                <Grid item>
                  <Typography variant="h6">Wo:</Typography>
                </Grid>
                <Grid item xs={3}>
                  <TextFieldElement
                    required
                    fullWidth
                    id="address"
                    label="Adresse: "
                    name="address"
                    control={control}
                  />
                </Grid>
                <Grid item xs={1}>
                  <TextFieldElement
                    required
                    id="addressNr"
                    label="Nr."
                    name="addressNr"
                    control={control}
                  />
                </Grid>
              </Grid>
            </Grid>

            <Grid item>
              <Typography variant="h6">Kosten [CHFr. / Tag]: </Typography>
              <TextFieldElement
                required
                fullWidth
                id="price"
                label="Kosten: "
                name="price"
                control={control}
                style={{ marginTop: '10px', height: '60px' }}
              />
            </Grid>

            <Grid item>
              <Grid
                container
                justifyContent="left"
                alignItems="center"
                spacing={3}
              >
                <Grid item xs={2}>
                  <Typography variant="h6">Buchbarer Zeitraum: </Typography>
                </Grid>
                <Grid item xs={2}>
                  <DatePickerElement
                    required
                    label="von"
                    name="startDateOne"
                    control={control}
                  />
                </Grid>
                <Grid item xs={2}>
                  <DatePickerElement
                    required
                    label="bis"
                    name="endDateOne"
                    control={control}
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
                  <Typography variant="h6">an:</Typography>
                  <CheckboxButtonGroup
                    name="days"
                    returnObject
                    row
                    control={control}
                    options={[
                      {
                        id: '1',
                        label: 'Mo'
                      },
                      {
                        id: '2',
                        label: 'Di'
                      },
                      {
                        id: '3',
                        label: 'Mi'
                      },
                      {
                        id: '4',
                        label: 'Do'
                      },
                      {
                        id: '5',
                        label: 'Fr'
                      },
                      {
                        id: '6',
                        label: 'Sa'
                      },
                      {
                        id: '7',
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
  }
}));
