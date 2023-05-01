import {
  Box,
  Button,
  Divider,
  FormControlLabel,
  Grid,
  Modal,
  Paper,
  Stack,
  Table,
  TableBody,
  TableCell,
  TableRow,
  Typography
} from '@mui/material';
import React from 'react';
import { makeStyles } from '@mui/styles';
import { CreateParkingLotModel, OfferModel } from '../../models';
import { Icon } from '@iconify/react';
import { nanoid } from 'nanoid';
import Checkbox from '@mui/material/Checkbox';
import { useFormik } from 'formik';
import { ErrorMapCtx, TypeOf, z, ZodIssueOptionalMessage } from 'zod';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import {
  CheckboxButtonGroup,
  DatePickerElement,
  TextFieldElement
} from 'react-hook-form-mui';
import { logger } from 'src/logger';

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
      description: '',
      
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
      days: data.days,
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
          <Typography variant="h6">Besitzer: Benjamin Blümchen</Typography>
          <Typography variant="h6">Wo:</Typography>
          <TextFieldElement
            required
            fullWidth
            id="address"
            label="Adresse: "
            name="address"
            control={control}
            style={{ marginTop: '10px', height: '60px' }}
          />
          <TextFieldElement
            required
            fullWidth
            id="addressNr"
            label="Nummer: "
            name="addressNr"
            control={control}
            style={{ marginTop: '10px', height: '60px' }}
          />
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

          <Typography variant="h6">Buchbarer Zeitraum: </Typography>
          <div
            style={{
              display: 'flex',
              justifyContent: 'flex-start',
              gap: '10px',
              width: '100%'
            }}
          >
            <DatePickerElement
              required
              label="Start"
              name="startDateOne"
              control={control}
            />
            <DatePickerElement
              required
              label="Ende"
              name="endDateOne"
              control={control}
            />
          </div>

          <Typography variant="h6">Tage:</Typography>
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

          <Typography variant="h6">
            Beschreibung:
          </Typography>
          <TextFieldElement
            fullWidth
            id="description"
            label="Beschreibung: "
            name="description"
            control={control}
            style={{ marginTop: '10px', height: '60px' }}
          />
          <Button
            type={'submit'}
            variant={'contained'}
            sx={{
              width: '94%',
              marginTop: '30px'
            }}
          >
            Speichern
          </Button>
        </form>
      </Box>
    </Modal>
  );
};

const useStyles = makeStyles((theme) => ({
  form: {
    display: 'grid',
    width: '80%'
  },
  button: {
    float: 'right'
  },
  tableRoot: {
    overflow: 'scroll'
  },
  tableBody: {
    display: 'inline-table',
    width: '100%',
    [theme.breakpoints.down('md')]: {
      display: 'grid-inline'
    }
  },
  tableRow: {
    [theme.breakpoints.down('md')]: {
      borderBottom: '0pt solid black'
    },
    [theme.breakpoints.down('sm')]: {
      display: 'grid',
      gap: '0',
      marginBottom: '5px'
    },
    gap: '20px'
  },
  tableCell: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    minWidth: '250px',
    maxWidth: '250px',
    [theme.breakpoints.down('sm')]: {
      minWidth: '300px',
      maxWidth: '300px'
    }
  },
  tableCellLabel: {
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    minWidth: '70px',
    maxWidth: '70px',
    [theme.breakpoints.down('sm')]: {
      minWidth: '100px',
      maxWidth: '100px'
    }
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
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000'
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
