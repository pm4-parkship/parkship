import {
  CreateParkingLotModel,
  EMPTY_CREATE_PARKINGLOT,
  OfferModel
} from '../../models';
import { useEffect, useState } from 'react';
import Box from '@mui/material/Box';
import Stepper from '@mui/material/Stepper';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import StepContent from '@mui/material/StepContent';
import Button from '@mui/material/Button';
import Paper from '@mui/material/Paper';
import Typography from '@mui/material/Typography';
import { makeStyles } from '@mui/styles';
import Step1 from './step-1';
import Step2 from './step-2';
import Step3 from './step-3';
import Step4 from './step-4';
import router from 'next/router';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

interface CreateParkingModalProps {
  addParkingLot: (value: CreateParkingLotModel, offers: OfferModel[]) => void;
  parkingLotData: CreateParkingLotModel;
  owner: string;
}

export interface StepProps {
  navigation: () => JSX.Element;
  parkingLot: CreateParkingLotModel;
  handleNext: (CreateParkingLotModel) => void;
}

export const CreateParkingStepper = ({
  addParkingLot,
  owner,
  parkingLotData
}: CreateParkingModalProps) => {
  const classes = useStyles();

  const [parkingLot, setParkingLot] = useState<CreateParkingLotModel>(
    EMPTY_CREATE_PARKINGLOT
  );

  useEffect(() => {
    setParkingLot({ ...parkingLotData });
  }, [parkingLotData]);

  const [activeStep, setActiveStep] = useState<number>(0);
  const handleNext = (updatedOffer: CreateParkingLotModel) => {
    setParkingLot(updatedOffer);
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };
  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };
  const saveParkingLot = () => {
    addParkingLot(parkingLot, parkingLot.offers);
  };

  const handleReset = () => {
    setActiveStep(0);
    setParkingLot(EMPTY_CREATE_PARKINGLOT);
    router.push('/my-parking-lot');
  };

  const Navigation = () => {
    return (
      <Box sx={{ mb: 2 }}>
        <div>
          <Button variant="contained" sx={{ mt: 1, mr: 1 }} type={'submit'}>
            {activeStep === 3 ? 'Speichern' : 'Weiter'}
          </Button>
          <Button
            disabled={activeStep === 0}
            onClick={handleBack}
            sx={{ mt: 1, mr: 1 }}
          >
            Zurück
          </Button>
        </div>
      </Box>
    );
  };

  const steps = [
    {
      label: 'Bezeichnung und Kosten',
      content: (
        <Step1
          owner={owner}
          navigation={Navigation}
          parkingLot={parkingLot}
          handleNext={handleNext}
        />
      )
    },
    {
      label: 'Adresse',
      content: (
        <Step2
          navigation={Navigation}
          parkingLot={parkingLot}
          handleNext={handleNext}
        ></Step2>
      )
    },
    {
      label: 'Verfügbarkeiten',
      content: (
        <Step3
          navigation={Navigation}
          parkingLot={parkingLot}
          handleNext={handleNext}
        ></Step3>
      )
    },
    {
      label: 'Charakteristika',
      content: (
        <Step4
          navigation={Navigation}
          parkingLot={parkingLot}
          handleNext={saveParkingLot}
        ></Step4>
      )
    }
  ];

  return (
    <Box>
      <div className={classes.header}>
        <Button startIcon={<ArrowBackIcon />} onClick={handleReset}>
          Abbrechen
        </Button>
        <Typography variant="h6">{parkingLot.name}</Typography>
      </div>

      <Stepper activeStep={activeStep} orientation="vertical">
        {steps.map((step, index) => (
          <Step key={step.label}>
            <StepLabel
              optional={
                index === steps.length - 1 ? (
                  <Typography variant="caption">Letzer Schritt</Typography>
                ) : null
              }
            >
              {step.label}
            </StepLabel>
            <StepContent className={classes.content}>
              {step.content}
            </StepContent>
          </Step>
        ))}
      </Stepper>
      {activeStep === steps.length && (
        <Paper square elevation={0} sx={{ p: 3 }}>
          <Typography>Alle Schritte abgeschlossen - Sie sind fertig</Typography>
          <Button onClick={handleReset} sx={{ mt: 1, mr: 1 }}>
            Zurück zur Übersicht
          </Button>
        </Paper>
      )}
    </Box>
  );
};

const useStyles = makeStyles(() => ({
  header: {
    flexDirection: 'row',
    display: 'flex',
    justifyContent: 'flex-start',
    alignItems: 'center',
    gap: 20
  },
  content: {
    paddingLeft: '2rem'
  }
}));
